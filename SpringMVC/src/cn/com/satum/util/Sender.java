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

	// 发送者--->客户端 客户端--->发送者
	// 发送者发给客户端数据,客户端返回数据给发送者
	public String send( String text,String IP,int port) throws SocketException {
		System.out.println("---send----");
		this.socket = new DatagramSocket(port);
		String param="S";
		// 发送端
		try {
			// 发送的内容
			
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
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		String json="{"
				+ "\"zjbh\":\"123456789aaaaaaa\"}\r\n";
		/**String json="{"
				+ "\"zjbh\":\"123456789aaaaaaa\","		
				+ "\"data\":["
				+ "{\"num\":\"1\","
				+ "\"name\":\"A电源一\","
				+ "\"status\":\"1\"},"
				+ "{\"num\":\"2\","
				+ "\"name\":\"A电源二\","
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
		   System.out.println("第"+(i+1)+"次连接成功！");
		  
		  //Thread.sleep(3000);
		//2、连接成功后，将形成数据通道，即输入和输出IO流，可以通过getInpuStream()和getOutputStream()方法获取IO流对象。  
	        OutputStream out = socket.getOutputStream();  
	          
	        //3、获取IO对象以后，就可以通过操作IO流来对数据进行读写。  
	        out.write(json.getBytes());  
	        br=new DataInputStream(socket.getInputStream());
		      // System.out.println(br.readUTF());
		        br.close();

		   socket.close();
		  }

	}
}