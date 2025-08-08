package system.http.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Max Rupplin
 */
public class NetworkContext
{
    /**
     * 
     * @param server 
     */
    public NetworkContext(BaseServer server)
    {
        if(server==null) throw new SecurityException("//bodi/connect");
        
        this.server = server;
    }    
    
    public BaseServer server;
    
    public volatile Socket socket;
    
    public InputStream inputstream;
    
    public OutputStream outputstream;
    
    public StringBuffer inqueue = new StringBuffer();
    
    public StringBuffer outqueue = new StringBuffer();
    
    public String remoteaddress = null;
    
    public BufferedReader reader = null;
    
    public BufferedWriter writer = null;
    
    public ListenerThread thread;
    
    public Boolean isdonewriting;
    
    public Boolean haswriteready;
       
    /**
     * 
     * @param line 
     */
    public void appendline(String line) 
    {
        this.inqueue.append(line);
    }
    
    /**
     * 
     * @return 
     */
    public Boolean inputqueueisready()
    {
        return this.inqueue!=null && this.inqueue.length()>0;
    }   
    
    /**
     * 
     * @param connectioncontext
     * @return 
     */
    public Boolean processresponse(HTTPServerContext connectioncontext)
    {
        //set output buffer, flag, and bodi object reference into the output queue
        
        connectioncontext.networkContext.outqueue.append(connectioncontext.httpConnection.toString());
                                                                        
        connectioncontext.networkContext.haswriteready = true;
                        
        connectioncontext.networkContext.thread.outputlistenerthread.haswriteready = true;
                            
        //connectioncontext.networkcontext.inqueue = connectioncontext.networkcontext.inqueue.delete(0, connectioncontext.inputstring.length());      
        
        return true;
    }
    
    /**
     * 
     * @param connectioncontext
     * @return
     * @throws Exception 
     */
    public Boolean close(HTTPServerContext connectioncontext) throws Exception
    {
        this.socket.close();
        
        return false;
    }
    
    /**
     * 
     * @return 
     */
    public Boolean issocketclosed()
    {
        try
        {
            this.writer.write("");
        }
        catch(Exception e)
        {
            return true;
        }
        
        return false;
    }    
    
    /**
     * 
     * @return 
     */
    public Boolean issocketconnected()
    {
        try
        {
            this.writer.write("");
        }
        catch(Exception e)
        {
            return false;
        }
        
        return true;
    }      
}
