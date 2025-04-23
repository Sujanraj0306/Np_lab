import java.net.*;
import java.util.*;

class StockTradingServerUDP {
    private static final int PORT = 5050;
    private static Map<String, Integer> stockPrices = new HashMap<>();
    private static Map<String, Integer> stockInventory = new HashMap<>();
    private static Map<String, Map<String, Integer>> userPortfolios = new HashMap<>();

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Stock Trading Server started on port " + PORT);

            // Initial stocks
            stockPrices.put("AAPL", 150);
            stockPrices.put("TSLA", 800);
            stockPrices.put("GOOG", 2800);
            stockInventory.put("AAPL", 100);
            stockInventory.put("TSLA", 50);
            stockInventory.put("GOOG", 30);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                String request = new String(requestPacket.getData(), 0, requestPacket.getLength()).trim();
                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();

                System.out.println("[SERVER] Received request: " + request);
                String response = processRequest(request);

                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request) {
        String[] parts = request.split(":", 2);
        String command = parts[0].trim();
        String data = (parts.length > 1) ? parts[1].trim() : "";

        switch (command) {
            case "VIEW_STOCKS":
                return stockPrices.entrySet().stream()
                        .map(entry -> entry.getKey() + " - $" + entry.getValue() + " (" + stockInventory.get(entry.getKey()) + " available)")
                        .reduce("Available Stocks:\n", (acc, stock) -> acc + stock + "\n");

            case "BUY":
                return handleBuyRequest(data);

            case "SELL":
                return handleSellRequest(data);

            case "PORTFOLIO":
                return viewPortfolio(data);

            case "EXIT":
                return "Goodbye!";

            default:
                return "Invalid request.";
        }
    }

    private static String handleBuyRequest(String data) {
        String[] parts = data.split(",");
        if (parts.length < 3) return "Invalid format. Use: BUY:username,stockSymbol,quantity";

        String username = parts[0].trim();
        String stockSymbol = parts[1].trim().toUpperCase();
        int quantity;

        try {
            quantity = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            return "Invalid quantity format.";
        }

        if (!stockPrices.containsKey(stockSymbol)) return "Stock not found.";
        if (stockInventory.get(stockSymbol) < quantity) return "Not enough stock available.";

        stockInventory.put(stockSymbol, stockInventory.get(stockSymbol) - quantity);
        userPortfolios.putIfAbsent(username, new HashMap<>());
        userPortfolios.get(username).put(stockSymbol, userPortfolios.get(username).getOrDefault(stockSymbol, 0) + quantity);

        return "Bought " + quantity + " shares of " + stockSymbol;
    }

    private static String handleSellRequest(String data) {
        String[] parts = data.split(",");
        if (parts.length < 3) return "Invalid format. Use: SELL:username,stockSymbol,quantity";

        String username = parts[0].trim();
        String stockSymbol = parts[1].trim().toUpperCase();
        int quantity;

        try {
            quantity = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            return "Invalid quantity format.";
        }

        if (!userPortfolios.containsKey(username) || userPortfolios.get(username).getOrDefault(stockSymbol, 0) < quantity)
            return "Not enough shares to sell.";

        userPortfolios.get(username).put(stockSymbol, userPortfolios.get(username).get(stockSymbol) - quantity);
        stockInventory.put(stockSymbol, stockInventory.get(stockSymbol) + quantity);

        return "Sold " + quantity + " shares of " + stockSymbol;
    }

    private static String viewPortfolio(String username) {
        if (!userPortfolios.containsKey(username)) return "No stocks in portfolio.";

        return userPortfolios.get(username).entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue() + " shares")
                .reduce("Your Portfolio:\n", (acc, stock) -> acc + stock + "\n");
    }
}
