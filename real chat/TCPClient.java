import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String SERVER_IP = "172.20.10.10"; 
        int SERVER_PORT = 2345;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to server!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = input.readLine()) != null) {
                        System.out.println("\nServer: " + serverMsg);
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