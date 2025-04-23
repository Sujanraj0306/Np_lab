import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
public class Bankserver {
	public static void main(String args[]) throws IOException {
		ServerSocket ss = new ServerSocket(4444);
		Socket s = ss.accept();
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		while (true) {
			String input = dis.readUTF();
			if (input.equals("bye"))
				break;
			// System.out.println("Equation received:-" + input);
			int result;

			StringTokenizer st = new StringTokenizer(input);
			int choice= Integer.parseInt(st.nextToken());
               int balance= Integer.parseInt(st.nextToken());
               int w =Integer.parseInt(st.nextToken());
			if (choice==1) {
				result = balance+w;
			}
			else if (choice==2) {
				result = balance + w;
			} 
			else {
				result = balance - w;
			}
			System.out.println("Sending the result...");
			dos.writeUTF(Integer.toString(result));
		}
	}
}
