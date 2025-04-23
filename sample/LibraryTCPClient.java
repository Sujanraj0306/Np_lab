import java.io.*;
import java.net.*;

public class LibraryTCPClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (true) {
                System.out.println("\nLibrary Management System (TCP)");
                System.out.println("1. List Available Books");
                System.out.println("2. Check Book Availability");
                System.out.println("3. Borrow a Book");
                System.out.println("4. Return a Book");
                System.out.println("5. Add a New Book");
                System.out.println("6. END");
                System.out.print("Choose an option (1-6): ");
                
                int choice;
                try {
                    choice = Integer.parseInt(userInput.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number between 1-6.");
                    continue;
                }

                if (choice == 6) {
                    out.println("END");
                    System.out.println("Exiting Library System...");
                    break;
                }

                String command = "";
                switch (choice) {
                    case 1:
                        command = "list";
                        break;
                    case 2:
                        System.out.print("Enter book name to check availability: ");
                        command = "check:" + userInput.readLine();
                        break;
                    case 3:
                        System.out.print("Enter book name to borrow: ");
                        command = "borrow:" + userInput.readLine();
                        break;
                    case 4:
                        System.out.print("Enter book name to return: ");
                        command = "return:" + userInput.readLine();
                        break;
                    case 5:
                        System.out.print("Enter new book name to add: ");
                        command = "add:" + userInput.readLine();
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                        continue;
                }

                // Send request to server
                out.println(command);

                // Read multi-line response properly
                String response;
                System.out.println("Server Response:");
                while ((response = in.readLine()) != null && !response.isEmpty()) {
                    System.out.println(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
