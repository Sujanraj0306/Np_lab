import java.io.*;
import java.net.*;

public class Departmentalserver {

    public static void main(String[] args) throws IOException {

        String[] menuItems = {"apple", "milk", "soda", "tea_power", "banana", "coffee_power"};
        double[] prices = {185.0, 40.0, 10.0, 20.5, 15.0, 20.0};

        ServerSocket serverSocket = new ServerSocket(6022);
        System.out.println("server is running...");

        Socket socket = serverSocket.accept();
        System.out.println("client connected!");

       while(true){
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        
        out.println("Menu:");
        for (int i = 0; i < menuItems.length; i++) {
            out.println(menuItems[i] + " - Rs " + prices[i]);
        }

        out.println("\nplease enter your order (comma-separated items):");
        String order = in.readLine();
        
        
        System.out.println("client ordered: " + order);

        String[] orderedItems = order.split(",");
        double totalBill = 0.0;
        for (String item : orderedItems) {
            item = item.trim(); 
            boolean found = false;
            
          
            for (int i = 0; i < menuItems.length; i++) {
                if (menuItems[i].equalsIgnoreCase(item)) {
                    totalBill += prices[i];
                    found = true;
                    break;
                }
            }

            if (!found) {
                out.println("Sorry, " + item + " is not on the menu.");
            }
        }

    
        out.println("\nTotal Bill: $" + totalBill);
      
        socket.close();
        serverSocket.close();
     }
    }
}
