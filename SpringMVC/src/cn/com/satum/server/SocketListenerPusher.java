package cn.com.satum.server;

import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledThreadPoolExecutor;  
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;  
   
public class SocketListenerPusher implements Runnable {    
    private ServerSocket serverSocket;  
    private ExecutorService pool;  
      
      
    public SocketListenerPusher() {  
        int port = 0;  
        int poolsize = 0;  
          
        try {  
            port = 8090;  
            poolsize = 100;  
             System.out.println("======1111=============8090端口启动"); 
            serverSocket = new ServerSocket();  
            serverSocket.setReuseAddress(true);  
            serverSocket.bind(new InetSocketAddress(port));  
            pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * poolsize);  
              
            //下面两句循环执行run()方法, 相当于while(true){...}  
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());  
            executor.scheduleAtFixedRate(this, 1L, 1L, TimeUnit.MILLISECONDS);  
              
        } catch (Exception e) {  
           
            e.printStackTrace();  
       
        }  
  
    }  
      
    public void run() {  
        Socket socket = null;  
        try {  
            socket = serverSocket.accept(); 
            System.out.println("等待客户端链接中===============");
            pool.execute(new SocketListenerHandler(socket));  
        } catch (IOException e) {  
            System.out.println("线程池被关闭!!!!!!!!!!!");  
            pool.shutdown();  
           
            e.printStackTrace();  
        }  
          
    }
}
  
