import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * A panel containing the information of a player.
 *
 * @author Andrei Constantin
 * @version 08-04-2021
 */
public class PlayerInfoPanel extends JPanel
{
    private JTextField nameField;
    private JLabel colorLabel;
    private JLabel piecesLabel;
    private JLabel scoreLabel;

    /**
     * Constructor for the player info panel, when the player is already known.
     * @param player The player
     * @param color The colour
     * @param colorName The name of the colour
     */
    public PlayerInfoPanel(SessionPlayer player, Color color, String colorName)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new LineBorder(Color.BLACK));

        createNameField(player.getName());
        add(nameField);

        createColorLabel(color, colorName);
        add(colorLabel);

        createPiecesLabel(player.getPieces());
        add(piecesLabel);

        createScoreLabel(player.getWins());
        add(scoreLabel);
    }

    /**
     * Constructor for the player info panel.
     * @param color The colour
     * @param colorName The name of the colour
     */
    public PlayerInfoPanel(Color color, String colorName)
    {
        super();
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new LineBorder(Color.BLACK));

        createNameField();
        add(nameField);

        createColorLabel(color, colorName);
        add(colorLabel);

        createPiecesLabel();
        add(piecesLabel);

        createScoreLabel();
        add(scoreLabel);
    }

    /**
     * Create the name field, when the player name is already known.
     */
    private void createNameField(String playerName)
    {
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.BOLD, 14));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setColumns(1);
        nameField.setToolTipText("Player name");
        nameField.setMaximumSize(new Dimension(nameField.getMaximumSize().width, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setText(playerName);
        nameField.setEnabled(false);
    }

    /**
     * Create the name field.
     */
    private void createNameField()
    {
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.BOLD, 14));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setColumns(1);
        nameField.setToolTipText("Player name");
        nameField.setMaximumSize(new Dimension(nameField.getMaximumSize().width, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Create the colour label.
     * @param color The colour
     * @param colorName The name of the colour
     */
    private void createColorLabel(Color color, String colorName)
    {
        colorLabel = new JLabel();
        colorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeTheme(color, colorName);
    }

    /**
     * Create the label that shows the number of pieces, with a given number of pieces.
     */
    private void createPiecesLabel(int pieces)
    {
        piecesLabel = new JLabel("Pieces: "+pieces);
        piecesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        piecesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Create the label that shows the number of wins, with a given number of wins.
     */
    private void createScoreLabel(int score)
    {
        scoreLabel = new JLabel("Wins: "+score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Create the label that shows the number of pieces.
     */
    private void createPiecesLabel()
    {
        piecesLabel = new JLabel("Pieces: "+0);
        piecesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        piecesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Create the label that shows the number of wins.
     */
    private void createScoreLabel()
    {
        scoreLabel = new JLabel("Wins: "+0);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Get the name in the name field.
     * @return The name in the name field
     */
    public String getName()
    {
        return nameField.getText().trim();
    }

    /**
     * Set the text of the name field to the current name, and make it unclickable.
     */
    public void setNameToCurrent()
    {
        nameField.setText(getName());
        nameField.setEnabled(false);
    }


    /**
     * Change the number of pieces displayed.
     * @param amount The new amount of pieces
     */
    public void changePieces(int amount)
    {
        piecesLabel.setText("Pieces: "+amount);
    }

    /**
     * Change the score displayed.
     * @param newScore The new score
     */
    public void changeScore(int newScore)
    {
        scoreLabel.setText("Wins: "+newScore);
    }

    /**
     * Change the theme of the current player info panel
     * @param playerColor The new color
     * @param playerColorName The name of the new color
     */
    public void changeTheme(Color playerColor, String playerColorName)
    {
        colorLabel.setText(toTitleCase(playerColorName));
        colorLabel.setForeground(playerColorName.toLowerCase().equals("white")?Color.gray:playerColor);
    }

    /**
     * Returns the given input String with Title Case
     * @param string The String to modify
     * @return The modified String
     */
    private static String toTitleCase(String string) {
        StringBuilder titleCase = new StringBuilder(string.length());
        boolean nextTitleCase = true;

        for (char c : string.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}