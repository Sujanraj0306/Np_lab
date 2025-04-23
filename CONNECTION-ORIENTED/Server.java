import java.io.*;
import java.net.*;

public class Server {
public static void main(String[] args) throws Exception {
try {
ServerSocket server = new ServerSocket(8888);
int counter = 0;
System.out.println("Waiting for client");
while (true) { {
Socket serverClient = server.accept();
System.out.println("New client connected");
ServerClientThread sct = new ServerClientThread(serverClient, counter);
sct.start();
counter++;
}
}
} catch (IOException e) {
System.out.println(e);
}
}
}

class ServerClientThread extends Thread {
Socket serverClient;
int clientNo;

ServerClientThread(Socket inSocket, int counter) {
serverClient = inSocket;
clientNo = counter;
}

public void run() {
try {
DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
DataOutputStream outStream = new
DataOutputStream(serverClient.getOutputStream());
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String clientMessage = "", serverMessage = "";
while (!clientMessage.equals("quit")) {
clientMessage = inStream.readUTF();
System.out.println("client "+(clientNo+1)+": " + clientMessage);
serverMessage = br.readLine();
outStream.writeUTF(serverMessage);
outStream.flush();

}
inStream.close();
outStream.close();
serverClient.close();
} catch (Exception ex) {
System.out.println(ex);
} finally {
System.out.println("exit");
}
}
}