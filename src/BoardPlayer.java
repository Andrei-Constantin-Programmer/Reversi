import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * A player on the game board.
 *
 * @author Andrei Constantin
 * @version 23-03-2021
 */
public class BoardPlayer implements Serializable
{
    private String name;
    //private Color color;

    /**
     * Constructor for creating a new player
     * @param playerName The player's name
     */
    public BoardPlayer(String playerName) {
        name=playerName.trim();
//        color=playerColor;
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

}
