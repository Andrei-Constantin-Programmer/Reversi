import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Reversi game board. This class handles the game logic of the board itself.
 *
 * @author Andrei Constantin
 * @version 31-03-2021
 */
public class GameBoard implements Serializable {
    public transient static final int MIN_BOARD_SIZE = 4;
    public transient static final int MAX_BOARD_SIZE = 12;

    public transient static final BoardPlayer tiePlayer = new BoardPlayer("Tie");

    private transient static final int N = 0, NE = 1, E = 2, SE = 3, S = 4, SW = 5, W = 6, NW = 7;

    private int playerOnePieces, playerTwoPieces;
    private int size;
    private CellState[][] gameBoard;
    private BoardPlayer player1;
    private BoardPlayer player2;
    private BoardPlayer currentPlayer;

    /**
     * Create a game board of the respective size.
     *
     * @param player1 The first player
     * @param player2 The second player
     * @param size    The number of rows/columns of the board
     */
    public GameBoard(BoardPlayer player1, BoardPlayer player2, int size) {
        if (player1 == null)
            throw new IllegalArgumentException("Player 1 cannot be null");
        if (player2 == null)
            throw new IllegalArgumentException("Player 2 cannot be null");
        checkProperSize(size);

        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        this.size = size;
        gameBoard = new CellState[size][];

        for (int i = 0; i < size; i++) {
            gameBoard[i] = new CellState[size];
            for (int j = 0; j < size; j++)
                gameBoard[i][j] = new CellState(i, j, getListPosition(i, j, size));
        }

        getCell(size / 2, size / 2).setPlayer(player1);
        getCell(size / 2 - 1, size / 2 - 1).setPlayer(player1);
        getCell(size / 2 - 1, size / 2).setPlayer(player2);
        getCell(size / 2, size / 2 - 1).setPlayer(player2);

        setPlayerOnePieces(2);
        setPlayerTwoPieces(2);
    }

    /**
     * Change the state of the cell at the given position and any other cells that should be affected.
     *
     * @param listPosition The position of the cell in the list
     * @return An array list of all of the positions of the modified cells, or null if none are found
     */
    public ArrayList<Integer> onPositionPlayed(int listPosition) {
        if (listPosition < 0)
            throw new ListPositionNegativeException("onPositionPlayed", "GameBoard");
        CellState currentCell = getCell(listPosition);

        if (currentCell.getPlayer() != null)
            return null;

        if (!isPlaceable(currentCell))
            return null;

        ArrayList<Integer> toChange = new ArrayList<>();
        toChange.add(currentCell.getListPosition());
        currentCell.setPlayer(currentPlayer);

        for (int direction = N; direction <= NW; direction++) {
            ArrayList<Integer> changed = changeDirection(currentCell, direction);
            if (changed != null && changed.size() > 0)
                toChange.addAll(changed);
        }

        setPlayerOnePieces(calculatePlayerPieces(player1));
        setPlayerTwoPieces(calculatePlayerPieces(player2));

        changeCurrentPlayer();

        return toChange;
    }

    /**
     * Checks whether the currentCell can be modified by the current player.
     *
     * @param currentCell The cell to verify
     * @return true, if the cell is changeable, false otherwise
     */
    private boolean isPlaceable(CellState currentCell) {
        if (currentCell.getPlayer() != null)
            return false;

        for (int direction = N; direction <= NW; direction++)
            if (checkDirection(currentCell, direction))
                return true;

        return false;
    }

    /**
     * Get a list of all of the positions where the current player can place a piece.
     *
     * @return An array list of all possible positions
     */
    public ArrayList<Integer> getPossiblePositions() {
        ArrayList<Integer> clickable = new ArrayList<>();

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (isPlaceable(gameBoard[j][i]))
                    clickable.add(gameBoard[j][i].getListPosition());
            }

        return clickable;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces in the given direction, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param startingCell The starting cell from which to check
     * @param direction    The direction to check (N, NE, E, SE, S, SW, W, NW)
     * @return An array list of all the modified positions, or null if none are found
     */
    private ArrayList<Integer> changeDirection(CellState startingCell, int direction) {
        if (startingCell == null)
            return null;
        int x = startingCell.getXPosition(), y = startingCell.getYPosition();
        BoardPlayer secondPlayer = getOtherPlayer();
        switch (direction) {
            case N:
                return changeNorth(x, y, secondPlayer);
            case NE:
                return changeNorthEast(x, y, secondPlayer);
            case E:
                return changeEast(x, y, secondPlayer);
            case SE:
                return changeSouthEast(x, y, secondPlayer);
            case S:
                return changeSouth(x, y, secondPlayer);
            case SW:
                return changeSouthWest(x, y, secondPlayer);
            case W:
                return changeWest(x, y, secondPlayer);
            case NW:
                return changeNorthWest(x, y, secondPlayer);
            default:
                throw new IllegalArgumentException("The cardinal direction does not exist");
        }
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the north, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north, false otherwise
     */
    private ArrayList<Integer> changeNorth(int x, int y, BoardPlayer secondPlayer) {
        if (!checkNorth(x, y, secondPlayer))
            return null;
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y; i >= 0; i--) {
            CellState cell = getCell(x, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the north-east, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north-east, false otherwise
     */
    private ArrayList<Integer> changeNorthEast(int x, int y, BoardPlayer secondPlayer) {
        if (!checkNorthEast(x, y, secondPlayer))
            return null;
        x++;
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y, j = x; i >= 0 && j < size; i--, j++) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the east, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the east, false otherwise
     */
    private ArrayList<Integer> changeEast(int x, int y, BoardPlayer secondPlayer) {
        if (!checkEast(x, y, secondPlayer))
            return null;
        x++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int j = x; j < size; j++) {
            CellState cell = getCell(j, y);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the south-east, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south-east, false otherwise
     */
    private ArrayList<Integer> changeSouthEast(int x, int y, BoardPlayer secondPlayer) {
        if (!checkSouthEast(x, y, secondPlayer))
            return null;
        x++;
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y, j = x; i < size && j < size; i++, j++) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the south, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south, false otherwise
     */
    private ArrayList<Integer> changeSouth(int x, int y, BoardPlayer secondPlayer) {
        if (!checkSouth(x, y, secondPlayer))
            return null;
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y; i < size; i++) {
            CellState cell = getCell(x, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the south-west, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south-west, false otherwise
     */
    private ArrayList<Integer> changeSouthWest(int x, int y, BoardPlayer secondPlayer) {
        if (!checkSouthWest(x, y, secondPlayer))
            return null;
        x--;
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y, j = x; i < size && j >= 0; i++, j--) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the west, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the west, false otherwise
     */
    private ArrayList<Integer> changeWest(int x, int y, BoardPlayer secondPlayer) {
        if (!checkWest(x, y, secondPlayer))
            return null;
        x--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int j = x; j >= 0; j--) {
            CellState cell = getCell(j, y);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Changes all of the opponent's pieces to the current player's pieces to the north-west, if it is possible to do so. It will return an array list of the positions of all the changed pieces.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north-west, false otherwise
     */
    private ArrayList<Integer> changeNorthWest(int x, int y, BoardPlayer secondPlayer) {
        if (!checkNorthWest(x, y, secondPlayer))
            return null;
        x--;
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return null;

        ArrayList<Integer> changed = new ArrayList<>();
        for (int i = y, j = x; i >= 0 && j >= 0; i--, j--) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return changed;
            else {
                cell.setPlayer(currentPlayer);
                changed.add(cell.getListPosition());
            }
        }

        return changed;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces on the given direction. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param startingCell The starting cell from which to check
     * @param direction    The direction to check (N, NE, E, SE, S, SW, W, NW)
     * @return true, if the new piece changes any other pieces in the direction, false otherwise
     */
    private boolean checkDirection(CellState startingCell, int direction) {
        if (startingCell == null)
            return false;
        int x = startingCell.getXPosition(), y = startingCell.getYPosition();
        BoardPlayer secondPlayer = getOtherPlayer();
        switch (direction) {
            case N:
                return checkNorth(x, y, secondPlayer);
            case NE:
                return checkNorthEast(x, y, secondPlayer);
            case E:
                return checkEast(x, y, secondPlayer);
            case SE:
                return checkSouthEast(x, y, secondPlayer);
            case S:
                return checkSouth(x, y, secondPlayer);
            case SW:
                return checkSouthWest(x, y, secondPlayer);
            case W:
                return checkWest(x, y, secondPlayer);
            case NW:
                return checkNorthWest(x, y, secondPlayer);
            default:
                throw new IllegalArgumentException("The cardinal direction does not exist");
        }
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the north of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north, false otherwise
     */
    private boolean checkNorth(int x, int y, BoardPlayer secondPlayer) {
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y; i >= 0; i--) {
            CellState cell = getCell(x, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the north-east of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north-east, false otherwise
     */
    private boolean checkNorthEast(int x, int y, BoardPlayer secondPlayer) {
        x++;
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y, j = x; i >= 0 && j < size; i--, j++) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the east of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the east, false otherwise
     */
    private boolean checkEast(int x, int y, BoardPlayer secondPlayer) {
        x++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int j = x; j < size; j++) {
            CellState cell = getCell(j, y);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the south-east of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south-east, false otherwise
     */
    private boolean checkSouthEast(int x, int y, BoardPlayer secondPlayer) {
        x++;
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y, j = x; i < size && j < size; i++, j++) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the south of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south, false otherwise
     */
    private boolean checkSouth(int x, int y, BoardPlayer secondPlayer) {
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y; i < size; i++) {
            CellState cell = getCell(x, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the south-west of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the south-west, false otherwise
     */
    private boolean checkSouthWest(int x, int y, BoardPlayer secondPlayer) {
        x--;
        y++;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y, j = x; i < size && j >= 0; i++, j--) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the west of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the west, false otherwise
     */
    private boolean checkWest(int x, int y, BoardPlayer secondPlayer) {
        x--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int j = x; j >= 0; j--) {
            CellState cell = getCell(j, y);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Checks if placing a piece on the given starting cell will change any pieces to the north-west of the cell. It will return true if it will change at least one piece in that direction, or false otherwise.
     *
     * @param x            The column of the starting cell
     * @param y            The row of the starting cell
     * @param secondPlayer The second player
     * @return true, if the new piece changes any other pieces to the north-west, false otherwise
     */
    private boolean checkNorthWest(int x, int y, BoardPlayer secondPlayer) {
        x--;
        y--;
        if (getCell(x, y) == null || getCell(x, y).getPlayer() != secondPlayer)
            return false;
        for (int i = y, j = x; i >= 0 && j >= 0; i--, j--) {
            CellState cell = getCell(j, i);
            if (cell.getPlayer() == currentPlayer)
                return true;
            else if (cell.getPlayer() != secondPlayer)
                return false;
        }
        return false;
    }

    /**
     * Get the cell state at the give list position.
     *
     * @param listPosition The list position
     * @return The cell state, or null if does not exist
     */
    public CellState getCell(int listPosition) {
        if (listPosition < size * size && listPosition >= 0)
            return gameBoard[listPosition % size][listPosition / size];
        return null;
    }

    /**
     * Get the cell state at the give list position.
     *
     * @param x The column
     * @param y The row
     * @return The cell state, or null if does not exist
     */
    public CellState getCell(int x, int y) {
        int position = getListPosition(x, y, size);
        return getCell(position);
    }

    /**
     * Change the current player to the other player.
     */
    private void changeCurrentPlayer() {
        if (currentPlayer == player1)
            currentPlayer = player2;
        else
            currentPlayer = player1;
    }

    /**
     * Get the other player.
     *
     * @return The other player
     */
    private BoardPlayer getOtherPlayer() {
        if (currentPlayer == player1)
            return player2;
        else
            return player1;
    }

    /**
     * Get the position in a 0-indexed list of an element with the given x and y positions in a square grid with the number of rows equal to n.
     *
     * @param x    The x position in the grid
     * @param y    The y position in the grid
     * @param size The number of rows/columns
     * @return The position in the list
     */
    public static int getListPosition(int x, int y, int size) {
        return x + y * size;
    }

    /**
     * Get the size of the board.
     *
     * @return The size of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the current player.
     *
     * @return The current player
     */
    public BoardPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the number of pieces player 1 has.
     *
     * @return The number of player 1's pieces
     */
    public int getPlayerOnePieces() {
        return playerOnePieces;
    }

    /**
     * Get the number of pieces player 2 has.
     *
     * @return The number of player 2's pieces
     */
    public int getPlayerTwoPieces() {
        return playerTwoPieces;
    }

    /**
     * Chanage the amount of pieces player 1 has.
     *
     * @param pieces The new amount
     */
    private void setPlayerOnePieces(int pieces) {
        playerOnePieces = pieces;
    }

    /**
     * Chanage the amount of pieces player 2 has.
     *
     * @param pieces The new amount
     */
    private void setPlayerTwoPieces(int pieces) {
        playerTwoPieces = pieces;
    }

    /**
     * Get the number of pieces currently on the board for the given player.
     *
     * @param player The player
     * @return The number of pieces
     */
    private int calculatePlayerPieces(BoardPlayer player) {
        int count = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (gameBoard[j][i].getPlayer() == player)
                    count++;
        return count;
    }

    /**
     * Check if victory was achieved.
     * @return The player that won, or null if no player has won
     */
    public BoardPlayer checkVictory()
    {
        if(getPossiblePositions().size()==0)
        {
            changeCurrentPlayer();
            if(getPossiblePositions().size()==0)
                return getVictoriousPlayer();
            changeCurrentPlayer();
        }
        return null;
    }

    /**
     * Get the player with the most pieces on the board.
     * @return The player with the most pieces, or the tiePlayer, if both players have the same number of pieces
     */
    private BoardPlayer getVictoriousPlayer()
    {
        int piecesP1 = calculatePlayerPieces(player1);
        int piecesP2 = calculatePlayerPieces(player2);

        if(piecesP1==piecesP2)
            return tiePlayer;

        return piecesP1>piecesP2?player1:player2;
    }

    /**
     * Get player 1.
     * @return Player 1
     */
    public BoardPlayer getPlayer1()
    {
        return player1;
    }

    /**
     * Get player 2.
     * @return Player 2
     */
    public BoardPlayer getPlayer2()
    {
        return player2;
    }

    /**
     * Skip the turn of the current player.
     */
    public void skipTurn()
    {
        changeCurrentPlayer();
    }

    /**
     * Checks if the given size can be the size of a Reversi game board. If not, it throws an exception.
     * @param size The size of the
     *
     */
    private void checkProperSize(int size)
    {
        if(size<GameBoard.MIN_BOARD_SIZE || size>GameBoard.MAX_BOARD_SIZE)
            throw new IllegalSizeException("The size must be between " + GameBoard.MIN_BOARD_SIZE + " and " + GameBoard.MAX_BOARD_SIZE);
        if(size%2==1)
            throw new IllegalSizeException("The size must be an even number.");
    }

}

