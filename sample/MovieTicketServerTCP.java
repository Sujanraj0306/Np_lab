import java.io.*;
import java.net.*;
import java.util.*;

class MovieTicketServerTCP {
    private static final int PORT = 5050;
    private static Map<String, Movie> movies = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Movie Ticket Server started on port " + PORT);

            // Initialize some movies
            movies.put("Avengers", new Movie("Avengers", 50, 200));
            movies.put("Inception", new Movie("Inception", 30, 250));
            movies.put("Interstellar", new Movie("Interstellar", 40, 300));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Movie {
        String name;
        int availableSeats;
        int pricePerTicket;

        public Movie(String name, int availableSeats, int pricePerTicket) {
            this.name = name;
            this.availableSeats = availableSeats;
            this.pricePerTicket = pricePerTicket;
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    output.println("\n--- Movie Ticket Management System ---\n1. List Movies\n2. Add Movie\n3. Book Ticket\n4. Exit\nEnter your choice:");
                    String choice = input.readLine();
                    if (choice == null) break;

                    switch (choice) {
                        case "1":
                            output.println(getMovieList());
                            break;
                        case "2":
                            addMovie();
                            break;
                        case "3":
                            bookTicket();
                            break;
                        case "4":
                            output.println("Goodbye!");
                            socket.close();
                            return;
                        default:
                            output.println("Invalid choice! Try again.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            }
        }

        private String getMovieList() {
            if (movies.isEmpty()) return "No movies available.";

            StringBuilder list = new StringBuilder("Available Movies:\n");
            for (Movie movie : movies.values()) {
                list.append(movie.name).append(" - ").append(movie.availableSeats)
                        .append(" seats available, ₹").append(movie.pricePerTicket).append(" per ticket\n");
            }
            return list.toString();
        }

        private void addMovie() {
            try {
                output.println("Enter movie name:");
                String name = input.readLine();
                output.println("Enter total seats:");
                int seats = Integer.parseInt(input.readLine());
                output.println("Enter ticket price:");
                int price = Integer.parseInt(input.readLine());

                movies.put(name, new Movie(name, seats, price));
                output.println("Movie added successfully!");
            } catch (Exception e) {
                output.println("Invalid input! Try again.");
            }
        }

        private void bookTicket() {
            try {
                output.println(getMovieList());
                output.println("Enter movie name to book:");
                String name = input.readLine();

                if (!movies.containsKey(name)) {
                    output.println("Movie not found!");
                    return;
                }

                Movie movie = movies.get(name);
                output.println("Enter number of tickets:");
                int tickets = Integer.parseInt(input.readLine());

                if (movie.availableSeats < tickets) {
                    output.println("Not enough seats available!");
                    return;
                }

                movie.availableSeats -= tickets;
                int totalAmount = tickets * movie.pricePerTicket;
                output.println("Booking confirmed!\nMovie: " + name + "\nTickets: " + tickets + "\nTotal: ₹" + totalAmount);
            } catch (Exception e) {
                output.println("Invalid input! Try again.");
            }
        }
    }
}
