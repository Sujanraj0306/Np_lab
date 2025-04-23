import java.io.IOException; 
import java.net.InetAddress; 

public class Ping { 
    public static void main(String[] args) { 
        if (args.length != 1) { 
            System.out.println("Usage: java Ping <hostname>");
            return; 
        } 
        
        String hostname = args[0]; 
        try { 
            InetAddress address = InetAddress.getByName(hostname); 
            System.out.println("Pinging " + hostname + " [" + address.getHostAddress() + "]");
            
            long startTime = System.currentTimeMillis(); 
            if (address.isReachable(5)) { 
                long rtt = System.currentTimeMillis() - startTime; 
                System.out.println("Reply received in " + rtt + "ms"); 
            } else { 
                System.out.println("Request timed out."); 
            } 
        } catch (IOException e) { 
            System.out.println("Error: " + e.getMessage()); 
        } 
    } 
}