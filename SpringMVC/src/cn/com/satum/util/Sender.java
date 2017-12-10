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
//			text = "弋辛博的家。。。!";
			text="0xAA"+text+"0X55";
			byte[] buf = text.getBytes();
			
			// 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP),port);
			InetAddress address=InetAddress.getByName(IP);
			System.out.println(address+"000000000000000-----------------");
			// 从此套接字发送数据报包
			boolean f=socket.isConnected();
			boolean f1=socket.isClosed();
			socket.send(packet);
			// 接收，接收者返回的数据
//			displayReciveInfo(socket);
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
		new Sender().send("模拟主机1", "192.168.0.108", 8049);
	}
}