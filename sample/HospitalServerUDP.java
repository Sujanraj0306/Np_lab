import java.net.*;
import java.util.*;

class HospitalServerUDP {
    private static final int PORT = 5011;
    private static List<String> doctors = new ArrayList<>(Arrays.asList(
            "Dr. Smith - Cardiologist", 
            "Dr. John - Neurologist", 
            "Dr. Lisa - Pediatrician"));
    private static Map<String, String> appointments = new HashMap<>();
    private static Set<String> bookedDoctors = new HashSet<>();

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Hospital UDP Server started on port " + PORT);

            while (true) {
                byte[] buffer = new byte[1024];  // Clear buffer for each request
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength()).trim();
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String response = handleRequest(request);
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String handleRequest(String request) {
        String[] parts = request.split(":", 2);
        String command = parts[0].trim();
        String data = (parts.length > 1) ? parts[1].trim() : "";

        switch (command) {
            case "VIEW_DOCTORS":
                return "Available Doctors: " + String.join(", ", doctors);

            case "BOOK_APPOINTMENT":
                String[] details = data.split(",");
                if (details.length < 2) return "Invalid format. Send: BOOK_APPOINTMENT:YourName,DoctorName";

                String name = details[0].trim();
                String doctor = details[1].trim();

                if (doctors.stream().anyMatch(d -> d.equalsIgnoreCase(doctor))) {
                    if (!bookedDoctors.contains(doctor.toLowerCase())) {
                        appointments.put(name.toLowerCase(), doctor);
                        bookedDoctors.add(doctor.toLowerCase());
                        return "Appointment booked with " + doctor;
                    } else {
                        return "Doctor is already booked. Choose another doctor.";
                    }
                } else {
                    return "Doctor not available.";
                }

            case "VIEW_APPOINTMENT":
                return appointments.getOrDefault(data.toLowerCase(), "No appointment found.");

            case "ADD_DOCTOR":
                if (doctors.stream().noneMatch(d -> d.equalsIgnoreCase(data))) {
                    doctors.add(data);
                    return data + " has been added.";
                } else {
                    return "Doctor already exists.";
                }

            case "CHECK_DOCTOR":
                boolean available = doctors.stream().anyMatch(d -> d.equalsIgnoreCase(data))
                                           && !bookedDoctors.contains(data.toLowerCase());
                return available ? data + " is available." : data + " is not available.";

            case "EXIT":
                return "Goodbye!";

            default:
                return "Invalid request.";
        }
    }
}
