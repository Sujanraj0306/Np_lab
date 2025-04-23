import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = buffReader.readLine();
            clientHandlers.add(this);
            broadcastMessage(name + " has entered the room!");
        } catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                if (messageFromClient == null) {
                    closeAll(socket, buffReader, buffWriter);
                    break;
                }
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeAll(socket, buffReader, buffWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.name.equals(name)) {
                    clientHandler.buffWriter.write(messageToSend);
                    clientHandler.buffWriter.newLine();
                    clientHandler.buffWriter.flush();
                }
            } catch (IOException e) {
                closeAll(clientHandler.socket, clientHandler.buffReader, clientHandler.buffWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(name + " has left the chat.");
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        removeClientHandler();
        try {
            if (buffReader != null) buffReader.close();
            if (buffWriter != null) buffWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
