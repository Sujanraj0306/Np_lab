import java.io.*;
import java.net.*;
import java.util.Scanner;

class StockTradingClientTCP {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5050);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Read username prompt
            System.out.print(readFullResponse(input) + " ");
            String username = scanner.nextLine().trim();
            output.println(username);

            while (true) {
                System.out.println(readFullResponse(input)); // Read menu

                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();
                output.println(choice);

                String response = readFullResponse(input); // Read response
                System.out.println(response);

                if (choice.equals("2") || choice.equals("3")) {
                    System.out.print("Enter stock symbol: ");
                    String stock = scanner.nextLine().toUpperCase();
                    output.println(stock);

                    System.out.print("Enter quantity: ");
                    String quantity = scanner.nextLine();
                    output.println(quantity);

                    System.out.println(readFullResponse(input)); // Read buy/sell response
                }

                if (choice.equals("5")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFullResponse(BufferedReader input) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null && !line.equals("END")) {
            response.append(line).append("\n");
        }
        return response.toString().trim();
    }
}