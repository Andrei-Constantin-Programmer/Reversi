import java.awt.*;
import java.io.Serializable;

/**
 * A player in the session.
 *
 * @author Andrei Constantin
 * @version 08-04-2021
 */
public class SessionPlayer implements Serializable
{
    private String name;
    //private String colorName;
    //private Color color;
    private int wins;
    private int pieces;

    /**
     * Constructor for creating a new player
     * @param playerName The player's name
     */
    public SessionPlayer(String playerName) {
        wins=0;
        pieces=0;
        if(playerName==null || playerName.isBlank())
            throw new IllegalArgumentException("The name cannot be null or empty.");
        name=playerName.trim();
        //this.colorName=colorName.trim();
        //color=playerColor;
    }

    /**
     * Returns the player's name.
     * @return The player's name
     */
    public String getName()
    {
        return name;
    }

    /*
     * Returns the player's color.
     * @return The player's color
     */
    /*public Color getColor()
    {
        return color;
    }*/

    /*
     * Returns the player's color name.
     * @return The player's color name
     */
    /*public String getColorName()
    {
        return colorName;
    }*/

    /**
     * Increment the number of wins by 1.
     */
    public void incrementWins()
    {
        wins++;
    }

    /**
     * Get the number of wins.
     * @return The number of wins
     */
    public int getWins()
    {
        return wins;
    }

    /**
     * Change the number of pieces.
     */
    public void changeNumberOfPieces(int amount)
    {
        pieces = amount;
    }

    /**
     * Get the number of pieces.
     * @return The number of pieces
     */
    public int getPieces()
    {
        return pieces;
    }

    /**]
     * Change the number of wins.
     * @param newScore The new number of wins
     */
    public void changeScore(int newScore)
    {
        wins=newScore;
    }
}
