import java.io.*;
import java.net.*;

public class RestaurantServer {
    public static void main(String[] args) throws IOException {
        // Define menu items and their prices using arrays
        String[] menuItems = {"Burger", "Pizza", "Pasta", "Salad", "Soda", "Coffee"};
        double[] prices = {85.0, 120.0, 90.0, 50.5, 25.0, 20.0};

        // Create ServerSocket to listen on port 6000
        ServerSocket serverSocket = new ServerSocket(6000);
        System.out.println("Server is running...");

        // Wait for client connection
        Socket socket = serverSocket.accept();
        System.out.println("Client connected!");

        // Create input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send the menu to the client
        out.println("Welcome to the Restaurant! Here's the menu:");
        for (int i = 0; i < menuItems.length; i++) {
            out.println(menuItems[i] + " - $" + prices[i]);
        }

        // Receive order from client
        out.println("\nPlease enter your order (comma-separated items):");
        String order = in.readLine();
        System.out.println("Client ordered: " + order);

        // Calculate the total bill
        String[] orderedItems = order.split(",");
        double totalBill = 0.0;
        for (String item : orderedItems) {
            item = item.trim(); // Remove extra spaces
            boolean found = false;
            
            // Find the item in the menu
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

        // Send the calculated bill to the client
        out.println("\nTotal Bill: $" + totalBill);

        // Close the connection
        socket.close();
        serverSocket.close();
    }
}
