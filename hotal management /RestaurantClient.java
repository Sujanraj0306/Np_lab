import java.io.*;
import java.net.*;

public class RestaurantClient {
    public static void main(String[] args) throws IOException {
        // Connect to the server (localhost:6000)
        Socket socket = new Socket("localhost", 6000);

        // Create input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Read and display server's messages (menu + order prompt)
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            // If the server asks for order input, break the loop to send user input
            if (line.contains("Please enter your order")) {
                break;
            }
        }

        // Get the order from the user
        System.out.println("Enter your order (comma-separated items from the menu):");
        String order = userInput.readLine();
        out.println(order);  // Send order to the server

        // Receive the total bill from the server
        while ((line = in.readLine()) != null) {
            System.out.println(line); // Print server's response
        }

        // Close the connection
        socket.close();
    }
}

