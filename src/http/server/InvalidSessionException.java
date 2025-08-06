package http.server;

/**
 *
 * @author Max Rupplin
 */
public class InvalidSessionException extends Exception
{
    public InvalidSessionException(String message)
    {
        super(message);
    }
}
