package cn.com.satum.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
	DatagramSocket socket = null;

	// ������--->�ͻ��� �ͻ���--->������
	// �����߷����ͻ�������,�ͻ��˷������ݸ�������
	public String send( String text,String IP,int port) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket();
		String param="S";
		// ���Ͷ�
		try {
			// ���͵�����
//			text = "߮�����ļҡ�����!";
			text="0xAA"+text+"0X55";
			byte[] buf = text.getBytes();
			
			// �������ݱ���������������Ϊ length �İ����͵�ָ�������ϵ�ָ���˿ںš�
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP),port);
			InetAddress address=InetAddress.getByName(IP);
			System.out.println(address+"000000000000000-----------------");
			// �Ӵ��׽��ַ������ݱ���
			boolean f=socket.isConnected();
			boolean f1=socket.isClosed();
			socket.send(packet);
			// ���գ������߷��ص�����
//			displayReciveInfo(socket);
			// �رմ����ݱ��׽��֡�
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
		new Sender().send("ģ������1", "192.168.0.108", 8049);
	}
}