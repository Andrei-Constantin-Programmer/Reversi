/**
 * An exception thrown when the button position in the list is negative
 *
 * @author Andrei Constantin
 * @version 31-03-2021
 */
public class ListPositionNegativeException extends IllegalArgumentException
{
    public ListPositionNegativeException(String methodName, String className)
    {
        super("The position in the list must be positive in " + methodName + "(), in Class " + className);
    }
}
