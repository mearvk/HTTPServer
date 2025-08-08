package system.http.server;

/**
 * Represents a single connection to an HTTP server
 * 
 * @author Max Rupplin
 */
public class HTTPServerContext
{   
    public HTTPConnection httpConnection;
    
    public HTTPServer httpServer;
    
    public NetworkContext networkContext;
    
    public String protocol;
    
    public String packet;
    
    public StringBuffer inputBuffer;
    
    public String inputString;
    
    /**
     * 
     * @param httpServer
     * @param protocol
     * @param bodiservercontext
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer httpServer, String protocol, HTTPServerContext bodiservercontext) throws Exception
    {
        if(httpServer ==null || protocol==null || bodiservercontext==null) throw new SecurityException("//bodi/connect");
        
        this.httpServer = httpServer;
        
        this.protocol = protocol;
        
        this.inputBuffer = new StringBuffer(bodiservercontext.networkContext.inqueue);
        
        this.inputString = String.valueOf(bodiservercontext.networkContext.inqueue);
        
        this.networkContext = bodiservercontext.networkContext;
        
        this.httpConnection = bodiservercontext.httpConnection;
        
        this.packet = String.valueOf(bodiservercontext.networkContext.inqueue);
    }    
    
    /**
     * Quick fill for primary loop on an HTTP server; not a total Constructor for BRS at this time. /mr /ok /ss
     * 
     * @param httpServer
     * @param networkContext
     * @param httpConnection
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer httpServer, NetworkContext networkContext, HTTPConnection httpConnection) throws Exception
    {
        if(httpServer ==null || networkContext ==null) throw new SecurityException("//bodi/connect");
        
        this.httpServer = httpServer;
        
        this.networkContext = networkContext;
        
        this.httpConnection = httpConnection;
        
        this.packet = networkContext.inqueue.toString();
        
        this.inputString = networkContext.inqueue.toString();
    }
    
    /**
     * 
     * @param httpServer
     * @param protocol
     * @param input
     * @param network
     * @param httpConnection
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer httpServer, String protocol, String input, NetworkContext network, HTTPConnection httpConnection) throws Exception
    {
        if( httpServer ==null || protocol==null || input==null || network==null || httpConnection==null) throw new SecurityException("//bodi/connect");
            
        this.protocol = protocol;
        
        this.inputString = input;
        
        this.inputBuffer = network.inqueue;
        
        this.httpServer = httpServer;
        
        this.networkContext = network;
        
        this.httpConnection = httpConnection;
        
        this.httpConnection.operation = protocol;
        
        this.packet = String.valueOf(network.inqueue);
        
        //
        
        if( protocol.startsWith("//other") )
        {                       
            this.httpConnection.cause = "unrecognized protocol";
                    
            this.httpConnection.message = "unable to complete request";
        }
    }
    
    /**
     * 
     * @param parameterization
     * @return 
     */
    public String getcontext(HTTPServerContext parameterization)
    {
        return ProtocolStripper.stripforcontext(parameterization);
    }
    
    /**
     * 
     * @param connectioncontext
     * @throws Exception 
     */
    public void processprotocol(HTTPServerContext connectioncontext) throws Exception
    {
        connectioncontext.httpServer.protocolhandler.parseprotocol(connectioncontext);
    }

    /**
     * 
     * @param connectioncontext
     * @throws Exception 
     */
    public void processrequest(HTTPServerContext connectioncontext) throws Exception
    {
        connectioncontext.httpConnection.processRequest(connectioncontext);
    }
    
    /**
     * 
     * @param connectioncontext
     * @throws Exception 
     */
    public void processsesponse(HTTPServerContext connectioncontext) throws Exception
    {
        connectioncontext.networkContext.processresponse(connectioncontext);
    }
}
