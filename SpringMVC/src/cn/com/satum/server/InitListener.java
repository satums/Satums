package cn.com.satum.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
public class InitListener implements ServletContextListener { 
public void contextDestroyed(ServletContextEvent context) {  
    
}  
static final int INPORT = 1712;
private byte[] buf = new byte[1000];
private DatagramPacket dp = new DatagramPacket(buf, buf.length);
private DatagramSocket socket;
@Override  
public void contextInitialized(ServletContextEvent context) {  
    // �����ĳ�ʼ��ִ��  
	System.out.println("3232222222222");
	
	 Runnable runnable = new Runnable() {  
         public void run() {  
             // task to run goes here  
        	 String str_send = "0132132132132";  
             byte[] buf = new byte[1024];  
             //�������3000�˿ڼ������յ�������  
             DatagramSocket ds = null;
        	try {
        		ds = new DatagramSocket(8049);
        	} catch (SocketException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}  
             //���մӿͻ��˷��͹���������  
             DatagramPacket dp_receive = new DatagramPacket(buf, 1024);  
             System.out.println("server is on��waiting for client to send data......");  
             boolean f = true;  
             while(f){  
                 //�������˽������Կͻ��˵�����  
                 try {
        			ds.receive(dp_receive);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}  
                 System.out.println("server received data from client��");  
                 String str_receive = new String(dp_receive.getData(),0,dp_receive.getLength()) +   
                         " from " + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();  
                 System.out.println(str_receive); 
                 //String str_send="�յ���������������������";
                 //���ݷ������ͻ��˵�3000�˿�  
                 DatagramPacket dp_send= new DatagramPacket(str_send.getBytes(),str_send.length(),dp_receive.getAddress(),9988);  
                 try {
        			ds.send(dp_send);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}  
                 //����dp_receive�ڽ���������֮�����ڲ���Ϣ����ֵ���Ϊʵ�ʽ��յ���Ϣ���ֽ�����  
                 //��������Ҫ��dp_receive���ڲ���Ϣ����������Ϊ1024  
                 dp_receive.setLength(1024);  
             }  
             ds.close();  
           
         }  
     };  
     ScheduledExecutorService service = Executors  
             .newSingleThreadScheduledExecutor();  
     // �ڶ�������Ϊ�״�ִ�е���ʱʱ�䣬����������Ϊ��ʱִ�еļ��ʱ��  
     service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);  
	
	
	
	
	 
}
}  



	  
  
 