import java.io.*; // For input and output classes
import java.net.*; // For networking classes

public class Server {
    public static void main(String[] args) {
        try {
            // Step 1: Create a ServerSocket to listen on a specific port (6666 in this case)
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("Server is listening on port 6666...");

            // Step 2: Accept an incoming connection
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Step 3: Create a DataInputStream to receive messages from the client
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String clientMessage = "";

            // Step 4: Continuously read messages from the client
            while (!clientMessage.equals("exit")) {
                clientMessage = dis.readUTF(); // Read the client's message
                System.out.println("Client: " + clientMessage); // Display the message
            }

            // Step 5: Close the connections
            dis.close();
            socket.close();
            serverSocket.close();
            System.out.println("Server closed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}