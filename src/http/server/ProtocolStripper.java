package http.server;

/**
 *
 * @author Max Rupplin
 */
public class ProtocolStripper
{        
    /**
     * 
     * @param parameterization
     * @return 
     */
    public static String stripforkey(HTTPServerContext parameterization)
    {
        if(parameterization==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = parameterization.inputstring.split(" ");
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//key="))
            {
                String substring = string.replace("//key=", "");
                
                return substring;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param parameterization
     * @return 
     */
    public static String stripforvalue(HTTPServerContext parameterization)
    {
        if(parameterization==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = parameterization.inputstring.split(" ");
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//value="))
            {                
                String substring = string.replace("//value=", "");
                
                return substring;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param parameterization
     * @return 
     */
    public static String stripforcontext(HTTPServerContext parameterization)
    {
        if(parameterization==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = parameterization.inputstring.split(" ");
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//context=")) 
            {
                String substring = string.replace("//context=", "");
                
                return substring;
            }
        }
        
        return null;
    }   
    
    /**
     * 
     * @param parameterization
     * @return 
     */
    public static String stripforprotocoltoken(HTTPServerContext parameterization)
    {
        if(parameterization==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = parameterization.networkcontext.inqueue.toString().split(" ");
        
        if(tokens==null) return null;
        
        if(tokens.length==0) return null;
        
        if(tokens[0].startsWith("//")) return tokens[0];
        
        return null;
    }
    
    /**
     * 
     * @param input
     * @return 
     */
    public static String stripforkey(String input)
    {
        if(input==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = input.split(" ");
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//key="))
            {
                String substring = string.replace("//key=", "");
                
                return substring;
            }
        }
        
        return null;
    }    
    
    /**
     * 
     * @param input
     * @return 
     */
    public static String stripforvalue(String input)
    {
        if(input==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = input.split(" ");
        
        if(tokens==null) return null;
        
        if(tokens.length==0) return null;        
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//value="))
            {                
                String substring = string.replace("//value=", "");
                
                return substring;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param input
     * @return 
     */
    public static String stripforcontext(String input)
    {
        if(input==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = input.split(" ");
        
        if(tokens==null) return null;
        
        if(tokens.length==0) return null;        
        
        for(String string : tokens)
        {
            if(string.trim().startsWith("//context=")) 
            {
                String substring = string.replace("//context=", "");
                
                return substring;
            }
        }
        
        return null;
    }   
    
    /**
     * 
     * @param input
     * @return 
     */
    public static String stripforprotocoltoken(String input)
    {
        if(input==null) throw new SecurityException("//bodi/connect");
        
        String tokens[] = input.split(" ");
        
        if(tokens==null) return null;
        
        if(tokens.length==0) return null;
        
        if(tokens[0].startsWith("//")) return tokens[0];
        
        return null;
    }    
}
