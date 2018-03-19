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

import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.util.HEX2And16;
import cn.com.satum.util.Reciver;
import cn.com.satum.util.UDPRecive;
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
	System.out.println("���շ���������IPΪ����IP���˿�8049������");	
	 Runnable runnable = new Runnable() {  
         public void run() {   
        	//new Reciver().recive(); 
             // task to run goes here  
        	 String str_send =new HEX2And16().hex16tTo2("S");  
             byte[] buf = new byte[8192];  
             //�������3000�˿ڼ������յ�������  
             DatagramSocket ds = null;
        	try {
        		ds = new DatagramSocket(8049);
        	} catch (SocketException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} 
        	
        	
             //���մӿͻ��˷��͹���������  
        	
        	
             DatagramPacket dp_receive = new DatagramPacket(buf, buf.length);  
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
                 String ips=dp_receive.getAddress().getHostAddress();
                 int ports=dp_receive.getPort();
                 String data=new String(dp_receive.getData());
                 str_send=new DataUtil().dataParse(data,ips,ports);
                 System.out.println(str_send);
                 //�������ݷ��ؽ��յ�״̬
                 DatagramPacket dp_send= new DatagramPacket(str_send.getBytes(),str_send.length(),dp_receive.getAddress(),ports);  
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



	  
  
 