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
	public String send(String text,String IP,int port) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket(2);
		String param="S";
		// ���Ͷ�
		try {
			// ���͵�����
			text = "߮�����ļҡ�����!";
			byte[] buf = text.getBytes();
			// �������ݱ���������������Ϊ length �İ����͵�ָ�������ϵ�ָ���˿ںš�
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP),port);
			InetAddress address=InetAddress.getByName(IP);
			System.out.println(address+"000000000000000-----------------");
			// �Ӵ��׽��ַ������ݱ���
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
}