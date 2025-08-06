package system.http.server;

/**
 *
 * @author Max Rupplin
 */
class ListenerThread extends Thread
{
    
    public NetworkContext connection;
    
    public Boolean running = true;
    
    public InputListenerThread inputlistenerthread;
    
    public OutputListenerThread outputlistenerthread;
        
    /**
     * 
     * @param connection 
     */
    public ListenerThread(NetworkContext connection)
    {
        if(connection==null) throw new SecurityException("//bodi/connect");
        
        this.connection = connection;
                
        //
        
        this.setName("Listenerthread");
        
        //
        
        this.inputlistenerthread = new InputListenerThread(this);
        
        this.inputlistenerthread.start();
                
        //
        
        this.outputlistenerthread = new OutputListenerThread(this);
        
        this.outputlistenerthread.start();
    }
    
    /**
     * 
     */
    @Override
    public void run()
    {        
        try
        {
            while(running)            
            {              
                try
                {               
                    this.inputlistenerthread.checkinputqueue();
                }
                catch(Exception e)
                {
                    e.printStackTrace(System.err);
                }

                try
                {
                    this.outputlistenerthread.checkoutputqueue();
                }
                catch(Exception e)
                {
                    e.printStackTrace(System.err);
                }     
            }            
            
            this.sleepmillis(500l);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
        }
    } 
    
    /**
     * 
     * @param millis
     * @throws Exception 
     */
    protected void sleepmillis(Long millis) throws Exception
    {        
        Thread.currentThread().sleep(millis);     
    }        
}
