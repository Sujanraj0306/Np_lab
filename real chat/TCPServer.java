import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            
            // Get and print the server's local IP address
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server started at IP: " + serverIP);
            System.out.println("Waiting for a client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    String clientMsg;
                    while ((clientMsg = input.readLine()) != null) {
                        System.out.println("\nClient: " + clientMsg);
                        System.out.print("You: ");
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            receiveThread.start();

            String message;
            while ((message = userInput.readLine()) != null) {
                output.println(message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
