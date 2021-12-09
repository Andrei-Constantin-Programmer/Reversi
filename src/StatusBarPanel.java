import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * The status bar. It details the current state of the game.
 *
 * @author Andrei Constantin
 * @version 08-04-2021
 */
public class StatusBarPanel extends JPanel
{
    private JLabel statusField;
    private Reversi game;

    /**
     * Create a status bar panel
     * @param game The Reversi object to which this status bar belongs.
     * @param initialStatus The initial status to be displayed
     */
    public StatusBarPanel(Reversi game, String initialStatus)
    {
        super();
        this.game=game;
        this.setBorder(new EmptyBorder(7, 7, 7, 7));
        this.setBackground(Color.lightGray);
        createStatusField();
        this.add(statusField);

        /*skipButton = new JButton("Skip");
        skipButton.setFont(new Font("Arial", Font.BOLD, 18));
        skipButton.addActionListener(new ActionListener() {*/
        /*
         * Skip the turn.
         * @param e The action event
         */
            /*@Override
            public void actionPerformed(ActionEvent e)
            {
                game.skipTurn();
            }
        });*/
        //this.add(skipButton);
        //setSkipEnabled(false);
        setStatus(initialStatus);
    }

    /**
     * Get the status message.
     * @return The statuss
     */
    public String getStatus()
    {
        return statusField.getText();
    }

    /**
     * Change the status message.
     * @param newStatus The new status
     */
    public void setStatus(String newStatus)
    {
        statusField.setText(newStatus.trim());
    }

    private void createStatusField()
    {
        statusField = new JLabel();
        statusField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
    }

    /*
     * Set whether the skip button is enabled or not.
     * @param enabled true to enable, false to disable
     */
    /*public void setSkipEnabled(boolean enabled)
    {
        skipButton.setEnabled(enabled);
    }*/
}
