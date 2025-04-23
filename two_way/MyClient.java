import java. io. *;
import java. net. *;
public class MyClient {
     public static void main(String[] args) {
         try {
             Socket s = new Socket("localhost", 6663);
             DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             DataInputStream dis = new DataInputStream(s.getInputStream());
             BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in));
             Thread receiveThread = new Thread(() -> {
                 try {
                     String serverMessage;
                     while (true) {
                         serverMessage = dis.readUTF();
                         if (serverMessage.equals("exit")) break;
                         System.out.println("Server: " + serverMessage);
                     }
                 } catch (IOException e) {
                     System.out.println("Disconnected from server");
                 }
             });
             receiveThread.start();
             String clientMessage = "";
             while (!clientMessage.equals("exit")) {    
                 System.out.print("-");           
                 clientMessage = clientReader.readLine();
                 dos.writeUTF(clientMessage);
                 dos.flush();
             }
             dos.close();
             dis.close();
             s.close();
         } catch (Exception e) {
             System.out.println(e);
         }
     }
 }