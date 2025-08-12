package system.http.server;

import system.http.Bndi;

import java.util.Collection;
import java.util.Objects;

import static system.http.server.HTTPProtocol.*;

public class HTTPServer extends BaseServer
{                       
    public Integer hash = 0x008808ef;
    
    public HTTPProtocolHandler protocolHandler = new HTTPProtocolHandler();
    
    public Boolean running = true;
    
    /**
     * 
     * @param host
     * @param port 
     */
    public HTTPServer(String host, Integer port)
    {      
        super(host, port);
        
        if(host==null || port==null) throw new SecurityException("//http/connect");
        
        this.host = host;
        
        this.port = port;        
        
        Bndi.setcontext("//http/server/remote/httpconnections");
        
        Bndi.setcontext("//http/server/remote/netconnections");
    }
    
    /**
     * 
     * @param port 
     */
    public HTTPServer(Integer port)
    {
        super("localhost", port);
        
        if(port==null) throw new SecurityException("//http/connect");
        
        this.port = port;        
        
        Bndi.setcontext("//http/server/remote/httpconnections");
        
        Bndi.setcontext("//http/server/remote/netconnections");
    }
    
    /**
     * Attend new connections and HTTP requests
     */
    public void attend() 
    {                                  
        while(running)
        {                                                
            HTTPServerContext httpServerContext = null;
            
            NetworkContext networkContext = this.pollQueuedNetworkConnections();
                        
            //
            
            try
            {                
                if( this.tryValidateNetworkConnection(networkContext) ) //
                {
                    httpServerContext = new HTTPServerContext(this, networkContext, new HTTPConnection());

                    if( this.tryValidateHTTPConnection(httpServerContext) )
                    {                                                                                              
                        
                        if(httpServerContext.inputString.startsWith(GET))
                        {
                            httpServerContext = new HTTPServerContext(this, GET, httpServerContext);

                            httpServerContext.processprotocol();

                            httpServerContext.processrequest();

                            httpServerContext.processsesponse();
                            
                            //

                           // this.trystorecontextstobodhi(httpServerContext, networkContext);
                        }
                        else
                        {
                            httpServerContext = new HTTPServerContext(this, OTHER, "", httpServerContext.networkContext, new HTTPConnection());

                            httpServerContext.processsesponse();
                        }                                                
                    }  
                    else
                    {
                        httpServerContext = new HTTPServerContext(this, OTHER, "", httpServerContext.networkContext, new HTTPConnection());

                        httpServerContext.processsesponse();
                    }                    
                }                                                   
            }
            catch(SecurityException exception) 
            {                                                
                exception.printStackTrace(System.err);
                
                //
            }            
            catch(NullPointerException exception)
            {
                exception.printStackTrace(System.err);
                
                //
            }            
            catch(InvalidSessionException exception)
            {
                exception.printStackTrace(System.err);
                
                //
            }
            catch(Exception exception)
            {
                exception.printStackTrace(System.err);
                
                //                                
            }              
            finally
            {                
                //
                
                this.tryflushnetworkcontext(networkContext);
                
                //
                
                this.tryFlushHTTPContext(httpServerContext);
                
                //
                
                this.sleepmillis(500L);
                
                //
                
                System.gc();
                
                //
            }
        }
    }      

    
    /**
     * That network context is non-null, has a value to be read in the InputQueue and is still connected to a client.
     * 
     * @param networkcontext The network context containing the network and the associated the input queue.
     * 
     * @return TRUE is underlying network context is not null, has some ready input and the network connection (socket) is not disconnected but connected and open; FALSE otherwise.
     */
    public Boolean tryValidateNetworkConnection(NetworkContext networkcontext)
    {
        return
                networkcontext!=null &&

                networkcontext.inputqueueisready() &&

                networkcontext.issocketconnected();
    }
    
    /**
     * We care that only valid Bodiconnections get introduced into the server and her routing procedure.
     * 
     * @param httpservercontext The server context containing the network packet and the bodicontext for inspection.
     * 
     * @return TRUE if Bodiconnection is valid by these requirements; FALSE otherwise.
     */
    public Boolean tryValidateHTTPConnection(HTTPServerContext httpservercontext)
    {
        return 
            
            httpservercontext!=null &&
                
            httpservercontext.httpConnection !=null &&
            
            httpservercontext.packet!=null &&

            httpservercontext.packet.startsWith("GET");
    }    
    
    /**
     * Clear me
     * 
     * @param httpservercontext
     */
    public void tryFlushHTTPContext(HTTPServerContext httpservercontext)
    {
        if(httpservercontext==null) return; //throw new SecurityException("//bodi/connect");
        
        httpservercontext.httpConnection.value = "";
        
        httpservercontext.httpConnection.result = "";
        
        httpservercontext.httpConnection.message = "";
        
        httpservercontext.httpConnection.value = null;
        
        httpservercontext.httpConnection.result = null;
        
        httpservercontext.httpConnection.message = null;
    }
    
    /**
     * Clear cyclical buffers 
     * 
     * @param networkcontext 
     */
    public void tryflushnetworkcontext(NetworkContext networkcontext)
    {                
        try
        { 
            networkcontext.inqueue.delete(0, networkcontext.inqueue.length());                         
        }
        catch(Exception e){} 
    }
    
    /**
     * Returns the next network connection from InputQueue from BasicServer class to Bodiremoteserver
     * 
     * @return That particular Networkcontext or null if none found queued
     */
    public NetworkContext pollQueuedNetworkConnections()
    {        
        NetworkContext connection = this.connectionqueue.peek();
        
        if(connection!=null) this.connectionqueue.remove(connection);
        
        return connection;
    }    
    
    /**
     * Talk to system; have it rest millis number of milliseconds before returning to task.
     * 
     * @param millis Rest this many milliseconds and no longer.
     */
    public void sleepmillis(Long millis) 
    {
        try
        {
            Thread.currentThread().sleep(millis);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}