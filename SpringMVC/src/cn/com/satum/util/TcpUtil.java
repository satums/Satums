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
	
	System.out.println("������Ϣ�ɹ�");
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
	//2�����ӳɹ��󣬽��γ�����ͨ��������������IO��������ͨ��getInpuStream()��getOutputStream()������ȡIO������  
      OutputStream out = socket.getOutputStream();          
      //3����ȡIO�����Ժ󣬾Ϳ���ͨ������IO���������ݽ��ж�д��  
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
