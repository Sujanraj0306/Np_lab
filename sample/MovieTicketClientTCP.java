import java.io.*;
import java.net.*;
import java.util.Scanner;

class MovieTicketClientTCP {
     public static void main(String[] args) {
          try (Socket socket = new Socket("localhost", 5050);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)) {

               while (true) {
                    System.out.println(input.readLine()); // Print menu
                    String choice = scanner.nextLine();
                    output.println(choice);

                    if (choice.equals("1")) { // List movies
                         System.out.println(input.readLine());
                    } else if (choice.equals("2")) { // Add movie
                         System.out.println(input.readLine()); // Enter movie name
                         String name = scanner.nextLine();
                         output.println(name);

                         System.out.println(input.readLine()); // Enter total seats
                         String seats = scanner.nextLine();
                         output.println(seats);

                         System.out.println(input.readLine()); // Enter ticket price
                         String price = scanner.nextLine();
                         output.println(price);

                         System.out.println(input.readLine()); // Confirmation
                    } else if (choice.equals("3")) { // Book ticket
                         System.out.println(input.readLine()); // Movie list
                         System.out.println(input.readLine()); // Enter movie name
                         String name = scanner.nextLine();
                         output.println(name);

                         System.out.println(input.readLine()); // Enter ticket count
                         String tickets = scanner.nextLine();
                         output.println(tickets);

                         System.out.println(input.readLine()); // Booking confirmation
                    } else if (choice.equals("4")) { // Exit
                         System.out.println(input.readLine()); // Goodbye message
                         break;
                    } else {
                         System.out.println(input.readLine()); // Invalid choice
                    }
               }
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}
