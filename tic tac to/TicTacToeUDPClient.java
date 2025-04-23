import java.net.*;
import java.io.*;
public class TicTacToeUDPClient {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Enter position (1-9) or 'exit' to quit: ");
                String move = reader.readLine();
                byte[] buffer = move.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, 5000);
                socket.send(packet);
                if (move.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting game...");
                    break;
                }
                byte[] responseBuffer = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                socket.receive(responsePacket);
                String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println(response);
                if (response.contains("wins") || response.contains("draw")) {
                    System.out.println("Game Over!");
                    break;
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
