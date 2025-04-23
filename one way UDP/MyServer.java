import java.io.*;
import java.net.*;
class MyServer {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9000); 
        byte[] receiveData = new byte[500];
        System.out.println("Server is running and waiting for messages...");
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket); 
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength()); 
            System.out.println("Received from client: " + message); 
        }
    }
}


