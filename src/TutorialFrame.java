import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Spliterator;
import java.util.concurrent.Flow;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * A frame containing information about the game and the different buttons inside the game.
 *
 * @author Andrei Constantin
 * @version 22-04-2021
 */
public class TutorialFrame extends JFrame
{
    private static final int TUTORIAL_WIDTH = 720;
    private static final int TUTORIAL_HEIGHT = 480;

    private static final int TUTORIAL_CONTENT_WIDTH = TUTORIAL_WIDTH*19/20;
    private static final int TUTORIAL_CONTENT_HEIGHT = TUTORIAL_HEIGHT-10;

    /**
     * Constructor for the tutorial frame.
     */
    public TutorialFrame()
    {
        super("Help");
        setResizable(false);
        setMinimumSize(new Dimension(TUTORIAL_WIDTH, TUTORIAL_HEIGHT));
        Image helpIcon = Toolkit.getDefaultToolkit().getImage("./icons/help_reversi.png");
        setIconImage(helpIcon);

        TutorialFrame frame = this;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });

        add(createTabbedPane());
        pack();
        setVisible(false);
    }

    /**
     * Create the tabbed pane.
     * @return The tabbed pane
     */
    private JTabbedPane createTabbedPane()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon tabIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./icons/help.png").getScaledInstance(15, 15, Image.SCALE_SMOOTH));

        JComponent gameTutorial = createGameTutorial();
        tabbedPane.addTab("Playing Reversi", tabIcon, gameTutorial, "Explore the game of Reversi and how it's played.");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_R);

        JComponent panel2 = createInterfaceTutorial();
        tabbedPane.addTab("Exploring the interface", tabIcon, panel2, "See what every bit of the game's page does.");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_E);

        JComponent panel3 = createSavingTutorial();
        tabbedPane.addTab("Saving/Loading", tabIcon, panel3, "Learng about saving and loading your games.");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_S);

        return tabbedPane;
    }

    /**
     * Create the tutorial panel for the game.
     * @return The game tutorial panel
     */
    private JComponent createGameTutorial()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        int height=0;

        JScrollPane scrollPane = new JScrollPane(panel, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(TUTORIAL_WIDTH, TUTORIAL_HEIGHT));

        height += createTutorialText(panel, "Reversi is a two-player game in which the players place disks on a rectangular board. The objective of the game is to have the majority of disks turned to display the player's colour when the last playable empty square is filled.");

        height += createTutorialText(panel, "The board is usually of size 8x8, but it can vary. The number of rows and columns must be equal and even. At the start of the game, four disks of alternating colours are disposed in the center of the board.");

        height += createTutorialImage(panel, "./images/initial_board.png");

        height += createTutorialText(panel, "When a player places a disk on the board, they also flip all enemy disks between the newly-placed disk and any disks that belong to the same player. Each player can only place a disk in such a way that at least one enemy disk is flipped (changed).");

        height += createTutorialImage(panel, "./images/placing_disk.png");

        height += createTutorialText(panel, "If a player cannot place a disk, their turn is skipped, and the control changes to the other player.");

        height += createTutorialImage(panel, "./images/skip_turn.png");

        height += createTutorialText(panel, "The end of the game is achieved when neither player can place a disk on the board (this includes the posibility of the entire board being filled). The winner is the one with the most pieces on the board at the end of the game. If the number of pieces of both players is the same, then the game results in a tie.");

        height += createTutorialImage(panel, "./images/end_game.png");

        setTutorialPanelSize(panel, height);
        return scrollPane;
    }

    /**
     * Create the tutorial panel for the interface.
     * @return The interface tutorial panel.
     */
    private JComponent createInterfaceTutorial()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        int height = 0;

        JScrollPane scrollPane = new JScrollPane(panel, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(TUTORIAL_WIDTH, TUTORIAL_HEIGHT));

        height += createTutorialText(panel, "The interface of the Reversi game contains three sections and the menu bar.");

        height += createTutorialImage(panel, "./images/full_page.png");

        height += createTutorialText(panel, "1. The game board contains the actual game area and is the core of the game. Players can click on the highlighted squares to place their pieces. For instructions on how to play the game, please see the 'Playing Reversi' section.");

        height += createTutorialText(panel, "2. The player status is where the players can initially choose their names and start the game. It is also where the players can see the number of disks they currently have on the board, as well as the number of wins so far. After starting the game by hitting the 'PLAY' button, the button will disappear. When the game is ended, a 'REPLAY' button will appear to create a new board of the same size as the existing one.");

        height += createTutorialText(panel, "3. The status bar contains simple information about the current status of the game. It displays the player whose turn it currently is, and informs the player that the move they tried to make is illegal.");

        height += createTutorialText(panel, "4. The menu bar contains several useful menus, which can be accessed by pressing on the buttons.");

        height += createTutorialImage(panel, "./images/file_menu.png");

        height += createTutorialText(panel, "a) The file menu ('File') offers the possibility of starting a new session (new players), saving or loading the current session (along with the current board), quicksaving and quickloading, autosaving, creating a new board (keeping the players the same) and quitting the application. For more information about saving and loading, please see the 'Saving/Loading' section.");

        height += createTutorialImage(panel, "./images/capture_menu.png");

        height += createTutorialText(panel, "b) The capture menu ('Capture') allows the user to take screenshots.");

        height += createTutorialImage(panel, "./images/settings_help.png");

        height += createTutorialText(panel, "c) The Settings menu allows the user to change the theme. Several are available, and they change the colors of the two players.");

        height += createTutorialImage(panel, "./images/theme_choice.png");

        height += createTutorialText(panel, "d) The Help button opens up the help window (the one you are currently seeing). It contains useful information about the application.");

        setTutorialPanelSize(panel, height);

        return scrollPane;
    }

    /**
     * Create the tutorial panel for saving and loading.
     * @return The saving tutorial panel
     */
    private JComponent createSavingTutorial()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        int height = 0;

        JScrollPane scrollPane = new JScrollPane(panel, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(TUTORIAL_WIDTH, TUTORIAL_HEIGHT));

        height += createTutorialText(panel, "The game contains multiple ways of saving and loading the current session. A session consists of the players and the last board that they have played. All of this information is saved in a .rev file.");

        height += createTutorialImage(panel, "./images/saving.png");

        height += createTutorialText(panel, "There are three ways of saving in Reversi:");
        height += createTutorialText(panel, "1. Normal saving is done by clicking 'Save Game' in the 'File' menu (or the shortcut CTRL+S on Windows, CMD+S on Mac). This allows the user to set a name for the file, and saves the session in the 'saves' folder (see above). If the file already exists, a popup will appear to ask if the user wants to overwrite the existing save.");
        height += createTutorialText(panel, "2. Auto-saving creates a file with the name 'AUTO player1-player2', where player1 is the name of the first player, and player2 the name of the second player. Auto-saving is turned off at the beginning and it can be switched on/off from the 'File' menu.");
        height += createTutorialText(panel, "3. Quick-saving always overwrites the file with the name 'QUICK'. Although volatile, it is a quick way of saving the file, as it does not ask for a file name or overwrite permissions. It can be used by clicking 'Quick Save (or the shortcut CTRL+W on Windows, CMD+W on Mac).");

        height += createTutorialImage(panel, "./images/loading.png");

        height += createTutorialText(panel, "There are three ways of loading in Reversi:");
        height += createTutorialText(panel, "1. Normal loading is done by clicking 'Load Game' in the 'File' menu (or the shortcut CTRL+L on Windows, CMD+L on Mac). This opens up a file explorer for the user to choose a .rev file they want to load. Loading will remove the current session, but it will prompt the user with a message before doing so.");
        height += createTutorialText(panel, "2. Quick-loading is done by clicking 'Quick Load' in the 'File' menu (or the shortcut CTRL+E on Windows, CMD+E on Mac). This always loads the latest save file. Just like normal loading, quick-loading will remove the current session. Be careful, as quick-loading does not prompt the user with a message beforehand, and all existing progress could be lost.");

        setTutorialPanelSize(panel, height);

        return scrollPane;
    }

    /**
     * Set the preferred size for a tutorial panel, based on the given height.
     * @param panel The panel
     * @param height The raw height of the tutorial information
     */
    private void setTutorialPanelSize(JPanel panel, int height)
    {
        panel.setPreferredSize(new Dimension(TUTORIAL_CONTENT_WIDTH, height*10/7));
    }

    /**
     * Create a tutorial text area and add it to the given panel.
     * @param panel The panel
     * @param text The text
     * @return The height of the newly added component
     */
    private int createTutorialText(JPanel panel, String text)
    {
        JTextArea textArea = new JTextArea(1, 60);
        textArea.append(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(10, 100));

        panel.add(textArea);

        return (int) textArea.getPreferredSize().getHeight();
    }

    /**
     * Create a tutorial image label and add it to the given panel.
     * @param panel The panel
     * @param path The path to the image
     * @return The height of the newly added component
     */
    private int createTutorialImage(JPanel panel, String path)
    {
        JLabel image = null;
        try {
            BufferedImage readImage = ImageIO.read(new File(path));
            int width = readImage.getWidth();
            int height = readImage.getHeight();
            int x = Math.min(width, TUTORIAL_CONTENT_WIDTH);
            int y = x==width?height: (height*x)/width;
            image = new JLabel(new ImageIcon(readImage.getScaledInstance(x, y, Image.SCALE_SMOOTH)));
        } catch (IOException ignored) {
        }

        if(image!=null)
        {
            panel.add(image);
            return (int) image.getPreferredSize().getHeight();
        }

        return 0;
    }
}
