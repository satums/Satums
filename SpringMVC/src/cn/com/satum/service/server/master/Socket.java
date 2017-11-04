package cn.com.satum.service.server.master;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Socket {
	public  String servers() throws IOException{
	 DatagramSocket socket = new DatagramSocket(8089);

     while (true) {
         byte[] buf = new byte[1024];
         // 解析数据包
         DatagramPacket packet = new DatagramPacket(buf, buf.length);
         socket.receive(packet);
         String ip = packet.getAddress().getHostAddress();
         buf = packet.getData();
         String data = new String(buf, 0, packet.getLength());
         System.out.println("收到 " + ip + " 发来的消息：" + data);
         return data;
     }


	    }  

}
