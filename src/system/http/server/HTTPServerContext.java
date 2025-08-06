package system.http.server;

/**
 * Represents a single connection to a Bodi server
 * 
 * @author Max Rupplin
 */
public class HTTPServerContext
{   
    public HTTPConnection httpcontext;
    
    public HTTPServer bodiserver;
    
    public NetworkContext networkcontext;
    
    public String protocol;
    
    public String packet;
    
    public StringBuffer inputbuffer; 
    
    public String inputstring;
    
    public String key;
    
    public String value;
    
    public String context;    
    
    /**
     * 
     */
    public HTTPServerContext()
    {
        
    }
    
    /**
     * 
     * @param bodiserver
     * @param protocol
     * @param bodiservercontext
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer bodiserver, String protocol, HTTPServerContext bodiservercontext) throws Exception
    {
        if(bodiserver==null || protocol==null || bodiservercontext==null) throw new SecurityException("//bodi/connect");                 
        
        this.bodiserver = bodiserver;
        
        this.protocol = new StringBuffer(protocol).toString();
        
        this.inputbuffer = new StringBuffer(bodiservercontext.networkcontext.inqueue);               
        
        this.inputstring = new StringBuffer(bodiservercontext.networkcontext.inqueue).toString();  
        
        this.networkcontext = bodiservercontext.networkcontext;
        
        this.httpcontext = bodiservercontext.httpcontext;
        
        this.packet = new StringBuffer(bodiservercontext.networkcontext.inqueue).toString();        
    }    
    
    /**
     * Quick fill for primary loop on Bodiserver; not a total Constructor for BRS at this time. /mr /ok /ss
     * 
     * @param bodiserver
     * @param networkcontext
     * @param httpcontext
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer bodiserver, NetworkContext networkcontext, HTTPConnection httpcontext) throws Exception
    {                
        //bodicontext==null ok /mr /ok
        
        if(bodiserver==null || networkcontext==null /* || bodicontext==null */) throw new SecurityException("//bodi/connect");  
        
        this.bodiserver = bodiserver;                
        
        this.networkcontext = networkcontext;
        
        this.httpcontext = httpcontext;
        
        this.packet = networkcontext.inqueue.toString();
        
        this.inputstring = networkcontext.inqueue.toString();
        
        //             
    }
    
    /**
     * 
     * @param bodiserver
     * @param protocol
     * @param input
     * @param network
     * @param bodiconnection
     * @throws Exception 
     */
    public HTTPServerContext(HTTPServer bodiserver, String protocol, String input, NetworkContext network, HTTPConnection bodiconnection) throws Exception
    {
        if( bodiserver==null || protocol==null || input==null || network==null || bodiconnection==null) throw new SecurityException("//bodi/connect");
            
        this.protocol = protocol;
        
        this.inputstring = input;
        
        this.inputbuffer = network.inqueue;
        
        this.bodiserver = bodiserver;
        
        this.networkcontext = network;
        
        this.httpcontext = bodiconnection;
        
        this.httpcontext.operation = protocol;
        
        this.packet = new StringBuffer(network.inqueue).toString();
        
        //
        
        if( protocol.startsWith("//other") )
        {                       
            this.httpcontext.cause = "unrecognized protocol";
                    
            this.httpcontext.message = "unable to complete request";
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
        connectioncontext.bodiserver.protocolhandler.parseprotocol(connectioncontext);
    }

    /**
     * 
     * @param connectioncontext
     * @throws Exception 
     */
    public void processrequest(HTTPServerContext connectioncontext) throws Exception
    {
        connectioncontext.httpcontext.processRequest(connectioncontext);
    }
    
    /**
     * 
     * @param connectioncontext
     * @throws Exception 
     */
    public void processsesponse(HTTPServerContext connectioncontext) throws Exception
    {
        connectioncontext.networkcontext.processresponse(connectioncontext);
    }
}
