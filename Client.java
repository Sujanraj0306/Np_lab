import java.io.*; // For input and output classes
import java.net.*; // For networking classes

public class Client {
    public static void main(String[] args) {
        try {
            // Step 1: Create a Socket to connect to the server (localhost and port 6666)
            Socket socket = new Socket("localhost", 6666);
            System.out.println("Connected to server.");

            // Step 2: Create a DataOutputStream to send messages to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Step 3: Create a BufferedReader to read user input from the console
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in));

            String messageToSend = "";

            // Step 4: Continuously read input from the user and send it to the server
            while (!messageToSend.equals("exit")) {
                System.out.print("Enter message: ");
                messageToSend = clientReader.readLine(); // Read user input
                dos.writeUTF(messageToSend); // Send the message to the server
                dos.flush(); // Ensure the data is sent immediately
            }

            // Step 5: Close the connections
            dos.close();
            socket.close();
            System.out.println("Client closed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}