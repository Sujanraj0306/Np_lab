import java.io.*;
import java.net.*;
class MyClient {
    public static void main(String args[]) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket(); 
        byte[] sendData = new byte[500];
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter messages to send to the server (type 'exit' to quit):");
        while (true) {
            String message = userInput.readLine();
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Client exiting...");
                break; 
            }
            sendData = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName("localhost"); 
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9000); 
            clientSocket.send(sendPacket); 
        }
        clientSocket.close(); 
    }

}



