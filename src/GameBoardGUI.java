import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static javax.swing.ScrollPaneConstants.*;

/**
 * The GUI for the Reversi game board. This class handles the board and its respective buttons.
 *
 * @author Andrei Constantin
 * @version 23-04-2021
 */
public class GameBoardGUI {
    private Reversi game;
    private JPanel parentPanel;
    private JPanel boardPanel;
    private GameBoard gameBoard;
    private PlayerStatusPanel playerStatus;
    private boolean isExample;

    /**
     * Constructor for objects of class GameBoardGUI. It creates a board of size 8x8.
     *
     * @param boardPlayerOne The first player
     * @param boardPlayerTwo The second player
     */
    public GameBoardGUI(BoardPlayer boardPlayerOne, BoardPlayer boardPlayerTwo, Reversi game) {
        this(boardPlayerOne, boardPlayerTwo, 8, game);
    }

    /**
     * Constructor for the GameBoardGUI class. It creates a new GameBoardGUI from a given GameBoard object.
     * @param gameBoard The GameBoard object
     * @param game The Reversi object
     */
    public GameBoardGUI(GameBoard gameBoard, Reversi game)
    {
        int size = gameBoard.getSize();
        if(game==null)
            throw new IllegalArgumentException("The game cannot be null");
        if(gameBoard.getPlayer1()==null)
            throw new IllegalArgumentException("Player One cannot be null.");
        if(gameBoard.getPlayer2()==null)
            throw new IllegalArgumentException("Player Two cannot be null.");

        this.game = game;
        this.gameBoard = gameBoard;
        playerStatus = game.getPlayerStatus();
        if(playerStatus==null)
            throw new IllegalArgumentException("The player status panel cannot be null.");

        isExample=false;

        generateGameBoardPanel(this.gameBoard, size);
    }

    /**
     * Constructor for objects of class GameBoardGUI. It creates a board of a given size.
     *
     * @param size The size of the game board (size x size)
     * @param player1 The first player
     * @param player2 The second player
     */
    public GameBoardGUI(BoardPlayer player1, BoardPlayer player2, int size, Reversi game) {
        if(game==null)
            throw new IllegalArgumentException("The game cannot be null");
        if (player1 == null)
            throw new IllegalArgumentException("Player One cannot be null");
        if (player2 == null)
            throw new IllegalArgumentException("Player Two cannot be null");

        isExample = player1.getName().equals("") || player2.getName().equals("");
        this.game = game;
        playerStatus = game.getPlayerStatus();
        if (playerStatus == null)
            throw new IllegalArgumentException("The player status panel cannot be null.");
        if (!isExample)
            gameBoard = new GameBoard(player1, player2, size);
        else
            gameBoard = null;

        generateGameBoardPanel(gameBoard, size);
    }

    /**
     * Generate the game board panel.
     * @param gameBoard The game board
     * @param size The size of the board
     */
    private void generateGameBoardPanel(GameBoard gameBoard, int size)
    {
        boardPanel = new JPanel(new GridLayout(size, size));
        boardPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
        /*GridLayout layout = (GridLayout) boardPanel.getLayout();
        layout.setHgap(0);
        layout.setVgap(0);*/

        Theme theme = game.getCurrentTheme();

        for (int i = 0; i < size * size; i++) {
            BoardButton button = new BoardButton(i, theme);
            if (!isExample)
            {
                BoardPlayer player = gameBoard.getCell(i).getPlayer();
                button.setPlayer(player==gameBoard.getPlayer1()?BoardButton.PLAYER1:(player==gameBoard.getPlayer2()?BoardButton.PLAYER2:BoardButton.NOPLAYER));
                button.addMouseListener(new MouseAdapter() {
                    /**
                     * Handles the click on the player.
                     *
                     * @param e The mouse event
                     */
                    @Override
                    public void mousePressed(MouseEvent e) {
                        ArrayList<Integer> changed = gameBoard.onPositionPlayed(button.getPosition());
                        if (changed != null && changed.size() > 0) {
                            for (int position : changed) {
                                try {
                                    BoardButton current = (BoardButton) boardPanel.getComponent(position);
                                    BoardPlayer player = gameBoard.getCell(position).getPlayer();
                                    current.setPlayer(player==gameBoard.getPlayer1()?BoardButton.PLAYER1:BoardButton.PLAYER2);
                                } catch (Exception ex) {
                                    System.out.println("Button at position " + position + " could not be cast");
                                }
                            }
                            game.setStatusPlayer(gameBoard.getCurrentPlayer());
                        } else
                            game.setStatusIllegalMove(gameBoard.getCurrentPlayer());

                        clearHighlights();
                        setHighlights();
                        playerStatus.changePlayerOnePieces(gameBoard.getPlayerOnePieces());
                        playerStatus.changePlayerTwoPieces(gameBoard.getPlayerTwoPieces());
                        game.turnPlayed();
                    }
                });
            }

            boardPanel.add(button);
        }

        if (isExample) {
            ((BoardButton) (boardPanel.getComponent(GameBoard.getListPosition(size / 2, size / 2, size)))).setPlayer(BoardButton.PLAYER1);
            ((BoardButton) (boardPanel.getComponent(GameBoard.getListPosition(size / 2 - 1, size / 2 - 1, size)))).setPlayer(BoardButton.PLAYER1);
            ((BoardButton) (boardPanel.getComponent(GameBoard.getListPosition(size / 2 - 1, size / 2, size)))).setPlayer(BoardButton.PLAYER2);
            ((BoardButton) (boardPanel.getComponent(GameBoard.getListPosition(size / 2, size / 2 - 1, size)))).setPlayer(BoardButton.PLAYER2);
        }

        if (!isExample) {
            game.setStatusPlayer(gameBoard.getCurrentPlayer());
            setHighlights();
        }

        if(playerStatus.getPlayer1()==null || playerStatus.getPlayer1().getPieces()==0) {
            playerStatus.changePlayerOnePieces(2);
            playerStatus.changePlayerTwoPieces(2);
        }

        parentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        if (isExample)
            parentPanel.setForeground(new Color(0.5f, 0.5f, 0.5f, 0.5f));

        parentPanel.add(boardPanel);
    }

    /**
     * Highlight all of the board buttons that the current player can press.
     */
    private void setHighlights() {
        ArrayList<Integer> clickable = gameBoard.getPossiblePositions();
        if (clickable != null && clickable.size() > 0) {
            for (int position : clickable) {
                try {
                    BoardButton current = (BoardButton) boardPanel.getComponent(position);
                    current.highlight();
                } catch (Exception ex) {
                    System.out.println("Button at position " + position + " could not be cast");
                }
            }
        } else {
            BoardPlayer victory = gameBoard.checkVictory();
            if (victory == null)
                noMoreMoves();
            else
                declareWinner(victory);
        }
    }

    /**
     * Skips the turn of the current player
     */
    public void skipTurn() {
        gameBoard.skipTurn();
        game.setStatusPlayer(gameBoard.getCurrentPlayer());
        clearHighlights();
        setHighlights();
    }

    /**
     * No more moves available
     */
    private void noMoreMoves()
    {
        game.noMoreMoves(gameBoard.getCurrentPlayer());
        skipTurn();
    }

    /**
     * Declare the winner.
     * @param winningPlayer The player who has won
     */
    private void declareWinner(BoardPlayer winningPlayer)
    {
        for(Component c: boardPanel.getComponents())
            c.removeMouseListener(c.getMouseListeners()[0]);

        game.setWin(winningPlayer);
    }

    /**
     * Clears the highlights of all board pieces.
     */
    private void clearHighlights() {
        for (Component component : boardPanel.getComponents()) {
            try {
                BoardButton current = (BoardButton) component;
                current.unhighlight();
            } catch (Exception ex) {
                System.out.println("Button could not be cast");
            }
        }
    }

    /**
     * Returns the board's panel.
     *
     * @return The board panel
     */
    public JPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Returns the board's parent panel. This panel contains the board panel.
     *
     * @return The parent panel
     */
    public JPanel getParentPanel() {
        return parentPanel;
    }

    /**
     * Get player 1.
     * @return Player 1
     */
    public BoardPlayer getPlayer1()
    {
        return gameBoard.getPlayer1();
    }

    /**
     * Get player 2.
     * @return Player 2
     */
    public BoardPlayer getPlayer2()
    {
        return gameBoard.getPlayer2();
    }

    /**
     * Get the game board.
     * @return The game board
     */
    public GameBoard getBoard()
    {
        return gameBoard;
    }

    public void changeTheme()
    {
        Theme theme = game.getCurrentTheme();
        int size = 8;
        if(gameBoard!=null)
            size = gameBoard.getSize();

        for (int i = 0; i < size*size; i++)
        {
            BoardButton button = (BoardButton) boardPanel.getComponent(i);
            button.changeTheme(theme);
        }
    }
}
