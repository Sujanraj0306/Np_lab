import java.net.*;
import java.io.*;

public class TicTacToeServer {
    private static char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    private static boolean isXTurn = true;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
        System.out.println("Tic-Tac-Toe UDP Server started...");

        while (true) {
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            String response = processMove(message);
            byte[] sendBuffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            socket.send(sendPacket);
        }
    }

    private static String processMove(String move) {
        try {
            String[] parts = move.split(" ");
            char player = parts[0].charAt(0);
            int position = Integer.parseInt(parts[1]);

            if ((player == 'X' && !isXTurn) || (player == 'O' && isXTurn)) {
                return "Not your turn.";
            }
            if (position < 1 || position > 9 || board[position - 1] != ' ') {
                return "Invalid move. Try again.";
            }

            board[position - 1] = player;
            isXTurn = !isXTurn;
            return checkGameStatus();
        } catch (Exception e) {
            return "Invalid input. Format: X/O 1-9.";
        }
    }

    private static String checkGameStatus() {
        int[][] winningCombos = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] combo : winningCombos) {
            if (board[combo[0]] != ' ' && board[combo[0]] == board[combo[1]] && board[combo[1]] == board[combo[2]]) {
                return "Player " + board[combo[0]] + " wins!";
            }
        }
        for (char c : board) {
            if (c == ' ') return getBoardState();
        }
        return "Draw!";
    }

    private static String getBoardState() {
        return "Current board:\n" +
               board[0] + " | " + board[1] + " | " + board[2] + "\n" +
               "---------\n" +
               board[3] + " | " + board[4] + " | " + board[5] + "\n" +
               "---------\n" +
               board[6] + " | " + board[7] + " | " + board[8];
    }
}
