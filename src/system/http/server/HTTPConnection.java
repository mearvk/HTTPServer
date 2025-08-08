package system.http.server;

import system.http.Bndi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Max Rupplin
 */
public class HTTPConnection
{
    public Integer hash = 0x008808ef;
    
    public Bndi bodi;
    
    public HTTPServer server;
    
    public Boolean islive = true;
            
    public NetworkContext connection;
    
    public Integer sessionid;
    
    public Long ttl = 60000*5L;
    
    public Long day;       
    
    public Serializable object;
    
    public String result;
    
    public String operation = ""; 
    
    public String cause;
    
    public String context;
    
    public String key;    
    
    public String message;
    
    public String value;   
    
    public ArrayList<String> values;
    
   
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
     * 
     * @param connectionContext
     * @return 
     */
    public Boolean processRequest(HTTPServerContext connectionContext)
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
        
        return this.object == null;
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
     * 
     * @param connectioncontext
     * @return 
     */
    public Boolean processGETResponse(HTTPServerContext connectioncontext)
    {
        this.islive = false;       
        
        if(Bndi.hascontextat(connectioncontext.getcontext(connectioncontext)))
        {
            try
            {
                Bndi.removecontext(connectioncontext.getcontext(connectioncontext));
                
                connectioncontext.httpConnection.result = "success";
                
                connectioncontext.httpConnection.message = "context closed";
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
            }
        }
        else
        {
            connectioncontext.httpConnection.result = "failure";
        
            connectioncontext.httpConnection.cause = "no such open context";
        }        
        
        return false;
    }
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public Boolean processOtherResponse(HTTPServerContext connectioncontext)
    {
        connectioncontext.httpConnection.operation = "//other";
        
        connectioncontext.httpConnection.result = "rejection";
        
        connectioncontext.httpConnection.message = "unusual; please recheck";
        
        try
        {
            //no valid TTL 
            if(this.getTimeToLive()<=0)
            {
                connectioncontext.httpConnection.cause = "TTL expired";
            }                        
            //no valid session issue
            else if(this.server.isvalidsessionid(connectioncontext.httpConnection))
            {
                connectioncontext.httpConnection.cause = "Session ID not valid";
            }
            //unclear cause; tokens may be cause
            else 
            {
                connectioncontext.httpConnection.cause = "Unclear cause; check all tokens";
            }
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            return false;
        }
    }
    
    /**
     *
     * @param connectionContext
     * @return
     * @throws Exception
     */
    public HTTPConnection processGETRequest(HTTPServerContext connectionContext) throws Exception
    {               
        HTTPConnection httpConnection = connectionContext.httpConnection;
        
        httpConnection.operation = "GET";
        
        httpConnection.getTimeToLive();

        return httpConnection;
    }
    
    /**
     * 
     * @param connectionContext
     * @return
     * @throws Exception 
     */
    public HTTPConnection processOtherRequest(HTTPServerContext connectionContext) throws Exception
    {
        return connectionContext.httpConnection;
    }

    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public String stripForKey(HTTPServerContext connectioncontext)
    {
        return ProtocolStripper.stripforkey(connectioncontext);
    }
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public String stripforvalue(HTTPServerContext connectioncontext)
    {
        return ProtocolStripper.stripforvalue(connectioncontext);
    }
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public String stripforcontext(HTTPServerContext connectioncontext)
    {
        return ProtocolStripper.stripforcontext(connectioncontext);
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
     * @param connectioncontext
     * @return 
     */
    private Boolean checkConnection(HTTPServerContext connectioncontext)
    {
        if(connectioncontext.httpConnection ==null) return false;
        
        if(connectioncontext.httpConnection.ttl<=0) return false;
        
        if(this.server.isValidSession(connectioncontext.httpConnection)==null) return false;
        
        return true;
    }
       
    /**
     * 
     * @return 
     */
    protected Integer getSessionID()
    {
        if(this.sessionid==null || this.sessionid==0)
        {
            return this.hashCode();
        }
        
        return this.sessionid;
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
