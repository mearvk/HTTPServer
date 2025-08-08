package system.http.server;

/**
 *
 * @author Max Rupplin
 */
public class HTTPProtocolHandler
{ 
    public Integer hash = 0x008808ef;
    
    /**
     * @param connectioncontext
     * @throws Exception
     */
    protected void parseProtocol(HTTPServerContext connectioncontext ) throws Exception
    {                        
        if(connectioncontext==null) throw new SecurityException("//bodi/connect");
        
        if(connectioncontext.networkContext !=null)
        {                                    
            switch(connectioncontext.protocol)
            {     
                //
                case HTTPProtocol.GET:
                    
                    this.processGETprotocol(connectioncontext);
                    
                    return;
                   
                //
                default:
                    
                    this.processunknownprotocol(connectioncontext);                                         
                    
                    return;
            }                        
        }  
        
        throw new InvalidConnectionException("No valid connection found.");
    }     

    /**
     * GET protocol tokens pre-processing goes here
     * 
     * @param connectionContext
     * @throws SecurityException 
     */
    private void processGETprotocol(HTTPServerContext connectionContext) throws SecurityException
    {        
        if(!this.subtokenswellformed(connectionContext)) throw new SecurityException("//bodi/connect");
        
        if(!this.startsswith(connectionContext, "GET")) throw new SecurityException("//bodi/connect");

        if(!this.containscontext(connectionContext.inputBuffer)) throw new SecurityException("//bodi/connect");
        
        /*---------------------------------------------------------------------*/                
    }    

    /**
     * HANDSHAKE token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processhandshakeprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");
        
        if(!this.startsswith(connectioncontext, "//handshake")) throw new SecurityException("//bodi/connect");              
        
        /*---------------------------------------------------------------------*/        
    }
    
    /**
     * LIST token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processlistprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//list")) throw new SecurityException("//bodi/connect");        
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
        
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");
        
        /*---------------------------------------------------------------------*/                
    }    
    
    /**
     * OPEN token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processopenprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//open")) throw new SecurityException("//bodi/connect");
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
            
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");
        
        /*---------------------------------------------------------------------*/       
    }
    
    /**
     * PULL token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processpullprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//pull")) throw new SecurityException("//bodi/connect");
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
            
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");
            
        if(!this.containskey(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //key token missing; stopping.");
        
        /*---------------------------------------------------------------------*/       
    }
    
    /**
     * PUT token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processputprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {        
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//put")) throw new SecurityException("//bodi/connect");
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
            
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");
            
        if( !this.containskey(connectioncontext.inputBuffer) && this.containsvalue(connectioncontext.inputBuffer) )
        {
            throw new SecurityException("Bodi //key token missing; stopping.");
        }
        
        if( this.containskey(connectioncontext.inputBuffer) && !this.containsvalue(connectioncontext.inputBuffer) )
        {
            throw new SecurityException("Bodi //value token missing; stopping.");
        }        
        
        /*---------------------------------------------------------------------*/        
    }

    /**
     * TOUCH token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processtouchprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//touch")) throw new SecurityException("//bodi/connect");        
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
            
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");
        
        /*---------------------------------------------------------------------*/
    }
    
    /**
     * TRADE token pre-processing goes here
     * 
     * @param connectioncontext
     * @throws SecurityException 
     */
    private void processtradeprotocol(HTTPServerContext connectioncontext) throws SecurityException
    {
        if(!this.subtokenswellformed(connectioncontext)) throw new SecurityException("//bodi/connect");    
        
        if(!this.startsswith(connectioncontext, "//trade")) throw new SecurityException("//bodi/connect");        
        
        if(!this.containssessionsid(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //sessionid token missing; stopping.");
            
        if(!this.containscontext(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //context token missing; stopping.");

        if(!this.containskey(connectioncontext.inputBuffer)) throw new SecurityException("Bodi //key token missing; stopping.");
        
        /*---------------------------------------------------------------------*/
    }
    
    /**
     * UNKNOWN token pre-processing goes here
     * 
     * @param connectioncontext
     * @return
     * @throws Exception
     * @throws SecurityException 
     */
    protected Boolean processunknownprotocol(HTTPServerContext connectioncontext) throws Exception, SecurityException
    {                       
        String protocol = connectioncontext.protocol;
        
        StringBuffer buffer = connectioncontext.inputBuffer;
                
        switch(buffer.toString())
        {
            //not yet
        }        
        
        return true;
    }          
    
    /**
     * 
     * @param connectioncontext
     * @param comparator
     * @return 
     */
    public Boolean startsswith(HTTPServerContext connectioncontext, String comparator)
    {
        String input = connectioncontext.inputString;
        
        return input.startsWith(comparator);
    }
    
    /**
     * 
     * @param bodiserverconnectioncontext
     * @return 
     */
    public String stripforkey(HTTPServerContext bodiserverconnectioncontext)
    {
        return ProtocolStripper.stripforkey(bodiserverconnectioncontext);
    }
    
    /**
     * 
     * @param bodiserverconnectioncontext
     * @return 
     */
    public String stripforvalue(HTTPServerContext bodiserverconnectioncontext)
    {
        return ProtocolStripper.stripforvalue(bodiserverconnectioncontext);
    }
    
    /**
     * 
     * @param bodiserverconnectioncontext
     * @return 
     */
    public String stripforcontext(HTTPServerContext bodiserverconnectioncontext)
    {
        return ProtocolStripper.stripforcontext(bodiserverconnectioncontext);
    }   
    
    /**
     * 
     * @param bodiserverconnectioncontext
     * @return 
     */
    public String stripforprotocoltoken(HTTPServerContext bodiserverconnectioncontext)
    {
        return ProtocolStripper.stripforprotocoltoken(bodiserverconnectioncontext);
    }
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    protected Boolean subtokenswellformed(HTTPServerContext connectioncontext)
    {
        String[] tokens = connectioncontext.inputString.split(" ");
        
        for(String token : tokens)
        {
            if(!token.trim().startsWith("//")) return false;
        }        
        
        return true;
    }
    
    /**
     * 
     * @param buffer
     * @return 
     */
    protected Boolean containssessionsid(StringBuffer buffer)
    {
        String[] tokens = buffer.toString().split(" ");
        
        Boolean containssessionid = false;
            
        for(String token : tokens)
        {
            if(token.trim().startsWith("//sessionid=")) containssessionid = true;
        }     
        
        return containssessionid;
    }    
    
    /**
     * 
     * @param buffer
     * @return 
     */
    protected Boolean containscontext(StringBuffer buffer)
    {
        String[] tokens = buffer.toString().split(" ");
        
        Boolean containscontext = false;
            
        for(String token : tokens)
        {
            if(token.trim().startsWith("//context=")) containscontext = true;
        }     
        
        return containscontext;        
    }
    
    /**
     * 
     * @param buffer
     * @return 
     */
    protected Boolean containskey(StringBuffer buffer)
    {
        String[] tokens = buffer.toString().split(" ");
        
        Boolean containskey = false;
            
        for(String token : tokens)
        {
            if(token.trim().startsWith("//key=")) containskey = true;
        }     
        
        return containskey;              
    }
    
    /**
     * 
     * @param buffer
     * @return 
     */
    protected Boolean containsvalue(StringBuffer buffer)
    {
        String[] tokens = buffer.toString().split(" ");
        
        Boolean containskey = false;
            
        for(String token : tokens)
        {
            if(token.trim().startsWith("//value=")) containskey = true;
        }     
        
        return containskey;        
    }    
}



