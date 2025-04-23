import java. io. *;
import java. net. *;
public class MyServer {
     public static void main(String[] args) {
         try {
             ServerSocket ss = new ServerSocket(6663);
             System.out.println("Server is waiting for client...");
             Socket s = ss.accept();
             System.out.println("Client connected!");
             DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             DataInputStream dis = new DataInputStream(s.getInputStream());            
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(System.in));
             Thread receiveThread = new Thread(() -> {
                 try {
                     String clientMessage;
                     while (true) {
                         clientMessage = dis.readUTF();
                         if (clientMessage.equals("exit")) break;
                         System.out.println("Client: " + clientMessage);
                         
                     }
                 } catch (IOException e) {
                     System.out.println("Client disconnected");
                 }
             });
             receiveThread.start();
             String serverMessage = "";
             while (!serverMessage.equals("exit")) {
               System.out.print("-");
                 serverMessage = serverReader.readLine();
                 dos.writeUTF(serverMessage);
                 dos.flush();
             }
             dos.close();
             dis.close();
             s.close();
             ss.close(); 
         } catch (Exception e) {
             System.out.println(e);
         }
     }
 }