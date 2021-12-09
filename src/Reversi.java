import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import static javax.swing.ScrollPaneConstants.*;

/**
 * The main class for the Reversi game.
 *
 * @author Andrei Constantin
 * @version 23-03-2021
 */
public class Reversi
{
    private static ArrayList<Theme> themes;

    private boolean autoSaveOn=false;
    private static final TutorialFrame tutorial = new TutorialFrame();

    private static final String WELCOME = "Welcome to Reversi! Please insert the players' name and press Play.";
    private Session currentSession;

    private Theme currentTheme;

    private JMenuItem newGameItem;
    private JFrame frame;
    private StatusBarPanel statusBar;
    private PlayerStatusPanel playerStatus;
    private GameBoardGUI currentBoard;

    /**
     * The main function of the Reversi game
     */
    public static void main(String[] args)
    {
        changeLook();
        createThemes();
        if(themes.size()==0)
            themes.add(new Theme("Classic", Color.BLACK, "Black", Color.WHITE, "White"));
        new Reversi();
    }

    /**
     * Create the themes for the app.
     */
    private static void createThemes()
    {
        themes = new ArrayList<>();
        themes.add(new Theme("Classic", Color.BLACK, "Black", Color.WHITE, "White"));
        themes.add(new Theme("Red VS Blue", Color.RED, "Red", Color.BLUE, "Blue"));
        themes.add(new Theme("Spring", Color.GREEN, "Green", Color.YELLOW, "Yellow"));
        themes.add(new Theme("Retro", Color.ORANGE, "Orange", new Color(101,67,33), "Brown"));
        themes.add(new Theme("Neon", Color.MAGENTA, "Magenta", new Color(48,25,52), "Purple"));
    }

    /**
     * Constructor for objects of class Reversi
     */
    public Reversi()
    {
        makeFrame();
    }

    /**
     * Create the frame for the application
     */
    private void makeFrame()
    {
        currentTheme = themes.get(0);
        frame = new JFrame("Reversi");
        frame.setMinimumSize(new Dimension(720, 480));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Image reversiIcon = Toolkit.getDefaultToolkit().getImage("./icons/reversi_new.png");
        frame.setIconImage(reversiIcon);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        makeMenuBar(frame);

        createPlayerPanel();
        createStatusBar();
        createBoard(8, null, null);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Create the board panel that contains the game board.
     * @param size The size of the board
     */
    private void createBoard(int size, SessionPlayer player1, SessionPlayer player2)
    {
        if(currentBoard!=null)
        {
            BorderLayout layout = (BorderLayout)(frame.getContentPane()).getLayout();
            frame.getContentPane().remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        if(player1==null || player2==null)
            currentBoard = new GameBoardGUI(new BoardPlayer(""), new BoardPlayer(""), size, this);
        else
            currentBoard = new GameBoardGUI(new BoardPlayer(player1.getName()), new BoardPlayer(player2.getName()), size, this);
        frame.getContentPane().add(currentBoard.getParentPanel(), BorderLayout.CENTER);
    }

    /**
     * Create the panel for the players, when the players are already known.
     */
    private void createPlayerPanel(SessionPlayer player1, SessionPlayer player2)
    {
        if(playerStatus!=null)
        {
            BorderLayout layout = (BorderLayout)(frame.getContentPane()).getLayout();
            frame.getContentPane().remove(layout.getLayoutComponent(BorderLayout.LINE_END));
        }

        playerStatus = new PlayerStatusPanel(player1, player2, this);
        frame.getContentPane().add(playerStatus, BorderLayout.LINE_END);
    }

    /**
     * Create the panel for the players.
     */
    private void createPlayerPanel()
    {
        if(playerStatus!=null)
        {
            BorderLayout layout = (BorderLayout)(frame.getContentPane()).getLayout();
            frame.getContentPane().remove(layout.getLayoutComponent(BorderLayout.LINE_END));
        }

        playerStatus = new PlayerStatusPanel(this);
        frame.getContentPane().add(playerStatus, BorderLayout.LINE_END);
    }

    /**
     * Create the panel for the status bar
     */
    private void createStatusBar()
    {
        if(statusBar!=null)
        {
            BorderLayout layout = (BorderLayout)(frame.getContentPane()).getLayout();
            frame.getContentPane().remove(layout.getLayoutComponent(BorderLayout.PAGE_END));
        }
        statusBar = new StatusBarPanel(this, WELCOME);
        JScrollPane scroll = new JScrollPane(statusBar, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(scroll, BorderLayout.PAGE_END);
    }

    /**
     * Create the main frame's menu bar.
     * @param frame The frame that the menu bar should be added to.
     */
    private void makeMenuBar(JFrame frame)
    {
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        UIManager.put("MenuBar.background", Color.ORANGE);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        ImageIcon newSessionIcon, saveSessionIcon, loadSessionIcon, newGameIcon, quitIcon, quickSaveIcon, quickLoadIcon, filmIcon, themeIcon;
        newSessionIcon = getImageIcon("./icons/create_session.png");
        saveSessionIcon = getImageIcon("./icons/save.png");
        loadSessionIcon = getImageIcon("./icons/load.png");
        newGameIcon = getImageIcon("./icons/create_game.png");
        quitIcon = getImageIcon("./icons/quit.png");
        quickSaveIcon = getImageIcon("./icons/quicksave.png");
        quickLoadIcon = getImageIcon("./icons/quickload.png");
        filmIcon = getImageIcon("./icons/film.png");
        themeIcon = getImageIcon("./icons/theme.png");

        JMenuItem newSession = new JMenuItem("New Session", newSessionIcon);
        newSession.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        newSession.addActionListener(e -> newSession(frame));
        fileMenu.add(newSession);

        JMenuItem saveSession = new JMenuItem("Save Session", saveSessionIcon);
        saveSession.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
        saveSession.addActionListener(e -> saveSession(frame));
        fileMenu.add(saveSession);

        JMenuItem loadSession = new JMenuItem("Load Session", loadSessionIcon);
        loadSession.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, SHORTCUT_MASK));
        loadSession.addActionListener(e -> { loadSession(frame);
        });
        fileMenu.add(loadSession);

        JCheckBoxMenuItem autoSaveSession = new JCheckBoxMenuItem("Autosave OFF");
        autoSaveSession.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, SHORTCUT_MASK));
        autoSaveSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton button = (AbstractButton) e.getSource();
                autoSaveOn = button.getModel().isSelected();
                button.setText(autoSaveOn?"Autosave ON":"Autosave OFF");
            }
        });
        fileMenu.add(autoSaveSession);

        fileMenu.addSeparator();

        JMenuItem quickSaveItem = new JMenuItem("Quick Save", quickSaveIcon);
        quickSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
        quickSaveItem.addActionListener(e -> quickSave());
        fileMenu.add(quickSaveItem);

        JMenuItem quickLoadItem = new JMenuItem("Quick Load", quickLoadIcon);
        quickLoadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SHORTCUT_MASK));
        quickLoadItem.addActionListener(e -> quickLoad());
        fileMenu.add(quickLoadItem);

        fileMenu.addSeparator();

        newGameItem = new JMenuItem("New Game", newGameIcon);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, SHORTCUT_MASK));
        newGameItem.addActionListener(e -> newGame(frame));
        newGameItem.setEnabled(false);
        fileMenu.add(newGameItem);

        fileMenu.addSeparator();

        JMenuItem quitItem = new JMenuItem("Quit", quitIcon);
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        quitItem.addActionListener(e -> quit());
        fileMenu.add(quitItem);


        JMenu captureMenu = new JMenu("Capture");
        menuBar.add(captureMenu);

        JMenuItem screenshotItem = new JMenuItem("Screenshot", filmIcon);
        screenshotItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, SHORTCUT_MASK));
        screenshotItem.addActionListener(e -> screenShot());
        captureMenu.add(screenshotItem);


        menuBar.add(Box.createHorizontalGlue());

        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);

        JMenuItem themeItem = new JMenuItem("Change theme", themeIcon);
        themeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SHORTCUT_MASK));
        themeItem.addActionListener(e -> changeTheme());
        settingsMenu.add(themeItem);

        Action actionHelp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTutorial();
            }
        };
        JButton helpButton = new JButton(actionHelp);
        helpButton.setText("Help");
        helpButton.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpButton);
    }

    /**
     * Create a screenshot of the current Reversi game.
     */
    private void screenShot()
    {
        BufferedImage screenshot = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        frame.paint(screenshot.getGraphics());

        File directory = new File("./screenshots/");
        if(!directory.exists())
            directory.mkdir();

        int counter=1;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        File file = new File("./screenshots/screenshot_"+ new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss").format(timestamp)+".png");
        if(file.exists())
            showMessage(frame, "Could not save screenshot. Try again.");
        else {
            try {
                ImageIO.write(screenshot, "png", file);
                showMessage(frame, "The screenshot was saved successfully to " + file.getAbsolutePath());
            } catch (IOException e) {
                showMessage(frame, "There was an error saving the screenshot.");
            }
        }
    }

    /**
     * Display the tutorial frame.
     */
    private void showTutorial()
    {
        tutorial.setVisible(true);
    }

    /**
     * Create a new session. This will terminate the current session.
     * @param frame The main Reversi frame
     */
    private void newSession(JFrame frame)
    {
        Object[] options = {"New session",
                "Cancel"};
        int n = JOptionPane.showOptionDialog(frame,
                "Are you sure you want to create a new session? The current session will be removed and all unsaved progress will be erased.",
                "Create new session",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if(n==0)
        {
            currentSession=null;
            createStatusBar();
            createPlayerPanel();
            createBoard(8, null, null);
            newGameItem.setEnabled(false);
        }

        frame.pack();
    }

    /**
     * Save the current session.
     * @param frame The main Reversi frame
     */
    private void saveSession(JFrame frame)
    {
        if(currentSession==null)
            showMessage(frame, "No session running, there is nothing to save!");
        else {
            String fileName = (String) JOptionPane.showInputDialog(
                    frame,
                    "Choose a name for the file:",
                    "Save File",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");

            if(fileName==null || fileName.isEmpty())
                showMessage(frame, "The file name must not be empty.");
            else
            {
                try {
                    updateSession();
                    File file = new File("./saves/"+fileName+".rev");
                    if(file.exists()) {
                        Object[] options = {"Save",
                                "Cancel"};
                        int n = JOptionPane.showOptionDialog(frame,
                                "The file already exists. Are you sure you want to overwrite?",
                                "Save Session",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
                        if (n == 0)
                        {
                            currentSession.saveToFile(fileName);
                            showMessage(frame, "Save successful.");
                        }
                    }
                    else
                    {
                        currentSession.saveToFile(fileName);
                        showMessage(frame, "Save successful.");
                    }
                } catch (SaveSessionException e) {
                    showMessage(frame, "There was an error saving the file.");
                }
            }
        }
    }

    /**
     * Load a session. This will terminate the current session.
     * @param frame The main Reversi frame
     */
    private void loadSession(JFrame frame) {
        String fileName="";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "Reversi save files (*.rev)";
            }
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".rev");
                }
            }
        });

        Session.createSavesFolder();
        fileChooser.setCurrentDirectory(new File("./saves/"));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getPath();
        }

        if(!fileName.isEmpty())
        {
            Object[] options = {"Load",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(frame,
                    "Are you sure you want to load this file? The current session will be removed and all unsaved progress will be erased.",
                    "Load Session",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if(n==0)
            {
                load(fileName);
            }
        }
    }

    /**
     * Load the given fileName
     * @param fileName The file name
     */
    private void load(String fileName)
    {
        try {
            currentSession = Session.loadFromFile(fileName);
            setStatus(currentSession.getStatus());
            createPlayerPanel(currentSession.getPlayer1(), currentSession.getPlayer2());
            newGameItem.setEnabled(true);
            if(currentBoard!=null)
            {
                BorderLayout layout = (BorderLayout)(frame.getContentPane()).getLayout();
                frame.getContentPane().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                currentBoard=null;
            }
            currentBoard = new GameBoardGUI(currentSession.getGameBoard(), this);
            frame.getContentPane().add(currentBoard.getParentPanel(), BorderLayout.CENTER);

            frame.pack();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(frame, "There was an error loading the save file.");
        }
    }

    /**
     * Create a new game within the current session. The previous game will be destroyed.
     * @param frame The main Reversi frame
     */
    private void newGame(JFrame frame)
    {
        class SizeChoice
        {
            public int size=8;
            public String text="8 x 8";

            public SizeChoice(int size)
            {
                this.size = size;
                text = size + " x " + size;
            }
        }

        SizeChoice[] choices = new SizeChoice[GameBoard.MAX_BOARD_SIZE/2 - 1];
        Object[] possibilities = new Object[GameBoard.MAX_BOARD_SIZE/2-1];

        for(int i=GameBoard.MIN_BOARD_SIZE, pos=0; i<=GameBoard.MAX_BOARD_SIZE; i+=2, pos++)
        {
            choices[pos] = new SizeChoice(i);
            possibilities[pos] = choices[pos].text;
        }

        String choice = (String)JOptionPane.showInputDialog(
                frame,
                "Choose the number of lines and columns for the Reversi game",
                "Choose size",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibilities,
                "8 x 8");
        if(choice!=null)
        {
            Object[] options = {"New Game",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(frame,
                    "Are you sure you want to create a new game? The current board will be removed.",
                    "New Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n==0) {
                int size = 8;
                boolean found = false;
                for (int i = 0; i < choices.length && !found; i++)
                    if (choice.equals(choices[i].text)) {
                        found = true;
                        size = choices[i].size;
                    }

                createBoard(size, currentSession.getPlayer1(), currentSession.getPlayer2());
                currentSession.setGameBoard(currentBoard.getBoard());
                //statusBar.setSkipEnabled(true);
            }
        }
        frame.pack();
    }

    /**
     * Get the scaled image icon for the image at the given path.
     * @param path The path of the image
     * @return The resized image icon, or null if it could not be found
     */
    private static ImageIcon getImageIcon(String path) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(18, 18, Image.SCALE_DEFAULT));
    }

    /**
     * Change the look of the User Interface to the System Look and Feel.
     */
    private static void changeLook()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Error changing the look");
        }
    }

    /**
     * Shows a message dialog for the user.
     * @param frame The frame in which to show the messsage
     * @param message The message to be sent to the user
     */
    public static void showMessage(JFrame frame, String message)
    {
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * Quit function: quit the application.
     */
    private void quit()
    {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to quit? All unsaved progress will be lost.",
                "Quit",
                JOptionPane.YES_NO_OPTION);
        if (choice == 0)
            System.exit(0);
    }

    /**
     * Sets the status to the current player's turn.
     * @param player The player
     * @return The new status
     */
    public String setStatusPlayer(BoardPlayer player)
    {
        return setStatus("It's " + player.getName() + "'s turn!");
    }

    /**
     * Sets the status to signify an illegal move, plus the current player's turn.
     * @param player The player
     * @return The new status
     */
    public String setStatusIllegalMove(BoardPlayer player)
    {
        String playerString = setStatusPlayer(player);
        return setStatus("Illegal move. "+playerString);
    }

    /**
     * Sets the status to a given string.
     * @param newStatus The new status string
     * @return The new status
     */
    public String setStatus(String newStatus)
    {
        statusBar.setStatus(newStatus);
        return statusBar.getStatus();
    }

    /**
     * Get the current theme.
     * @return The current theme
     */
    public Theme getCurrentTheme()
    {
        return currentTheme;
    }

    /**
     * Get the player status panel.
     * @return The player status panel
     */
    public PlayerStatusPanel getPlayerStatus()
    {
        return playerStatus;
    }

    /**
     * Start the game
     * @param player1 Player 1
     * @param player2 Player 2
     */
    public void createSession(SessionPlayer player1, SessionPlayer player2)
    {
        //statusBar.setSkipEnabled(true);
        newGameItem.setEnabled(true);
        GameBoard board = currentBoard.getBoard();
        createBoard(8, player1, player2);
        currentSession = new Session(player1, player2, board, statusBar.getStatus());
    }

    /**
     * Set the victorious player in the status panel.
     * @param winningPlayer The winning player
     */
    public void setWin(BoardPlayer winningPlayer)
    {
        if(winningPlayer==GameBoard.tiePlayer)
        {
            statusBar.setStatus("Tie!");
        }
        else
        {
            statusBar.setStatus("Player "+winningPlayer.getName()+" is victorious.");
            if(winningPlayer == currentBoard.getPlayer1())
                playerStatus.incrementPlayerOneScore();
            else
                playerStatus.incrementPlayerTwoScore();
        }
        playerStatus.setReplayVisibility(true);

        //statusBar.setSkipEnabled(false);
    }

    /**
     * Show a dialog box telling the current player that he has no moves available.
     * @param player The current player
     */
    public void noMoreMoves(BoardPlayer player)
    {
        JOptionPane.showMessageDialog(frame,
                "Player "+player.getName()+" has no possible moves.",
                "No possible moves",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Skip the turn of the current player.
     */
    public void skipTurn()
    {
        currentBoard.skipTurn();
    }

    /**
     * Get the main Reversi frame.
     * @return
     */
    public JFrame getFrame()
    {
        return frame;
    }

    /**
     * Called whenever a turn was played on the game board.
     */
    public void turnPlayed()
    {
        if(autoSaveOn) {
            try {
                updateSession();
                currentSession.autoSave();
            } catch (SaveSessionException e) {
                //do nothing
            }
        }
    }

    /**
     * Quick save the current session.
     */
    private void quickSave()
    {
        if(currentSession==null)
            showMessage(frame, "No session running, there is nothing to save!");
        else {
            updateSession();
            try {
                Session.quickSave(currentSession);
            } catch (SaveSessionException e) {
                showMessage(frame, "There was an error saving the file.");
            }
        }
    }

    /**
     * Quick load the latest save file from the saves folder.
     */
    private void quickLoad()
    {
        Session.createSavesFolder();
        File dir = new File("./saves/");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".rev"));
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        if(files.length==0)
            showMessage(frame, "There are no save files to load from.");
        else
            load(files[0].getPath());
    }

    /**
     * Update the current session with the latest information.
     */
    private void updateSession()
    {
        currentSession.setGameBoard(currentBoard.getBoard());
        currentSession.setStatus(statusBar.getStatus());
        currentSession.setPlayer1(playerStatus.getPlayer1());
        currentSession.setPlayer2(playerStatus.getPlayer2());
    }

    /**
     * Creates a new board of the current board's size.
     */
    public void replay()
    {
        createBoard(currentBoard.getBoard().getSize(), playerStatus.getPlayer1(), playerStatus.getPlayer2());
    }

    /**
     * Change the theme of the project
     */
    public void changeTheme()
    {
        Object[] possibilities = themes.toArray();

        Theme choice = (Theme) JOptionPane.showInputDialog(
                frame,
                "Choose a new theme:",
                "Theme choice",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if(choice!=null)
        {
            currentTheme = choice;

            currentBoard.changeTheme();
            playerStatus.changeTheme();
        }
    }
}
