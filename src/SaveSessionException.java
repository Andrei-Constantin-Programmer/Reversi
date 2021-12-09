/**
 * Error that appears when trying to save the session.
 *
 * @author Andrei Constantin
 * @version 16-04-2021
 */
public class SaveSessionException extends Exception
{
    String message;

    public SaveSessionException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
