import java.net.*;
import java.util.Scanner;

class HospitalClientUDP {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 5011;

            System.out.println("Connected to Hospital Server");

            while (true) {
                System.out.println("\n--- Hospital Management System ---");
                System.out.println("1. View Doctors");
                System.out.println("2. Book Appointment");
                System.out.println("3. View My Appointment");
                System.out.println("4. Add Doctor");
                System.out.println("5. Check Doctor Availability");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();
                String request = "";

                switch (choice) {
                    case "1":
                        request = "VIEW_DOCTORS";
                        break;
                    case "2":
                        System.out.print("Enter Your Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Doctor's Name: ");
                        String doctor = scanner.nextLine();
                        request = "BOOK_APPOINTMENT:" + name + "," + doctor;
                        break;
                    case "3":
                        System.out.print("Enter Your Name: ");
                        String patientName = scanner.nextLine();
                        request = "VIEW_APPOINTMENT:" + patientName;
                        break;
                    case "4":
                        System.out.print("Enter Doctor's Name and Specialty (e.g., Dr. Miller - Dermatologist): ");
                        String newDoctor = scanner.nextLine();
                        request = "ADD_DOCTOR:" + newDoctor;
                        break;
                    case "5":
                        System.out.print("Enter Doctor's Name to check availability: ");
                        String checkDoctor = scanner.nextLine();
                        request = "CHECK_DOCTOR:" + checkDoctor;
                        break;
                    case "6":
                        request = "EXIT";
                        sendRequest(socket, serverAddress, serverPort, request);
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
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
