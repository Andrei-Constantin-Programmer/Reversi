/**
 * An exception thrown when the board size is not legal.
 *
 * @author Andrei Constantin
 * @version 31-03-2021
 */
public class IllegalSizeException extends IllegalArgumentException {

    private String message;

    /**
     * Constructor for an Illegal Size Exception.
     * @param message The message
     */
    public IllegalSizeException(String message)
    {
        super(message);
    }

    @Override
    public String toString()
    {
        return message;
    }
}
