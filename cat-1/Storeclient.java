import java.io.*;
import java.net.*;

public class Storeclient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5033)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                // Receive order from server
                String order = in.readLine();
                if (order.equals("4")) {
                    System.out.println("Server exited");
                    break;
                }

                // Receive extra items
                String extraItems = in.readLine();

                int price = 0;
                switch (order) {
                    case "1": price = 10; break; 
                    case "2": price = 5; break;  
                    case "3": price = 7; break;  
                    default: System.out.println("Invalid order received.");
                }

                int extraPrice = 0;
                if (!extraItems.equalsIgnoreCase("none")) {
                    String[] extras = extraItems.split(", ");
                    for (String extra : extras) {
                        if (extra.contains("-")) {
                            String[] parts = extra.split(" - ");
                            try {
                                extraPrice += Integer.parseInt(parts[1]); // Extract extra price
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid extra price format.");
                            }
                        }
                    }
                }

                int totalBill = price + extraPrice;
                System.out.println("Received order: Item " + order + " + Extras: " + extraItems + " - Calculating bill...");

                // Send bill to server
                out.println("Total Bill: Rs" + totalBill);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}