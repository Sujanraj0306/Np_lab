import java.io.*;
import java.net.*;
import java.util.*;

public class HotelServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5002)) {
            System.out.println("Server is waiting for a cook to connect...");
            Socket socket = serverSocket.accept();
            System.out.println("Cook connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Display menu
                System.out.println("\n--- Hotel Menu ---");
                System.out.println("1. Pizza - $10");
                System.out.println("2. Burger - $5");
                System.out.println("3. Pasta - $7");
                System.out.println("4. Exit");

                System.out.print("Enter item number to order: ");
                String order = scanner.nextLine();

                if (order.equals("4")) {
                    out.println(order);
                    System.out.println("Exiting...");
                    break;
                }

                // Ask for extras
                System.out.print("Add extra items (e.g., 'extra cheese - 2', 'none'): ");
                String extraItems = scanner.nextLine();

                // Send order and extra items to the cook
                out.println(order);
                out.println(extraItems);

                // Receive bill from cook
                String bill = in.readLine();
                System.out.println("Cook sent the bill: " + bill);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}