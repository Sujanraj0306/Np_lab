import java.io.*;
import java.net.*;
public class server {
    public static void main(String[] args) throws IOException {
        int port = 1234;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (true) {
                    String line = in.readUTF();
                    if ("bye".equals(line)) {
                        System.out.println("Client sent bye");
                        break;
                    }
                    // Process the order
                    System.out.println("Received: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }
    }
}