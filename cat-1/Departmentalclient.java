
import java.io.*;
import java.net.*;

public class Departmentalclient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6022);
        // while(true){
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);

            if (line.contains("please enter your order")) {
                break;
            }
        }
        
        System.out.println("enter your order (comma-separated items from the menu):");
        String order = userInput.readLine();
        
        out.println(order);  
    
        while ((line = in.readLine()) != null) {
            System.out.println(line); 
        }
    
        socket.close();
    }
    // }
}

