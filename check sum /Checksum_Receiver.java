import java.net.*;
import java.io.*;
import java.util.*;

public class Checksum_Receiver {
    
    private Socket s = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    
    public Checksum_Receiver(InetAddress ip, int port) throws IOException {
        System.out.println("Connecting to sender...");
        s = new Socket(ip, port);
        System.out.println("Connected to sender!");

        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());

        while (true) {
            int i, l, nob, sum = 0;

          
            l = dis.readInt();

           
            int c_data[] = new int[l];
            int data[] = new int[l];

            System.out.println("Data received (including checksum): ");
            for (i = 0; i < data.length; i++) {
                
                data[i] = dis.readInt();
                System.out.println(data[i]);

             
                nob = (int) (Math.floor(Math.log(data[i]) / Math.log(2))) + 1;
                c_data[i] = ((1 << nob) - 1) ^ data[i];

              
                sum += c_data[i];
            }

            System.out.println("Sum (in ones complement): " + sum);

           
            nob = (int) (Math.floor(Math.log(sum) / Math.log(2))) + 1;
            sum = ((1 << nob) - 1) ^ sum;

            System.out.println("Calculated Checksum: " + sum);

            
            if (sum == 0) {
                dos.writeUTF("success");
                dos.flush();  
                System.out.println("Checksum verified! Data received correctly.");
            } else {
                dos.writeUTF("failure");
                dos.flush();  
                System.out.println("Checksum failed! Data corrupted.");
            }

        
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted: " + e.getMessage());
            }

            break;
        }

       
        dis.close();
        dos.close();
        s.close();
    }

   
    public static void main(String args[]) throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        new Checksum_Receiver(ip, 3000);
    }
}
