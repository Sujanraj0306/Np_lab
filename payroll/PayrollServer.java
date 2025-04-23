import java.net.*;
import java.io.*;
public class PayrollServer {
     public static void main(String[] args) {
          try {
               DatagramSocket serverSocket = new DatagramSocket(9876);
               byte[] receiveData = new byte[1024];
               byte[] sendData;
               System.out.println("Server is running...");
               while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    String[] details = receivedString.split(",");
                    String empName = details[0];
                    int empNo = Integer.parseInt(details[1]);
                    double basic = Double.parseDouble(details[2]);
                    double da = Double.parseDouble(details[3]);
                    double hra = Double.parseDouble(details[4]);
                    double pf = Double.parseDouble(details[5]);
                    double grossPay = basic + da + hra;
                    double netPay = grossPay - pf;
                    String response = "Emp No: " + empNo + " | Name: " + empName + " | Gross Pay: " + grossPay
                              + " | Net Pay: " + netPay;
                    sendData = response.getBytes();
                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress,
                              clientPort);
                    serverSocket.send(sendPacket);
               }
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}
