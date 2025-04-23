import java.io.*;
import java.net.*;
import java.util.*;

class StockTradingServerTCP {
    private static final int PORT = 5050;
    private static final Object lock = new Object(); // For thread synchronization
    private static Map<String, Integer> stockPrices = new HashMap<>();
    private static Map<String, Integer> stockInventory = new HashMap<>();
    private static Map<String, Map<String, Integer>> userPortfolios = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Stock Trading Server started on port " + PORT);

            // Initialize stocks
            stockPrices.put("AAPL", 150);
            stockPrices.put("TSLA", 800);
            stockPrices.put("GOOG", 2800);
            stockInventory.put("AAPL", 100);
            stockInventory.put("TSLA", 50);
            stockInventory.put("GOOG", 30);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start(); // Handle each client in a new thread
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                // Get username
                output.println("Enter your username:");
                output.println("END");
                username = input.readLine().trim();
                System.out.println("User connected: " + username);

                while (true) {
                    output.println(getMenu());
                    output.println("END");
                    String choice = input.readLine();
                    if (choice == null) break;

                    String response = handleRequest(choice);
                    output.println(response);
                    output.println("END");

                    if (choice.equals("5")) {
                        socket.close();
                        System.out.println(username + " disconnected.");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            }
        }

        private String getMenu() {
            return "\n--- Stock Trading System ---\n" +
                   "1. View Stocks\n" +
                   "2. Buy Stock\n" +
                   "3. Sell Stock\n" +
                   "4. View Portfolio\n" +
                   "5. Exit\n" +
                   "";
        }

        private String handleRequest(String choice) {
            switch (choice) {
                case "1":
                    return getStockList();
                case "2":
                    return buyStock();
                case "3":
                    return sellStock();
                case "4":
                    return getPortfolio();
                case "5":
                    return "Goodbye!";
                default:
                    return "Invalid choice. Try again.";
            }
        }

        private String getStockList() {
            synchronized (lock) {
                if (stockPrices.isEmpty() || stockInventory.isEmpty()) {
                    return "No stocks available.";
                }

                StringBuilder stockList = new StringBuilder("Available Stocks:\n");
                for (String stock : stockPrices.keySet()) {
                    stockList.append(stock)
                            .append(" - $").append(stockPrices.get(stock))
                            .append(" (").append(stockInventory.get(stock)).append(" available)\n");
                }

                return stockList.toString().trim();
            }
        }

        private String buyStock() {
            synchronized (lock) {
                try {
                    output.println("");
                    output.println("END");
                    String stockSymbol = input.readLine().trim().toUpperCase();
                    output.println("");
                    output.println("END");
                    int quantity = Integer.parseInt(input.readLine().trim());

                    if (!stockPrices.containsKey(stockSymbol)) return "Stock not found.";
                    if (stockInventory.get(stockSymbol) < quantity) return "Not enough stock available.";

                    stockInventory.put(stockSymbol, stockInventory.get(stockSymbol) - quantity);
                    userPortfolios.putIfAbsent(username, new HashMap<>());
                    userPortfolios.get(username).put(stockSymbol, userPortfolios.get(username).getOrDefault(stockSymbol, 0) + quantity);

                    return "Bought " + quantity + " shares of " + stockSymbol;
                } catch (Exception e) {
                    return "Invalid input.";
                }
            }
        }

        private String sellStock() {
            synchronized (lock) {
                try {
                    output.println("Enter stock symbol:");
                    output.println("END");
                    String stockSymbol = input.readLine().trim().toUpperCase();
                    output.println("Enter quantity:");
                    output.println("END");
                    int quantity = Integer.parseInt(input.readLine().trim());

                    if (!userPortfolios.containsKey(username) || userPortfolios.get(username).getOrDefault(stockSymbol, 0) < quantity)
                        return "Not enough shares to sell.";

                    userPortfolios.get(username).put(stockSymbol, userPortfolios.get(username).get(stockSymbol) - quantity);
                    stockInventory.put(stockSymbol, stockInventory.get(stockSymbol) + quantity);

                    return "Sold " + quantity + " shares of " + stockSymbol;
                } catch (Exception e) {
                    return "Invalid input.";
                }
            }
        }

        private String getPortfolio() {
            synchronized (lock) {
                if (!userPortfolios.containsKey(username)) return "No stocks in portfolio.";

                StringBuilder portfolio = new StringBuilder("Your Portfolio:\n");
                for (Map.Entry<String, Integer> entry : userPortfolios.get(username).entrySet()) {
                    portfolio.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" shares\n");
                }
                return portfolio.toString();
            }
        }
    }
}