import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * A button on the board. It contains a colored circle.
 *
 * @author Andrei Constantin
 * @version 31-03-2021
 */
public class BoardButton extends JPanel
{
    public static final int NOPLAYER=0, PLAYER1=1, PLAYER2=2;

    private static final int PREFERRED_SIZE = 80;
    public static final Color NORMAL = Color.gray;
    public static final Color HIGHLIGHT = new Color(144, 144, 144);
    public static final Color HIGHLIGHT_CIRCLE = new Color(120, 120, 120);
    private int position;
    private int player;
    private Theme theme;

    /**
     * Constructor for the board button.
     * @param position The position in the gridlist
     * @param theme The theme
     */
    public BoardButton(int position, Theme theme)
    {
        if(position<0)
            throw new ListPositionNegativeException("BoardButton", this.getClass().getName());
        if(theme==null)
            throw new IllegalArgumentException("The theme cannot be null.");
        this.position = position;
        this.theme = theme;
        this.setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        this.setForeground(NORMAL);
        this.setBackground(NORMAL);
        this.generateBorders();
    }

    /**
     * Highlight the board button.
     */
    public void highlight()
    {
        this.setBackground(HIGHLIGHT);
        this.setForeground(HIGHLIGHT_CIRCLE);
    }

    /**
     * Unhighlight the board button
     */
    public void unhighlight()
    {
        this.setBackground(NORMAL);
        if(this.getForeground()==HIGHLIGHT_CIRCLE)
            this.setForeground(NORMAL);
    }

    /**
     * Change the player owner of this button.
     * @param player The new player
     */
    public void setPlayer(int player)
    {
        if(player==NOPLAYER)
        {
            unhighlight();
            this.player = NOPLAYER;
            this.setForeground(NORMAL);
        }
        else if(player==PLAYER1) {
            unhighlight();
            this.player = PLAYER1;
            this.setForeground(theme.getColor1());
        }
        else if(player==PLAYER2)
        {
            unhighlight();
            this.player = PLAYER2;
            this.setForeground(theme.getColor2());
        }
    }

    /**
     * Generates some dark gray borders for the button
     */
    private void generateBorders()
    {
        this.setBorder(new LineBorder(Color.DARK_GRAY));
    }


    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension size = this.getSize();
        int d = Math.min(size.width, size.height) - 10;
        int x = (size.width - d) / 2;
        int y = (size.height - d) / 2;
        g.fillOval(x, y, d, d);
        g.setColor(NORMAL);
        g.drawOval(x, y, d, d);
    }

    /**
     * Get the position of the button in the grid.
     * @return The position of the button
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Change the theme of the button.
     * @param theme The new theme
     */
    public void changeTheme(Theme theme)
    {
        this.theme = theme;
        setForeground(player==PLAYER1?theme.getColor1():(player==PLAYER2?theme.getColor2():NORMAL));
    }
}
