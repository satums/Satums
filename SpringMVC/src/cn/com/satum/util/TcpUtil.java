package cn.com.satum.util;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.service.server.util.TcpDataUtil;

public class TcpUtil {
	private AppBo appBo=new AppBo();
public String setNetInfo(String type,String contype,String usercode,String zjbh,List list) {
	String msg="";
	Map maps=new HashMap();
	maps.put("flag",type);
	maps.put("type",contype);
	maps.put("zjbh", zjbh);
	maps.put("data", list);
	JSONObject json=JSONObject.fromObject(maps);
	Map mapm=new HashMap();
	try {
		maps=new DataUtil().dataQuery(zjbh,usercode);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String IP=maps.get("ip").toString();
	String flag=maps.get("result").toString();
	String message=maps.get("message").toString();
	int second=Integer.valueOf(maps.get("second").toString());
	int port=Integer.valueOf(maps.get("port").toString());	
	send(json.toString(),IP,port);
	
	System.out.println("发送消息成功");
	return msg;
}
public static void send(String json,String ip,int port){
	  //Socket[] socket = new Socket[length];
	json=json+"\r\n";
	System.out.println("json---------"+json);
	System.out.println("ip---------"+ip);
	System.out.println("port---------"+port);
		  DataInputStream br=null;
		  try{
		  Socket socket = new Socket(ip,port);
	  //Thread.sleep(3000);
	//2、连接成功后，将形成数据通道，即输入和输出IO流，可以通过getInpuStream()和getOutputStream()方法获取IO流对象。  
      OutputStream out = socket.getOutputStream();          
      //3、获取IO对象以后，就可以通过操作IO流来对数据进行读写。  
      out.write(json.getBytes());  
      br=new DataInputStream(socket.getInputStream());
	      // System.out.println(br.readUTF());
	        br.close();
	   socket.close();
		  }catch(Exception e){
			  e.printStackTrace();
		   }
	   
	  }
}
