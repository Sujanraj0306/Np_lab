import java.io.*;
import java.net.*;
public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 1234;

        Socket socket = new Socket(host, port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected to server");

        while (true) {
            String command = readCommand();
            if ("bye".equals(command)) {
                break;
            }
            out.writeUTF(command);
        }

        socket.close();
    }

    private static String readCommand() throws IOException {
        System.out.print("> ");
        return new DataInputStream(System.in).readUTF();
    }
}
