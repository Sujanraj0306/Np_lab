import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Ex2 {
    public static void main(String[] args) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localhost.getHostAddress());
            System.out.println("Local Host name: " + localhost.getHostName());
            
            NetworkInterface ni = NetworkInterface.getByInetAddress(localhost);
            byte[] mac = ni.getHardwareAddress();
            System.out.print("MAC address: ");
            
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                    stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
            System.out.println(stringBuilder.toString());
            
        } catch (UnknownHostException | SocketException ex) {
            ex.printStackTrace();
        }
    }
}
