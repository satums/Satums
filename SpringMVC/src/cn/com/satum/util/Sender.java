package cn.com.satum.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sender {
	DatagramSocket socket = null;

	// ������--->�ͻ��� �ͻ���--->������
	// �����߷����ͻ�������,�ͻ��˷������ݸ�������
	public String send( String text,String IP,int port) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket(port);
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
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		String json="{"
				+ "\"zjbh\":\"123456789aaaaaaa\"}\r\n";
		/**String json="{"
				+ "\"zjbh\":\"123456789aaaaaaa\","		
				+ "\"data\":["
				+ "{\"num\":\"1\","
				+ "\"name\":\"A��Դһ\","
				+ "\"status\":\"1\"},"
				+ "{\"num\":\"2\","
				+ "\"name\":\"A��Դ��\","
				+ "\"status\":\"2\"}"		
				+ "]}\r\n";
**/
//System.out.println(json);
		//new Sender().send(json, "47.93.47.199",8049);
		
		final int length = 2;
		  String host = "47.93.47.199";
		  int port = 8089;
		  //Socket[] socket = new Socket[length];
		  for(int i = 0;i<length;i++){
			  DataInputStream br=null;

			  Socket socket = new Socket(host,port);
		   System.out.println("��"+(i+1)+"�����ӳɹ���");
		  
		  //Thread.sleep(3000);
		//2�����ӳɹ��󣬽��γ�����ͨ��������������IO��������ͨ��getInpuStream()��getOutputStream()������ȡIO������  
	        OutputStream out = socket.getOutputStream();  
	          
	        //3����ȡIO�����Ժ󣬾Ϳ���ͨ������IO���������ݽ��ж�д��  
	        out.write(json.getBytes());  
	        br=new DataInputStream(socket.getInputStream());
		      // System.out.println(br.readUTF());
		        br.close();

		   socket.close();
		  }

	}
}