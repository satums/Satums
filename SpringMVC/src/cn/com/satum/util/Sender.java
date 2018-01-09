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
		this.socket = new DatagramSocket(8090);
		String param="S";
		// ���Ͷ�
		try {
			// ���͵�����
			
			byte[] buf = text.getBytes();			
			// �������ݱ���������������Ϊ length �İ����͵�ָ�������ϵ�ָ���˿ںš�		
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP),port);
			InetAddress address=InetAddress.getByName(IP);		
			// �Ӵ��׽��ַ������ݱ���
			socket.send(packet);
			// ���գ������߷��ص�����
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
		String json="{"
				+ "\"zjbh\":\"bbbbbbbbbbbbb1\","		
				+ "data:["
				+ "{\"num\":\"1\","
				+ "\"name\":\"A��Դһ\","
				+ "\"status\":\"1\"},"
				+ "{\"num\":\"2\","
				+ "\"name\":\"A��Դ��\","
				+ "\"status\":\"2\"}"		
				+ "]}";

System.out.println(json);
		new Sender().send(json, "192.168.1.102", 8049);

	}
}