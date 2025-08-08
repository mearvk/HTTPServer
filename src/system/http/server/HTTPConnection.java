package system.http.server;

import system.http.Bndi;

/**
 *
 * @author Max Rupplin
 */
public class HTTPConnection
{
    public HTTPServer server;
    
    public Boolean islive = true;
            
    public NetworkContext connection;
    
    public Long ttl = 60000*5L;
    
    public Long day;
    
    public String result;
    
    public String operation = ""; 
    
    public String cause;
    
    public String message;
    
    public String value;
    
   
    /**
     * 
     */
    public HTTPConnection()
    {
        this.day = System.currentTimeMillis();
    }
    
    /**
     * 
     * @param server
     * @param connection 
     */
    public HTTPConnection(HTTPServer server, NetworkContext connection)
    {                
        if(server==null || connection==null) throw new SecurityException("//bodi/connect/exceptions");
        
        this.connection = connection;
        
        this.day = System.currentTimeMillis();
    }
         
    /**
     */
    public void processRequest(HTTPServerContext connectionContext)
    {                
        StringBuffer buffer = connectionContext.inputBuffer;
        
        String protocol = this.stripForProtocolToken(connectionContext);
        
        this.tryResetHTTPConnection(connectionContext);
        
        try
        {
            switch(protocol)
            {
                case "GET":

                    connectionContext.httpConnection.processGETRequest(connectionContext);

                    connectionContext.httpConnection.processGETResponse(connectionContext);

                    break;

                case "POST":

                    connectionContext.httpConnection.processGETRequest(connectionContext);

                    connectionContext.httpConnection.processGETResponse(connectionContext);

                    break;

                case "PUT":

                    connectionContext.httpConnection.processGETRequest(connectionContext);

                    connectionContext.httpConnection.processGETResponse(connectionContext);

                    break;

                case "PATCH":

                    connectionContext.httpConnection.processGETRequest(connectionContext);

                    connectionContext.httpConnection.processGETResponse(connectionContext);

                    break;

                case "DELETE":

                    connectionContext.httpConnection.processGETRequest(connectionContext);

                    connectionContext.httpConnection.processGETResponse(connectionContext);

                    break;

                default: 
                    
                    connectionContext.httpConnection.processOtherRequest(connectionContext);

                    connectionContext.httpConnection.processOtherResponse(connectionContext);

                    break;
            }        
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            //
        }

    }
    
    /**
     *      * 
     * @param httpServerContext
     * @return 
     */
    public Boolean tryResetHTTPConnection(HTTPServerContext httpServerContext)
    {        
        httpServerContext.httpConnection.cause = "";
        
        httpServerContext.httpConnection.message = "";
        
        httpServerContext.httpConnection.result = "";
        
        httpServerContext.httpConnection.cause = null;
        
        httpServerContext.httpConnection.message = null;
        
        httpServerContext.httpConnection.result = null;
        
        return false | true;
    }
    
    /**
     * @param connectionContext
     */
    public void processGETResponse(HTTPServerContext connectionContext)
    {
        this.islive = false;       
        
        if(Bndi.hascontextat(connectionContext.getcontext(connectionContext)))
        {
            try
            {
                Bndi.removecontext(connectionContext.getcontext(connectionContext));
                
                connectionContext.httpConnection.result = "success";
                
                connectionContext.httpConnection.message = "context closed";
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
            }
        }
        else
        {
            connectionContext.httpConnection.result = "failure";
        
            connectionContext.httpConnection.cause = "no such open context";
        }

    }
    
    /**
     * @param connectionContext
     */
    public void processOtherResponse(HTTPServerContext connectionContext)
    {
        connectionContext.httpConnection.operation = "OTHER";
        
        connectionContext.httpConnection.result = "REJECTION";
        
        connectionContext.httpConnection.message = "unusual; please recheck";
        
        try
        {
            //no valid TTL 
            if(this.getTimeToLive()<=0)
            {
                connectionContext.httpConnection.cause = "TTL expired";
            }
            else
            {
                connectionContext.httpConnection.cause = "Unclear cause; check all tokens";
            }
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            return;
        }
    }
    
    /**
     * @param connectionContext
     * @throws Exception
     */
    public void processGETRequest(HTTPServerContext connectionContext) throws Exception
    {               
        HTTPConnection httpConnection = connectionContext.httpConnection;
        
        httpConnection.operation = "GET";
        
        httpConnection.getTimeToLive();

    }
    
    /**
     * @param connectionContext
     * @throws Exception
     */
    public void processOtherRequest(HTTPServerContext connectionContext) throws Exception
    {
    }
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public String stripForProtocolToken(HTTPServerContext connectioncontext)
    {
        return ProtocolStripper.stripforprotocoltoken(connectioncontext);
    }

    /**
     * 
     * @return 
     */
    protected Long getTimeToLive()
    {
        Long now = System.currentTimeMillis();        
        
        Long day = this.day;
        
        Long ttl = this.ttl;
                
        //
        
        if( (this.ttl = ttl-(now-day))<0 )
        {
            return -1L;
        }      
        
        return this.ttl;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return "TBI";
    }        
}
