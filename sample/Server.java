import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private static final String WIN CONDITIONS = "X wins, O wins or draw";

    private Board board;
    private Player currentPlayer;

    public Server() {
        board = new Board();
        currentPlayer = Player.X;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Listening for connections...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            receiveMove(clientSocket);
        }
    }

    private void receiveMove(Socket clientSocket) throws IOException {
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        int move = in.readInt();

        if (move < 1 || move > 9) {
            sendInvalidMoveMessage(clientSocket);
            return;
        }

        boolean isValidMove = board.isValidMove(move);

        if (!isValidMove) {
            sendInvalidMoveMessage(clientSocket);
            return;
        }

        board.makeMove(move, currentPlayer);

        if (board.isGameOver()) {
            sendBoardAndResult(clientSocket);
            return;
        }

        currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;

        // Minimax AI
        int aiMove = minimax(board, currentPlayer);
        board.makeMove(aiMove, currentPlayer);

        sendBoardAndResult(clientSocket);
    }

    private int minimax(Board board, Player player) {
        if (board.isGameOver()) {
            return 0;
        }

        if (player == Player.O) {
            return -1;
        }

        return board.getBestMove();
    }

    private void sendInvalidMoveMessage(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("Invalid move. Try again.");
    }

    private void sendBoardAndResult(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        board.printBoard();
        out.println(WIN CONDITIONS);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}

class Board {
    private final int SIZE = 3;
    private final Cell[][] cells;

    public Board() {
        cells = new Cell[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public boolean isValidMove(int move) {
        int row = (move - 1) / 3;
        int col = (move - 1) % 3;

        return cells[row][col].isEmpty();
    }

    public void makeMove(int move, Player player) {
        int row = (move - 1) / 3;
        int col = (move - 1) % 3;

        Cell cell = cells[row][col];
        if (cell.isEmpty()) {
            cell.setValue(player);
        }
    }

    public boolean isGameOver() {
        return !hasValidMove();
    }

    private boolean hasValidMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (cell.isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getBestMove() {
        // Simple Minimax implementation
        if (isGameOver()) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Cell cell = cells[i][j];
                    if (!cell.isEmpty()) {
                        return (i * SIZE + j) + 1;
                    }
                }
            }
        }

        // Player O moves first, so we try to block player X
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty()) {
                    return (i * SIZE + j) + 1;
                }
            }
        }

        // Try to win
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty()) {
                    return (i * SIZE + j) + 1;
                }
            }
        }

        // Default to center cell
        return SIZE * SIZE / 2 + 1;

    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                System.out.print(cell.getValue() == null ? "-" : cell.getValue());
                System.out.print(" | ");
            }
            System.out.println();
            if (i < SIZE - 1) {
                for (int k = 0; k < SIZE * 4 - 3; k++) {
                    System.out.print("-");
                }
                System.out.println();
            }
        }
    }

    class Cell {
        private String value;

        public boolean isEmpty() {
            return value == null;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
