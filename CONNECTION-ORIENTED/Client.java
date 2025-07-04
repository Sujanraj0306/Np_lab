import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
public static void main(String[] args) throws Exception {
try {
Socket socket = new Socket("127.0.0.1", 8888);
DataInputStream inStream = new DataInputStream(socket.getInputStream());
DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in));
String clientMessage = "", serverMessage = "";
while (!clientMessage.equals ( "quit" ) ) {
clientMessage = br.readLine();
outStream.writeUTF(clientMessage);
serverMessage = inStream.readUTF();
System.out.println("server: " + serverMessage);
outStream.flush();
}
outStream.close();
inStream.close();
socket.close();
} catch (Exception e) {
System.out.println(e);
}
}
}