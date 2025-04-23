import java.io.*;
import java.net.*;
import java.util.*;

class HospitalServerTCP {
    private static final int PORT = 5011;
    private static List<String> doctors = new ArrayList<>(Arrays.asList(
            "Dr. Smith - Cardiologist", 
            "Dr. John - Neurologist", 
            "Dr. Lisa - Pediatrician"));
    private static Map<String, String> appointments = new HashMap<>();
    private static Set<String> bookedDoctors = new HashSet<>(); // Track booked doctors

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Hospital TCP Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("Welcome to Hospital Management System");

                while (true) {
                    out.println("\n--- Hospital Management System ---\n" +
                                "1. View Doctors\n" +
                                "2. Book Appointment\n" +
                                "3. View My Appointment\n" +
                                "4. Add Doctor\n" +
                                "5. Check Doctor Availability\n" +
                                "6. Exit\n" +
                                "Enter your choice: ");
                    
                    String choice = in.readLine();
                    if (choice == null) break;

                    switch (choice) {
                        case "1":
                            out.println("Available Doctors: " + String.join(", ", doctors));
                            break;

                        case "2":
                            out.println("Enter Your Name:");
                            String name = in.readLine();
                            out.println("Enter Doctor's Name:");
                            String doctor = in.readLine();

                            if (doctors.stream().anyMatch(d -> d.toLowerCase().contains(doctor.toLowerCase()))) {
                                if (!bookedDoctors.contains(doctor.toLowerCase())) {
                                    appointments.put(name, doctor);
                                    bookedDoctors.add(doctor.toLowerCase()); // Mark doctor as booked
                                    out.println("Appointment booked with " + doctor);
                                } else {
                                    out.println("Doctor is already booked. Please choose another doctor.");
                                }
                            } else {
                                out.println("Doctor not available.");
                            }
                            break;

                        case "3":
                            out.println("Enter Your Name:");
                            String patientName = in.readLine();
                            if (appointments.containsKey(patientName)) {
                                out.println("Your Appointment: " + appointments.get(patientName));
                            } else {
                                out.println("No appointment found.");
                            }
                            break;

                        case "4":
                            out.println("Enter Doctor's Name and Specialty (e.g., Dr. Miller - Dermatologist):");
                            String newDoctor = in.readLine();
                            if (!doctors.contains(newDoctor)) {
                                doctors.add(newDoctor);
                                out.println(newDoctor + " has been added.");
                            } else {
                                out.println("Doctor already exists.");
                            }
                            break;

                        case "5":
                            out.println("Enter Doctor's Name to check availability:");
                            String checkDoctor = in.readLine();
                            boolean available = doctors.stream().anyMatch(d -> d.toLowerCase().contains(checkDoctor.toLowerCase()))
                                                       && !bookedDoctors.contains(checkDoctor.toLowerCase());
                            out.println(available ? checkDoctor + " is available." : checkDoctor + " is not available.");
                            break;

                        case "6":
                            out.println("Goodbye!");
                            socket.close();
                            return;

                        default:
                            out.println("Invalid choice. Please enter a number between 1 and 6.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
