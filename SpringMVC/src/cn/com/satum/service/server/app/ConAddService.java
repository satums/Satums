package cn.com.satum.service.server.app;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.util.UdpUtil;
public class ConAddService implements AppService {
	private static AppToMaster atm=new AppToMaster();
	private final static String jsondata1="{"
			+ "\"flag\":\"device\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"041b93cad51347c28f4c6126d5d6058f\","
			+ "\"status\":\"2\","
			+ "}";
	private final static String jsondata2="{"
			+ "\"flag\":\"scene\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"00001\","
			+ "\"status\":\"2\","
			+ "}";
	private final static String jsondata3="{"
			+ "\"flag\":\"link\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"001\","
			+ "\"status\":\"2\","
			+ "}";
		public String getInfo(String jsonData) {
			AppBo appBo=new AppBo();
			Map map=JSONObject.fromObject(jsonData);
			String mark=map.get("flag").toString();;
			String userid=map.get("user_code").toString();
			String conID=map.get("code").toString();
			String zjbh=map.get("zjbh").toString();
			String status=map.get("status").toString();
			String flag="S";
			Map maps=new HashMap();
			Map mapm=new HashMap();
			try {
				maps=new DataUtil().dataQuery(zjbh,userid);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String IP=maps.get("ip").toString();
			flag=maps.get("result").toString();
			String message=maps.get("message").toString();
			int second=Integer.valueOf(maps.get("second").toString());
			int port=Integer.valueOf(maps.get("port").toString());	
			/**
	try {
		flag=new Sender().send(jsonData,IP,port);
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}**/
	//second是检测主机心跳检测的时间可以大于心跳检测时间1-5秒，假设心跳检测时间为60秒
	if(!flag.equals("S")||second>65){
		flag="E";
		mapm.put("message", "主机不在线");
	}else{
		switch(mark){
		case "device":
			String msg=new UdpUtil().getNetInfo(userid,zjbh,conID,status,IP,port);
			mapm.put("message",msg);
			break;
		case "scene":
			//发送情景的信息至主机
			break;
		case "link":
			//发送联动的信息至主机
			break;
		
		}			
	}
			return JSONObject.fromObject(mapm).toString();
		}

}
