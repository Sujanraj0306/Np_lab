import java.io.*;
import java.net.*;

class UDPServer {

    public static void main(String args[]) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(9000);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Server is running. Waiting for messages from client...");

        byte[] receiveData = new byte[500];

        byte[] sendData = new byte[500];

        while (true) {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);

            String messageFromClient = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println("Client: " + messageFromClient);

            if (messageFromClient.equalsIgnoreCase("exit")) {

                System.out.println("Client exited. Server shutting down...");

                break;

            }

            System.out.print("You: ");

            String messageToSend = userInput.readLine();

            InetAddress clientAddress = receivePacket.getAddress();

            sendData = messageToSend.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, 8000);

            serverSocket.send(sendPacket);

        }

        serverSocket.close();

    }

}