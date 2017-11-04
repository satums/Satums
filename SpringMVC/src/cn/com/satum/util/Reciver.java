package cn.com.satum.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Reciver {
	  public static void recive() {  
	        System.out.println("---recive---");  
	        // 接收端  
	        try {  
	            //创建接收方的套接字 对象  并与send方法中DatagramPacket的ip地址与端口号一致  
	            DatagramSocket socket = new DatagramSocket(8080,  
	                    InetAddress.getByName("192.168.1.101"));  
	            //接收数据的buf数组并指定大小  
	            byte[] buf = new byte[1024];  
	            //创建接收数据包，存储在buf中  
	            DatagramPacket packet = new DatagramPacket(buf, buf.length);  
	            //接收操作  
	            socket.receive(packet);  
	            byte data[] = packet.getData();// 接收的数据  
	            InetAddress address = packet.getAddress();// 接收的地址  
	            System.out.println("接收的文本:::" + new String(data));  
	            System.out.println("接收的ip地址:::" + address.toString());  
	            System.out.println("接收的端口::" + packet.getPort()); // 9004  
	            // 告诉发送者 我接收完毕了  
	            String temp = "我接收完毕了";  
	            byte buffer[] = temp.getBytes();  
	            //创建数据报，指定发送给 发送者的socketaddress地址  
	            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length,  
	                    packet.getSocketAddress());  
	            //发送  
	            socket.send(packet2);  
	            //关闭  
	            socket.close();  
	        } catch (SocketException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	    }  
	  
	   
}
