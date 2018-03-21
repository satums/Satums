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
    public static final int timeOut = 60*1000 ;  //���ö�ȡ�����쳣Ϊ10��  
    private final String dataRealTimeAction_id = "Agentdata_" + Math.random();  
      
    private static final String noData = "{'nodata':'������Ϣ'}";  
    private static final String errorData = "{'error':'�޷�����������'}";     
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
            connectedsocket.setSoTimeout(timeOut);  //��ʾ��������ʱ�ĵȴ���ʱ����, �˷��������ڽ�������֮ǰִ�в���Ч. ����, ���������� read()�����׳� SocketTimeoutException��, Socket��Ȼ�����ӵ�, ���Գ����ٴζ�����, ��λΪ����, ����Ĭ��ֵΪ 0(��ʾ�����޵ȴ�, ��Զ���ᳬʱ)  
            connectedsocket.setKeepAlive(true);   //��ʾ���ڳ�ʱ�䴦�ڿ���״̬��Socket, �Ƿ�Ҫ�Զ������ر�.  
            out = new DataOutputStream(connectedsocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connectedsocket.getInputStream())); 
            boolean flag=true;   
           //if (in.ready()) {  //�ж������Ƿ�������          	
                resultData = in.readLine().toString();                   
                if (resultData==null || "".equals(resultData)) {  
                	 out.writeBytes(errorData);
                } else if (resultData.charAt(0) != '{') {  //Ҫ�ڿͻ��˶�ʱά��������Ϣ  
                	 out.writeBytes(errorData);
                } else { 
                	String ips=connectedsocket.getInetAddress().toString();
                	int ports=connectedsocket.getPort();
                   // Map map=JSONObject.fromObject(resultData);
                  // String str= map.get("zjbh").toString(); 
                    System.out.println("��ȡ������----------------"+resultData); 
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
