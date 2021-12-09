import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The player status panel. It contains information about the current session and the game in progress.
 *
 * @author Andrei Constantin
 * @version 08-04-2021
 */
public class PlayerStatusPanel extends JPanel
{
    private Reversi game;
    private SessionPlayer player1, player2;

    private PlayerInfoPanel playerOnePanel, playerTwoPanel;

    private JButton playButton;
    private JButton replayButton;

    /**
     * Constructor for a player status panel, with an already existing set of players.
     * @param playerOne Player 1
     * @param playerTwo Player 2
     * @param game The Reversi object
     */
    public PlayerStatusPanel(SessionPlayer playerOne, SessionPlayer playerTwo, Reversi game)
    {
        if(game==null)
            throw new IllegalArgumentException("The game cannot be null.");
        if(playerOne==null)
            throw new IllegalArgumentException("Player One cannot be null.");
        if(playerTwo==null)
            throw new IllegalArgumentException("Player Two cannot be null.");
        this.game = game;
        this.player1 = playerOne;
        this.player2 = playerTwo;
        playerOnePanel = new PlayerInfoPanel(playerOne, game.getCurrentTheme().getColor1(), game.getCurrentTheme().getName1());
        playerTwoPanel = new PlayerInfoPanel(playerTwo, game.getCurrentTheme().getColor2(), game.getCurrentTheme().getName2());

        createStatusPanel();

        changePlayerOnePieces(player1.getPieces());
        changePlayerTwoPieces(player2.getPieces());
        changePlayerOneScore(player1.getWins());
        changePlayerTwoScore(player2.getWins());
    }

    /**
     * Constructor for a player status panel.
     * @param game The Reversi object
     */
    public PlayerStatusPanel(Reversi game)
    {
        if(game==null)
            throw new IllegalArgumentException("The game cannot be null.");
        this.game=game;
        player1=player2=null;

        playerOnePanel = new PlayerInfoPanel(game.getCurrentTheme().getColor1(), game.getCurrentTheme().getName1());
        playerTwoPanel = new PlayerInfoPanel(game.getCurrentTheme().getColor2(), game.getCurrentTheme().getName2());

        createStatusPanel();
    }

    /**
     * Creates the status panel itself, with all of its components.
     */
    private void createStatusPanel()
    {
        setMaximumSize(new Dimension(200, 200));
        setPreferredSize(getMaximumSize());
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new EmptyBorder(1, 1, 1, 5));

        add(Box.createVerticalGlue());
        add(playerOnePanel);
        add(Box.createVerticalStrut(20));
        add(playerTwoPanel);
        add(Box.createVerticalStrut(20));
        playButton = new JButton("PLAY");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setPreferredSize(new Dimension(20, 50));
        playButton.addActionListener(new ActionListener() {
            /**
             * The on-click event for the PLAY button. It sets the names of the players.
             * @param e The Action Event
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String playerOneName = playerOnePanel.getName();
                    String playerTwoName = playerTwoPanel.getName();
                    if (playerOneName.isBlank())
                        Reversi.showMessage(game.getFrame(),"Please insert a name for Player 1");
                    else if (playerTwoName.isBlank())
                        Reversi.showMessage(game.getFrame(), "Please insert a name for Player 2");
                    else if(playerOneName.equals(playerTwoName))
                        Reversi.showMessage(game.getFrame(), "Please choose different names for the two players.");
                    else
                    {
                        playerOnePanel.setNameToCurrent();
                        playerTwoPanel.setNameToCurrent();
                        player1 = new SessionPlayer(playerOneName);
                        player2 = new SessionPlayer(playerTwoName);

                        game.createSession(player1, player2);

                        setPlayVisibility(false);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Reversi.showMessage(game.getFrame(),"The field could not be found.");
                }
            }
        });
        add(playButton);

        replayButton = new JButton("REPLAY");
        replayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayButton.setFont(new Font("Arial", Font.BOLD, 18));
        replayButton.setPreferredSize(new Dimension(20, 50));
        replayButton.addActionListener(new ActionListener() {
            /**
             * The on-click event for the REPLAY button. It creates a new board of the current board's size.
             * @param e The Action Event
             */
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changePlayerOnePieces(0);
                changePlayerTwoPieces(0);
                game.replay();
                setReplayVisibility(false);
            }
        });
        add(replayButton);
        setReplayVisibility(false);

        add(Box.createVerticalGlue());
    }

    /**
     * Change the amount of pieces Player 1 has.
     * @param amount The new amount of pieces
     */
    public void changePlayerOnePieces(int amount)
    {
        if(player1!=null)
            player1.changeNumberOfPieces(amount);
        playerOnePanel.changePieces(amount);
    }

    /**
     * Change the amount of pieces Player 2 has.
     * @param amount The new amount of pieces
     */
    public void changePlayerTwoPieces(int amount)
    {
        if(player2!=null)
            player2.changeNumberOfPieces(amount);
        playerTwoPanel.changePieces(amount);
    }

    /**
     * Increment the score of Player 1
     */
    public void incrementPlayerOneScore()
    {
        player1.incrementWins();
        playerOnePanel.changeScore(player1.getWins());
    }

    /**
     * Increment the score of Player 2
     */
    public void incrementPlayerTwoScore()
    {
        player2.incrementWins();
        playerTwoPanel.changeScore(player2.getWins());
    }

    /**
     * Change the score of Player 1
     * @param newScore The new score
     */
    private void changePlayerOneScore(int newScore)
    {
        player1.changeScore(newScore);
    }

    /**
     * Change the score of Player 2
     * @param newScore The new score
     */
    private void changePlayerTwoScore(int newScore)
    {
        player2.changeScore(newScore);
    }

    /**
     * Get Player 1
     * @return Player 1
     */
    public SessionPlayer getPlayer1() {
        return player1;
    }

    /**
     * Get Player 2
     * @return Player 2
     */
    public SessionPlayer getPlayer2() {
        return player2;
    }

    /**
     * Changes the visibility of the play button
     * @param visible true to make the button visible, false to make it invisible
     */
    public void setPlayVisibility(boolean visible)
    {
        playButton.setVisible(visible);
    }

    /**
     * Changes the visibility of the replay button.
     * @param visible true to make the button visible, false to make it invisible
     */
    public void setReplayVisibility(boolean visible)
    {
        replayButton.setVisible(visible);
    }

    /**
     * Change the theme of the player status panel.
     */
    public void changeTheme()
    {
        playerOnePanel.changeTheme(game.getCurrentTheme().getColor1(), game.getCurrentTheme().getName1());
        playerTwoPanel.changeTheme(game.getCurrentTheme().getColor2(), game.getCurrentTheme().getName2());
    }

}
