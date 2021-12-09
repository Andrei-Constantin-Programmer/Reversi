/**
 * Exception that appears when trying to load a session.
 *
 * @author Andrei Constantin
 * @version 16-04-2021
 */
public class LoadSessionException extends Exception
{
    String message;

    public LoadSessionException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }

}
