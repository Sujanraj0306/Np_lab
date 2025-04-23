import java.net.*;
import java.io.*;
import java.util.Scanner;
public class PayrollClient {
     public static void main(String[] args) {
          try {
               DatagramSocket clientSocket = new DatagramSocket();
               InetAddress serverAddress = InetAddress.getByName("localhost");
               byte[] sendData;
               byte[] receiveData = new byte[1024];
               Scanner scanner = new Scanner(System.in);
               System.out.println("Enter Employee Name: ");
               String empName = scanner.nextLine();
               System.out.println("Enter Employee No: ");
               int empNo = scanner.nextInt();
               System.out.println("Enter Basic Salary: ");
               double basic = scanner.nextDouble();
               System.out.println("Enter DA: ");
               double da = scanner.nextDouble();
               System.out.println("Enter HRA: ");
               double hra = scanner.nextDouble();
               System.out.println("Enter PF: ");
               double pf = scanner.nextDouble();
               String message = empName + "," + empNo + "," + basic + "," + da + "," + hra + "," + pf;
               sendData = message.getBytes();
               DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);
               clientSocket.send(sendPacket);
               DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
               clientSocket.receive(receivePacket);
               String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
               System.out.println("Response from Server: " + response);
               clientSocket.close();
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}
