package cn.com.satum.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
	DatagramSocket socket = null;

	// 发送者--->客户端 客户端--->发送者
	// 发送者发给客户端数据,客户端返回数据给发送者
	public String send( String text,String IP,int port) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket();
		String param="S";
		// 发送端
		try {
			// 发送的内容
			text="0x55 "+new HEX2And16().hex16tTo2(text)+"0xff";
			byte[] buf = text.getBytes();			
			// 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。		
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP),port);
			InetAddress address=InetAddress.getByName(IP);		
			// 从此套接字发送数据报包
			socket.send(packet);
			// 接收，接收者返回的数据
			// 关闭此数据报套接字。
			socket.close();
		} catch (SocketException e) {
			param=e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			param=e.getMessage();
		}
		System.out.println("---send---end-"+param);
		return param;
	}
	public static void main(String[] args) throws SocketException {
		String json="{"
				+ "\"zjbh\":\"hhhhhhhhhhhsss\""		
				+ "data:["
				+ "{\"num\":\"1\","
				+ "\"name\":\"A电源一\","
				+ "\"status\":\"1\"},"
				+ "{\"num\":\"2\","
				+ "\"name\":\"A电源二\","
				+ "\"status\":\"2\"}"		
				+ "]}";
json=new HEX2And16().hex16tTo2(json);
System.out.println(json);
		new Sender().send(json, "192.168.1.101", 8049);

	}
}