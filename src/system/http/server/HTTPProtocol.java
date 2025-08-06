package system.http.server;

/**
 *
 * @author Max Rupplin
 */
public class HTTPProtocol extends Protocol
{        
    public static final String GET = "GET";

    public static final String OTHER = "OTHER";

    public HTTPProtocol()
    {
        super();
    }

    public static Boolean isHTTPProtocol(String comparator)
    {
        return comparator.startsWith(GET);
    }
}