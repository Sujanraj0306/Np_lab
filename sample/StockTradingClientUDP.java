import java.net.*;
import java.util.Scanner;

class StockTradingClientUDP {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 5050;

            System.out.println("Connected to Stock Trading Server");

            System.out.print("Enter your username: ");
            String username = scanner.nextLine().trim();

            while (true) {
                System.out.println("\n--- Stock Trading System ---");
                System.out.println("1. View Stocks");
                System.out.println("2. Buy Stock");
                System.out.println("3. Sell Stock");
                System.out.println("4. View Portfolio");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                
                String choice = scanner.nextLine();
                String request = "";

                switch (choice) {
                    case "1":
                        request = "VIEW_STOCKS";
                        break;
                    case "2":
                        System.out.print("Enter stock symbol: ");
                        String buyStock = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Enter quantity: ");
                        int buyQuantity = Integer.parseInt(scanner.nextLine().trim());
                        request = "BUY:" + username + "," + buyStock + "," + buyQuantity;
                        break;
                    case "3":
                        System.out.print("Enter stock symbol: ");
                        String sellStock = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Enter quantity: ");
                        int sellQuantity = Integer.parseInt(scanner.nextLine().trim());
                        request = "SELL:" + username + "," + sellStock + "," + sellQuantity;
                        break;
                    case "4":
                        request = "PORTFOLIO:" + username;
                        break;
                    case "5":
                        request = "EXIT";
                        sendRequest(socket, serverAddress, serverPort, request);
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }

                String response = sendRequest(socket, serverAddress, serverPort, request);
                System.out.println("Server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendRequest(DatagramSocket socket, InetAddress address, int port, String message) throws Exception {
        byte[] requestData = message.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, port);
        socket.send(requestPacket);

        byte[] buffer = new byte[1024];
        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(responsePacket);

        return new String(responsePacket.getData(), 0, responsePacket.getLength()).trim();
    }
}
