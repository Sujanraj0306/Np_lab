import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public Client(Socket socket, String name) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;
            
            // Send the name to the server upon connecting
            buffWriter.write(name);
            buffWriter.newLine();
            buffWriter.flush();
        } catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void sendMessage() {
        try {
            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = sc.nextLine();
                buffWriter.write(name + ": " + messageToSend);
                buffWriter.newLine();
                buffWriter.flush();
            }
        } catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void readMessage() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = buffReader.readLine();
                    if (msgFromGroupChat != null) {
                        System.out.println(msgFromGroupChat);
                    } else {
                        closeAll(socket, buffReader, buffWriter);
                        break;
                    }
                } catch (IOException e) {
                    closeAll(socket, buffReader, buffWriter);
                    break;
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        try {
            if (buffReader != null) buffReader.close();
            if (buffWriter != null) buffWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        Socket socket = new Socket("localhost", 4321);
        Client client = new Client(socket, name);

        new Thread(client::readMessage).start();
        client.sendMessage();
    }
}
