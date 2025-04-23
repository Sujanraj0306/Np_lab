import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class IPHeaderSettingExample {
    public static void main(String[] args) {
        try {
            // Fetch the system's assigned IP address dynamically from the kernel
            String systemIPAddress = getSystemIPAddress();
            if (systemIPAddress == null) {
                System.out.println("No valid network interface found. Exiting...");
                return;
            }

            System.out.println("Using system IP: " + systemIPAddress);

            // Create a DatagramSocket for UDP communication, bind it to the fetched IP
            DatagramSocket socket = new DatagramSocket();

            // Set IP header options
            socket.setTrafficClass(0x10); // TOS 2(Type of Service) - Example for Minimize Delay
            socket.setSoTimeout(3000); // Socket timeout (not an IP header field, but helpful)

            // Destination IP
            InetAddress destAddress = InetAddress.getByName("192.168.1.10");
            String message = "Hello from Java with IP header settings!";
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, destAddress, 8080);

            // Display packet details
            System.out.println("\n=== Packet Details ===");
            System.out.println("Source IP (Fetched from Kernel): " + systemIPAddress);
            System.out.println("Destination IP: " + destAddress.getHostAddress());
            System.out.println("Port: " + packet.getPort());
            System.out.println("Message: " + message);
            System.out.println("TOS (Type of Service): 0x" + Integer.toHexString(socket.getTrafficClass()));

            // IP Version Detection
            if (destAddress instanceof Inet4Address) {
                System.out.println("IP Version: IPv4");
            } else if (destAddress instanceof Inet6Address) {
                System.out.println("IP Version: IPv6");
            } else {
                System.out.println("Unknown IP Version");
            }

            // Send the packet
            socket.send(packet);

            System.out.println("Packet sent successfully.");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch system-assigned IP address from the kernel
    public static String getSystemIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual())
                    continue; // Skip inactive interfaces

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) { // Get first IPv4 Address
                      return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
          e.printStackTrace();
        }
        return null; // Return null if no valid IP is found
    }
}