import java.io.*;
import java.net.*;

public class LibraryTCPServer {
    private static String[][] books = {
            {"Java", "Available"},
            {"Machine Learning", "Not Available"},
            {"Data Structures", "Available"}
    };

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Library TCP Server is running on port 5000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            while (true) {
                String command = in.readLine();
                if (command == null || command.equalsIgnoreCase("END")) {
                    System.out.println("Client disconnected.");
                    break;
                }
                System.out.println("Received: " + command);
                String response = handleClientRequest(command);
                out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String handleClientRequest(String command) {
        System.out.println("Processing Command: " + command);

        if (command.equalsIgnoreCase("list")) {
            return listAvailableBooks();
        }

        String[] parts = command.split(":", 2);
        if (parts.length < 2) {
            return "Invalid request format!";
        }

        String action = parts[0].trim();
        String bookName = parts[1].trim();

        switch (action.toLowerCase()) {
            case "check":
                return checkAvailability(bookName);
            case "borrow":
                return borrowBook(bookName);
            case "return":
                return returnBook(bookName);
            case "add":
                return addBook(bookName);
            default:
                return "Invalid command!";
        }
    }

    private static String listAvailableBooks() {
        StringBuilder bookList = new StringBuilder("Available Books:\n");
        boolean found = false;

        for (String[] book : books) {
            if (book[1].equals("Available")) {
                bookList.append("- ").append(book[0]).append("\n");
                found = true;
            }
        }

        return found ? bookList.toString() : "No books available.";
    }

    private static String checkAvailability(String bookName) {
        for (String[] book : books) {
            if (book[0].equalsIgnoreCase(bookName)) {
                return book[1];
            }
        }
        return "Book not found";
    }

    private static String borrowBook(String bookName) {
        for (String[] book : books) {
            if (book[0].equalsIgnoreCase(bookName)) {
                if (book[1].equals("Available")) {
                    book[1] = "Not Available";
                    return bookName + " has been borrowed.";
                } else {
                    return "Book is not available.";
                }
            }
        }
        return "Book not found.";
    }

    private static String returnBook(String bookName) {
        for (String[] book : books) {
            if (book[0].equalsIgnoreCase(bookName)) {
                if (book[1].equals("Not Available")) {
                    book[1] = "Available";
                    return bookName + " has been returned.";
                } else {
                    return "Book is already available.";
                }
            }
        }
        return "Book not found.";
    }

    private static String addBook(String bookName) {
        String[][] newBooks = new String[books.length + 1][2];
        for (int i = 0; i < books.length; i++) {
            newBooks[i] = books[i];
        }
        newBooks[books.length] = new String[]{bookName, "Available"};
        books = newBooks;
        return bookName + " has been added to the library.";
    }
}
