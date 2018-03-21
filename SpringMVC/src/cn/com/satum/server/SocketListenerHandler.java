package cn.com.satum.server;

import java.io.BufferedInputStream;  
import java.io.BufferedReader;  
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.ObjectInputStream;  
import java.io.OutputStream;
import java.net.Socket;  
import java.sql.Connection;  
import java.sql.SQLException;  
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.service.server.util.TcpDataUtil;

import com.alibaba.fastjson.JSON;  

public class SocketListenerHandler implements Runnable {  
    public static final int timeOut = 60*1000 ;  //设置读取操作异常为10秒  
    private final String dataRealTimeAction_id = "Agentdata_" + Math.random();  
      
    private static final String noData = "{'nodata':'心跳信息'}";  
    private static final String errorData = "{'error':'无法解析的请求'}";     
    private Socket connectedsocket = null;  
      
    public SocketListenerHandler(Socket socket){  
        this.connectedsocket = socket;  
    }  
  
@Override  
    public void run() {  
        BufferedReader in = null;  
String resultData = "";  
DataOutputStream out = null;
        try {  
            connectedsocket.setSoTimeout(timeOut);  //表示接收数据时的等待超时数据, 此方法必须在接收数据之前执行才有效. 此外, 当输入流的 read()方法抛出 SocketTimeoutException后, Socket仍然是连接的, 可以尝试再次读数据, 单位为毫秒, 它的默认值为 0(表示会无限等待, 永远不会超时)  
            connectedsocket.setKeepAlive(true);   //表示对于长时间处于空闲状态的Socket, 是否要自动把它关闭.  
            out = new DataOutputStream(connectedsocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connectedsocket.getInputStream())); 
            boolean flag=true;   
           //if (in.ready()) {  //判断流中是否有数据          	
                resultData = in.readLine().toString();                   
                if (resultData==null || "".equals(resultData)) {  
                	 out.writeBytes(errorData);
                } else if (resultData.charAt(0) != '{') {  //要在客户端定时维持心跳信息  
                	 out.writeBytes(errorData);
                } else { 
                	String ips=connectedsocket.getInetAddress().toString();
                	int ports=connectedsocket.getPort();
                   // Map map=JSONObject.fromObject(resultData);
                  // String str= map.get("zjbh").toString(); 
                    System.out.println("读取数据中----------------"+resultData); 
                    String msg=new TcpDataUtil().dataParse(resultData,ips,ports);
                    out.writeBytes(msg);
                }   
           // }
           
} catch (IOException e) {  
           
            e.printStackTrace();  
        } catch (NumberFormatException e) {  
            
            e.printStackTrace();  
        } 
        /**
       finally {
        	
        	 if (out != null) {  
                 try {  
                     out.close();  
                 } catch (IOException e) {  
                 
                     e.printStackTrace();  
                 }
        	 }
        	
            if (in != null) {  
                try {  
                    in.close();  
                } catch (IOException e) {  
                
                    e.printStackTrace();  
                }  
            }  
}  **/
          
} }
