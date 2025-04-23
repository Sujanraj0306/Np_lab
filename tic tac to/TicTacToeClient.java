import java.net.*;
import java.io.*;
import java.util.*;

public class TicTacToeClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your player symbol (X or O):");
        String player = scanner.nextLine().toUpperCase();

        while (true) {
            System.out.println("Enter your move (1-9):");
            String move = player + " " + scanner.nextLine();
            byte[] sendBuffer = move.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 9876);
            socket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(response);

            if (response.contains("wins") || response.contains("Draw")) {
                break;
            }
        }
        socket.close();
    }
}
