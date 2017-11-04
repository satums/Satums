package cn.com.satum.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Reciver {
	  public static void recive() {  
	        System.out.println("---recive---");  
	        // ���ն�  
	        try {  
	            //�������շ����׽��� ����  ����send������DatagramPacket��ip��ַ��˿ں�һ��  
	            DatagramSocket socket = new DatagramSocket(8080,  
	                    InetAddress.getByName("192.168.1.101"));  
	            //�������ݵ�buf���鲢ָ����С  
	            byte[] buf = new byte[1024];  
	            //�����������ݰ����洢��buf��  
	            DatagramPacket packet = new DatagramPacket(buf, buf.length);  
	            //���ղ���  
	            socket.receive(packet);  
	            byte data[] = packet.getData();// ���յ�����  
	            InetAddress address = packet.getAddress();// ���յĵ�ַ  
	            System.out.println("���յ��ı�:::" + new String(data));  
	            System.out.println("���յ�ip��ַ:::" + address.toString());  
	            System.out.println("���յĶ˿�::" + packet.getPort()); // 9004  
	            // ���߷����� �ҽ��������  
	            String temp = "�ҽ��������";  
	            byte buffer[] = temp.getBytes();  
	            //�������ݱ���ָ�����͸� �����ߵ�socketaddress��ַ  
	            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length,  
	                    packet.getSocketAddress());  
	            //����  
	            socket.send(packet2);  
	            //�ر�  
	            socket.close();  
	        } catch (SocketException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	    }  
	  
	   
}
