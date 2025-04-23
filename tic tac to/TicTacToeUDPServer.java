import java.net.*;
import java.io.*;
public class TicTacToeUDPServer {
    private static char[][] board = {
            { '1', '2', '3' },
            { '4', '5', '6' },
            { '7', '8', '9' }
    };
    private static DatagramSocket socket;
    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(5000);
            System.out.println("Tic-Tac-Toe AI Server started...");
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String move = new String(packet.getData(), 0, packet.getLength()).trim();
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                if (move.equalsIgnoreCase("exit")) {
                    System.out.println("Game over.");
                    socket.close();
                    break;
                }
                String response = processMove(move);
                byte[] responseBytes = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress,
                        clientPort);
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String processMove(String move) {
        int pos;
        try {
            pos = Integer.parseInt(move);
            if (pos < 1 || pos > 9)
                return "Invalid move! Choose 1-9.";
        } catch (Exception e) {
            return "Invalid input!";
        }
        int row = (pos - 1) / 3;
        int col = (pos - 1) % 3;
        if (board[row][col] == 'X' || board[row][col] == 'O') {
            return "Spot already taken! Try again.";
        }
        board[row][col] = 'X'; // Player move
        String status = checkWinner();
        if (!status.isEmpty())
            return displayBoard() + status; // If player wins
        makeAIMove(); // AI move
        status = checkWinner();
        return displayBoard() + (status.isEmpty() ? "Your turn!" : status);
    }
    private static void makeAIMove() {
        int[] bestMove = minimax(board, true);
        board[bestMove[1]][bestMove[2]] = 'O'; // AI plays 'O'
    }
    private static int[] minimax(char[][] board, boolean isAI) {
        int bestScore = isAI ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int row = -1, col = -1;
        if (!checkWinner().isEmpty())
            return new int[] { evaluateBoard(), -1, -1 }; 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != 'X' && board[i][j] != 'O') {
                    char temp = board[i][j];
                    board[i][j] = isAI ? 'O' : 'X';
                    int score = minimax(board, !isAI)[0];
                    board[i][j] = temp;
                    if (isAI && score > bestScore) {
                        bestScore = score;
                        row = i;
                        col = j;
                    } else if (!isAI && score < bestScore) {
                        bestScore = score;
                        row = i;
                        col = j;
                    }
                }
            }
        }
        return new int[] { bestScore, row, col };
    }
    private static int evaluateBoard() {
        String status = checkWinner();
        if (status.contains("Player X wins"))
            return -10;
        if (status.contains("Player O wins"))
            return 10;
        return 0;
    }
    private static String checkWinner() {
        String[] lines = {
                "" + board[0][0] + board[0][1] + board[0][2],
                "" + board[1][0] + board[1][1] + board[1][2],
                "" + board[2][0] + board[2][1] + board[2][2],
                "" + board[0][0] + board[1][0] + board[2][0],
                "" + board[0][1] + board[1][1] + board[2][1],
                "" + board[0][2] + board[1][2] + board[2][2],
                "" + board[0][0] + board[1][1] + board[2][2],
                "" + board[0][2] + board[1][1] + board[2][0]
        };
        for (String line : lines) {
            if (line.equals("XXX"))
                return "Player X wins!";
            if (line.equals("OOO"))
                return "Player O wins!";
        }
        for (char[] row : board) {
            for (char cell : row) {
                if (Character.isDigit(cell))
                    return "";
            }
        }
        return "It's a draw!";
    }
    private static String displayBoard() {
        return "\n " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] +
                "\n---|---|---" +
                "\n " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] +
                "\n---|---|---" +
                "\n " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + "\n";
    }
}