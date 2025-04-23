import java.io.*;
import java.net.*;

public class Client {
    private static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", PORT);
        System.out.println("Connected to server");

        receiveBoard(clientSocket);

        while (true) {
            sendMove(clientSocket);
            receiveBoard(clientSocket);
            if (isGameOver(clientSocket)) {
                break;
            }
        }

        closeClient(clientSocket);
    }

    private static void sendMove(Socket clientSocket) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int move = scanner.nextInt();

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
true);
        out.println(move);
    }

    private static void receiveBoard(Socket clientSocket) throws 
IOException {
        BufferedReader in = new BufferedReader(new 
InputStreamReader(clientSocket.getInputStream()));
        System.out.print("Current board: ");
        in.readLine();
    }

    private static boolean isGameOver(Socket clientSocket) throws 
IOException {
        String result = readResult(clientSocket);
        if (result.equals("X wins")) {
            return true;
        } else if (result.equals("O wins")) {
            return true;
        } else if (result.equals("draw")) {
            return true;
        }

        closeClient(clientSocket);

        return false;
    }

    private static void closeClient(Socket clientSocket) throws 
IOException {
        clientSocket.close();
    }

    private static String readResult(Socket clientSocket) throws 
IOException {
        BufferedReader in = new BufferedReader(new 
InputStreamReader(clientSocket.getInputStream()));
        in.readLine(); // ignore board
        return in.readLine();
    }
}