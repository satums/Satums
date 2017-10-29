package cn.com.satum.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
    // ������--->�ͻ��� �ͻ���--->������  
    // �����߷����ͻ�������,�ͻ��˷������ݸ�������  
    public static void send() {  
        System.out.println("---send----");  
        // ���Ͷ�  
        try {  
        	
            // �������ͷ����׽��� ���� ����9004Ĭ�϶˿ں�  
            DatagramSocket socket = new DatagramSocket(9010);  
            // ���͵�����  
           
            String text = "̷����ļҡ�����!";  
            byte[] buf = text.getBytes();  
              
            // �������ݱ���������������Ϊ length �İ����͵�ָ�������ϵ�ָ���˿ںš�  
            DatagramPacket packet = new DatagramPacket(buf, buf.length,  
                    InetAddress.getByName("192.168.1.101"), 9005);  
            // �Ӵ��׽��ַ������ݱ���  
            socket.send(packet);  
            // ���գ������߷��ص�����  
            displayReciveInfo(socket);  
            // �رմ����ݱ��׽��֡�  
            socket.close();  
        } catch (SocketException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        System.out.println("---send---end-"); 
    }  
  
    /** 
     * �������ݲ���ӡ���� 
     *  
     * @param socket 
     * @throws IOException 
     */  
    public static void displayReciveInfo(DatagramSocket socket)  
            throws IOException {  
        byte[] buffer = new byte[1024];  
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);  
        socket.receive(packet);  
  
        byte data[] = packet.getData();// ���յ�����  
        InetAddress address = packet.getAddress();// ���յĵ�ַ  
        System.out.println("���յ��ı�:::" + new String(data));  
        System.out.println("���յ�ip��ַ:::" + address.toString());  
        System.out.println("���յĶ˿�::" + packet.getPort()); // 9004  
    }  
  
   
}
