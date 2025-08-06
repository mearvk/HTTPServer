package http.server;

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
     * @param connectioncontext
     * @return 
     */
    public Boolean processRequest(HTTPServerContext connectioncontext)
    {                
        StringBuffer buffer = connectioncontext.inputbuffer;      
        
        String protocol = this.stripForProtocolToken(connectioncontext);
        
        this.tryResetHTTPConnection(connectioncontext);
        
        try
        {
            switch(protocol)
            {
                case "GET":

                    connectioncontext.httpcontext.processGETRequest(connectioncontext);

                    connectioncontext.httpcontext.processGETResponse(connectioncontext);

                    break;

                case "POST":

                    connectioncontext.httpcontext.processGETRequest(connectioncontext);

                    connectioncontext.httpcontext.processGETResponse(connectioncontext);

                    break;

                case "PUT":

                    connectioncontext.httpcontext.processGETRequest(connectioncontext);

                    connectioncontext.httpcontext.processGETResponse(connectioncontext);

                    break;

                case "PATCH":

                    connectioncontext.httpcontext.processGETRequest(connectioncontext);

                    connectioncontext.httpcontext.processGETResponse(connectioncontext);

                    break;

                case "DELETE":

                    connectioncontext.httpcontext.processGETRequest(connectioncontext);

                    connectioncontext.httpcontext.processGETResponse(connectioncontext);

                    break;

                default: 
                    
                    connectioncontext.httpcontext.processOtherRequest(connectioncontext);

                    connectioncontext.httpcontext.processOtherResponse(connectioncontext);

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
        httpServerContext.httpcontext.cause = "";
        
        httpServerContext.httpcontext.message = "";
        
        httpServerContext.httpcontext.result = "";
        
        httpServerContext.httpcontext.cause = null;
        
        httpServerContext.httpcontext.message = null;
        
        httpServerContext.httpcontext.result = null;
        
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
                
                connectioncontext.httpcontext.result = "success";
                
                connectioncontext.httpcontext.message = "context closed";
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
            }
        }
        else
        {
            connectioncontext.httpcontext.result = "failure";
        
            connectioncontext.httpcontext.cause = "no such open context";
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
        connectioncontext.httpcontext.operation = "//other";
        
        connectioncontext.httpcontext.result = "rejection";
        
        connectioncontext.httpcontext.message = "unusual; please recheck";
        
        try
        {
            //no valid TTL 
            if(this.getTimeToLive()<=0)
            {
                connectioncontext.httpcontext.cause = "TTL expired";
            }                        
            //no valid session issue
            else if(this.server.isvalidsessionid(connectioncontext.httpcontext))
            {
                connectioncontext.httpcontext.cause = "Session ID not valid";
            }
            //unclear cause; tokens may be cause
            else 
            {
                connectioncontext.httpcontext.cause = "Unclear cause; check all tokens";
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
     * @param connectioncontext
     * @return
     * @throws Exception 
     */
    public HTTPConnection processGETRequest(HTTPServerContext connectioncontext) throws Exception
    {               
        HTTPConnection httpConnection = connectioncontext.httpcontext;
        
        httpConnection.operation = "GET";
        
        httpConnection.getSessionID();
        
        httpConnection.getTimeToLive();
        
        return httpConnection;
    }
    
    /**
     * 
     * @param connectioncontext
     * @return
     * @throws Exception 
     */
    public HTTPConnection processOtherRequest(HTTPServerContext connectioncontext) throws Exception
    {
        return connectioncontext.httpcontext;
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
        if(connectioncontext.httpcontext ==null) return false;
        
        if(connectioncontext.httpcontext.ttl<=0) return false;
        
        if(this.server.isValidSession(connectioncontext.httpcontext)==null) return false;
        
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
