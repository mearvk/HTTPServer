import system.http.server.HTTPServer;

public class Main
{
    public static void main(String...args)
    {
        HTTPServer server;

        server = new HTTPServer("localhost", 8080);

        server.start();

        server.attend();
    }
}