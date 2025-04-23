import java.io.*;
import java.net.*;

public class LibraryUDPClient {
    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Request and display available books
            sendRequest(clientSocket, serverAddress, "list");
            System.out.println("Available books in the library:");
            System.out.println(receiveResponse(clientSocket));

            while (true) {
                System.out.println("\nLibrary Management System (UDP)");
                System.out.println("1. Check Book Availability");
                System.out.println("2. Borrow a Book");
                System.out.println("3. Return a Book");
                System.out.println("4. Add a New Book");
                System.out.println("5. END");
                System.out.print("Choose an option (1-5): ");
                int choice = Integer.parseInt(userInput.readLine());

                if (choice == 5) {
                    System.out.println("Exiting Library System...");
                    break;
                }

                String command = "";
                switch (choice) {
                    case 1:
                        System.out.print("Enter book name to check availability: ");
                        command = "check:" + userInput.readLine();
                        break;
                    case 2:
                        System.out.print("Enter book name to borrow: ");
                        command = "borrow:" + userInput.readLine();
                        break;
                    case 3:
                        System.out.print("Enter book name to return: ");
                        command = "return:" + userInput.readLine();
                        break;
                    case 4:
                        System.out.print("Enter new book name to add: ");
                        command = "add:" + userInput.readLine();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        continue;
                }

                sendRequest(clientSocket, serverAddress, command);
                System.out.println("Server Response: " + receiveResponse(clientSocket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(DatagramSocket socket, InetAddress address, String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, 5000);
        socket.send(sendPacket);
    }

    private static String receiveResponse(DatagramSocket socket) throws IOException {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }
}
