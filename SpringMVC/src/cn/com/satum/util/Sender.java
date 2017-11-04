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
	public String send(String text) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket(2);
		String param="S";
		// 发送端
		try {

			// 创建发送方的套接字 对象 采用9004默认端口号

			// 发送的内容

			text = "弋辛博的家。。。!";
			byte[] buf = text.getBytes();

			// 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.107"),1);
			// 从此套接字发送数据报包
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
		System.out.println("---send---end-");
		return param;
	}


	public static void main(String[] args) throws SocketException {
		String pam=new Sender().send("");
		System.out.println(pam+"============");
	}
}