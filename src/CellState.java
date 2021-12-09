import java.io.Serializable;

/**
 * Contains the state of an individual cell in the Reversi game board.
 *
 * @author Andrei Constantin
 * @version 31-03-2021
 */
public class CellState implements Serializable
{
    private int x, y;
    private int listPos;
    private BoardPlayer player;

    /**
     * Creates a new cell state
     * @param x The x position in the grid
     * @param y The y position in the grid
     * @param positionList The position of the cell in a list
     */
    public CellState(int x, int y, int positionList)
    {
        if(x<0)
            throw new IllegalArgumentException("The x position must be positive");
        if(y<0)
            throw new IllegalArgumentException("The y position must be positive");
        if(positionList<0)
            throw new ListPositionNegativeException("CellState", "CellState");
        this.x=x;
        this.y=y;
        this.listPos=positionList;
        player=null;
    }

    /**
     * Get the x position in the grid.
     * @return x
     */
    public int getXPosition()
    {
        return x;
    }

    /**
     * Get the y position in the grid.
     * @return y
     */
    public int getYPosition()
    {
        return y;
    }

    /**
     * Get the position in the list.
     * @return The position in the list
     */
    public int getListPosition()
    {
        return listPos;
    }

    /**
     * Get the corresponding player.
     * @return The player
     */
    public BoardPlayer getPlayer()
    {
        return player;
    }

    /**
     * Change the player of the cell state.
     * @param player The player
     */
    public void setPlayer(BoardPlayer player)
    {
        if(player==null)
            throw new IllegalArgumentException("The player cannot be null");
        this.player = player;
    }
}
