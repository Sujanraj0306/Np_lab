 import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class Bankclient{
	public static void main(String[] args) throws IOException {
		InetAddress ip = InetAddress.getLocalHost();
		int port = 4444;
		Scanner sc = new Scanner(System.in);
		Socket s = new Socket(ip, port);
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
          System.out.println("enter the initial balance:");
		String balance = sc.nextLine();

		while (true) {
			System.out.print("\nMenue:\n1.balance\n2.deposit\n3.withdraw\n");
			System.out.println("\nyour choice:");
			String inp = sc.nextLine();
               if(inp.equals("1")){
                    inp=inp+" "+balance+" "+"0";
                    dos.writeUTF(inp);
               }
               else if(inp.equals("3")){
                    System.out.println("enter the amount to withdraw:");
                    String w= sc.nextLine();
                    int b=Integer.parseInt(balance);
                    int wi=Integer.parseInt(w);
                    if(b<wi){
                         System.out.println("insufficient balance");
                         inp=inp+" "+balance+" "+"0";
                         dos.writeUTF(inp);
                    }
                    else{
                         inp=inp+" "+balance+" "+w;
                         dos.writeUTF(inp);
                    }
                    

               }
               else{
                    System.out.println("enter the amount to deposit:");
                    String w= sc.nextLine();
                    inp=inp+" "+balance+" "+w;
                    dos.writeUTF(inp);   
               }
			if (inp.equals("bye"))
				break;
			
			String ans = dis.readUTF();
               balance=ans;
			System.out.println("\nbalance=" + ans);
		}
	}
}
