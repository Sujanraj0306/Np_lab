import java.io.*;
import java.net.*;
import java.util.*;

public class Checksum_Sender {
    
    private final int MAX = 100;

    
    private Socket socket = null;
    private ServerSocket servsock = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public Checksum_Sender(int port) throws IOException {
        servsock = new ServerSocket(port);
        System.out.println("Waiting for receiver to connect...");
        
        
        socket = servsock.accept();
        System.out.println("Receiver connected!");

        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        while (true) {
            int i, l, sum = 0, nob;
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter data length: ");
            l = sc.nextInt();

            int data[] = new int[MAX];

            
            int c_data[] = new int[MAX];

            System.out.println("Enter data to send: ");
            for (i = 0; i < l; i++) {
                data[i] = sc.nextInt();

               
                nob = (int) (Math.floor(Math.log(data[i]) / Math.log(2))) + 1;

                
                c_data[i] = ((1 << nob) - 1) ^ data[i];

               
                sum += c_data[i];
            }

          
            data[i] = sum;
            l += 1;

            System.out.println("Checksum Calculated: " + sum);
            System.out.println("Sending data with checksum...");

           
            dos.writeInt(l);

        
            for (int j = 0; j < l; j++) {
                dos.writeInt(data[j]);
            }
            String response = dis.readUTF().trim(); 
           
            if (response.equals("success")) {
                System.out.println("Thanks for the feedback! Message received successfully!");
            } else if (response.equals("failure")) {
                System.out.println("Message was not received successfully!");
            } else {
                System.out.println("Unexpected response from receiver: " + response);
            }
            break;
        }

       
        dis.close();
        dos.close();
        socket.close();
        servsock.close();
    }

 
    public static void main(String args[]) throws IOException {
        new Checksum_Sender(3000);
    }
}
