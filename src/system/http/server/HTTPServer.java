package system.http.server;

import system.http.Bndi;

import java.util.Collection;
import java.util.Objects;

import static system.http.server.HTTPProtocol.*;

public class HTTPServer extends BaseServer
{                       
    public Integer hash = 0x008808ef;
    
    public HTTPProtocolHandler protocolhandler = new HTTPProtocolHandler();
    
    public Boolean running = true;
    
    /**
     * 
     * @param host
     * @param port 
     */
    public HTTPServer(String host, Integer port)
    {      
        super(host, port);
        
        if(host==null || port==null) throw new SecurityException("//bodi/connect");
        
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
        
        if(port==null) throw new SecurityException("//bodi/connect");
        
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
                    
                    httpServerContext = new HTTPServerContext(this, networkContext, this.pollStoredHTTPSessions(networkContext)); //we care to use only existing bodisessions or handshakes
                    
                    if( this.tryValidateHTTPConnection(httpServerContext) )
                    {                                                                                              
                        
                        if(httpServerContext.inputString.startsWith(GET))
                        {
                            httpServerContext = new HTTPServerContext(this, GET, httpServerContext);

                            httpServerContext.processprotocol(httpServerContext);

                            httpServerContext.processrequest(httpServerContext);

                            httpServerContext.processsesponse(httpServerContext);
                            
                            //
                            
                            this.trystorecontextstobodhi(httpServerContext, networkContext);
                        }
                        else
                        {
                            httpServerContext = new HTTPServerContext(this, OTHER, "", httpServerContext.networkContext, new HTTPConnection());

                            httpServerContext.processsesponse(httpServerContext);
                        }                                                
                    }  
                    else
                    {
                        httpServerContext = new HTTPServerContext(this, OTHER, "", httpServerContext.networkContext, new HTTPConnection());

                        httpServerContext.processsesponse(httpServerContext);
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
     * Store session objects with a Bodi carekeeper
     * 
     * @param connectioncontext
     * @param connection
     * @return
     * @throws Exception 
     */    
    public Boolean trystorecontextstobodhi(HTTPServerContext connectioncontext, NetworkContext connection) throws Exception
    {
        if(connectioncontext==null) return false;
        
        if(connection==null) return false;
        
        if(connection.socket==null) return false;
        
        Bndi.context("//bodi/server/remote/bodiconnections").put(connectioncontext.httpConnection.sessionid.toString(), connectioncontext.httpConnection);
        
        Bndi.context("//bodi/server/remote/netconnections").put(connection.socket.getInetAddress().toString(), connection);
        
        return true;
    }
    
    /**
     * Returns the Collection of Bodiconnections from the Bodi server context.
     * 
     * @return The Collection of Bodiconnections from the Bodi server context.
     */
    public Collection<HTTPConnection> getbodiconnections()
    {
        Collection<Object> objects = Bndi.context("//bodi/server/remote/bodiconnections").values();
        
        Collection<HTTPConnection> _connections = (Collection<HTTPConnection>)(Collection<?>)objects;
        
        return _connections;
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
    
    /**
     * 
     * @param networkcontext
     * @return
     * @throws Exception 
     */
    public HTTPConnection pollStoredHTTPSessions(NetworkContext networkcontext) throws Exception
    {
        if(networkcontext==null) throw new SecurityException("//bodi/connect");
        
        if(networkcontext.inqueue==null) throw new SecurityException("//bodi/connect");        
                    
        String[] tokens = networkcontext.inqueue.toString().split(" ");                
        
        for(String token : tokens)
        {          
            if(token.trim().startsWith("//sessionid"))
            {
                for(HTTPConnection existingconnection : this.getbodiconnections())
                {
                    token = token.trim().replace("//sessionid=", "");
                    
                    if(token==null || existingconnection.sessionid==null) continue;                        
                    
                    int receivedsessionid = Integer.parseInt(token);
                    
                    int existingsessionid = existingconnection.sessionid;
                    
                    if(receivedsessionid == existingsessionid)
                    {
                        if(existingconnection.ttl>0)
                            
                        return existingconnection;
                        
                        else return null;                            
                    }
                }
            }
        }
        
        if(networkcontext.inqueue.toString().startsWith("//handshake")) 
        {
            return new HTTPConnection();
        }
        
        else return null;
    }
    
    /**
     * 
     * @param connection
     * @return 
     */
    public Boolean isvalidsessionid(HTTPConnection connection)
    {
        if(connection==null) return false;
        
        if(connection.sessionid==null) return false;
        
        for(HTTPConnection existingconnection : this.getbodiconnections())
        {
            if(Objects.equals(existingconnection.sessionid, connection.sessionid))
            {
                return true;
            }
        }
        
        return false;
    }  
    
    /**
     * 
     * @param connection
     * @return 
     */
    public Boolean isValidSession(HTTPConnection connection)
    {
        if(connection==null) return false;
        
        if(connection.sessionid==null) return false;
        
        if(connection.ttl<=0) return false;
        
        for(HTTPConnection existingConnection : this.getbodiconnections())
        {
            if(Objects.equals(existingConnection.sessionid, connection.sessionid))
            {                
                return connection.ttl > 0;                
            }
        }
        
        return false;
    }
}