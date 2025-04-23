

import java.io.*;

import java.net.*;



class UDPClient {

    public static void main(String args[]) throws Exception {

        DatagramSocket clientSocket = new DatagramSocket(8000); 

        InetAddress serverAddress = InetAddress.getByName("localhost"); 

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));



        System.out.println("Type messages to send to the server (type 'exit' to quit):");



        byte[] sendData = new byte[500];

        byte[] receiveData = new byte[500];



        while (true) {

         

            System.out.print("You: ");

            String messageToSend = userInput.readLine();

            if (messageToSend.equalsIgnoreCase("exit")) {

                System.out.println("Exiting...");

                break; 

            }

            sendData = messageToSend.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9000);

            clientSocket.send(sendPacket);



    

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String messageFromServer = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println("Server: " + messageFromServer);

        }

        clientSocket.close(); 

    }

}


// javac UDPServer.java && java UDPServer
// javac UDPClient.java && java UDPClient