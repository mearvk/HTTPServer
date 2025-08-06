package http.server;

import java.io.Serializable;

/**
 *
 * @author Max Rupplin
 */
public class SerializedCarrier implements Serializable
{
    public Object object = null;
    
    public Class _class = null;
    
    /**
     * 
     */
    public SerializedCarrier()
    {
        
    }
    
    /**
     * 
     * @param _class
     * @param object 
     */
    public SerializedCarrier(Class _class, Object object)
    {
        this._class = _class;
        
        this.object = object;
    }
}