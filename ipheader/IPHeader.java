import java.net.InetAddress;
import java.net.UnknownHostException;
public class IPHeader {
// IP header fields
private int version;
private int headerLength;
private String sourceIP;
private String destinationIP;
private int totalLength;
// Constructor to initialize header
public IPHeader(int version, int headerLength, String sourceIP, String
destinationIP, int totalLength) {
this.version = version;
this.headerLength = headerLength;
this.sourceIP = sourceIP;
this.destinationIP = destinationIP;
this.totalLength = totalLength;
}
// Method to display IP Header details
public void displayHeader() {
System.out.println("IP Header Details:");
System.out.println("Version: " + this.version);
System.out.println("Header Length: " + this.headerLength);
System.out.println("Source IP: " + this.sourceIP);
System.out.println("Destination IP: " + this.destinationIP);

System.out.println("Total Length: " + this.totalLength);
}
// Main function to test IPHeader
public static void main(String[] args) {
try {
InetAddress localHost = InetAddress.getLocalHost();
String sourceIP = localHost.getHostAddress();
String destinationIP = "192.168.1.1"; // Example destination IP
// IP Header settings (simplified example)
int version = 4; // IPv4z
int headerLength = 20; // typical length for IPv4 header
int totalLength = 60; // an example total length of the packet
IPHeader ipHeader = new IPHeader(version, headerLength,
sourceIP, destinationIP, totalLength);
ipHeader.displayHeader();
} catch (UnknownHostException e) {
System.out.println("Error retrieving local host address.");
}
}
}