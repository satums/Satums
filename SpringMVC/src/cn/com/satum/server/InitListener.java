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
    // 上下文初始化执行  
	System.out.println("3232222222222");
	
	 Runnable runnable = new Runnable() {  
         public void run() {  
             // task to run goes here  
        	 String str_send = "0132132132132";  
             byte[] buf = new byte[1024];  
             //服务端在3000端口监听接收到的数据  
             DatagramSocket ds = null;
        	try {
        		ds = new DatagramSocket(8049);
        	} catch (SocketException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}  
             //接收从客户端发送过来的数据  
             DatagramPacket dp_receive = new DatagramPacket(buf, 1024);  
             System.out.println("server is on，waiting for client to send data......");  
             boolean f = true;  
             while(f){  
                 //服务器端接收来自客户端的数据  
                 try {
        			ds.receive(dp_receive);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}  
                 System.out.println("server received data from client：");  
                 String str_receive = new String(dp_receive.getData(),0,dp_receive.getLength()) +   
                         " from " + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();  
                 System.out.println(str_receive); 
                 //String str_send="收到。。。。。。。。。。";
                 //数据发动到客户端的3000端口  
                 DatagramPacket dp_send= new DatagramPacket(str_send.getBytes(),str_send.length(),dp_receive.getAddress(),9988);  
                 try {
        			ds.send(dp_send);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}  
                 //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，  
                 //所以这里要将dp_receive的内部消息长度重新置为1024  
                 dp_receive.setLength(1024);  
             }  
             ds.close();  
           
         }  
     };  
     ScheduledExecutorService service = Executors  
             .newSingleThreadScheduledExecutor();  
     // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
     service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);  
	
	
	
	
	 
}
}  



	  
  
 