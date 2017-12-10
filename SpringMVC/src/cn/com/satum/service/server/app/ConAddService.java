package cn.com.satum.service.server.app;

import java.net.SocketException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.util.PostStyle;
import cn.com.satum.util.Sender;
public class ConAddService implements AppService {
	private final static String jsondata1="{"
			+ "\"flag\":\"device\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"A01\","
			+ "\"starts\":\"2\","
			+ "}";
	private final static String jsondata2="{"
			+ "\"flag\":\"scene\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"00001\","
			+ "\"starts\":\"2\","
			+ "}";
	private final static String jsondata3="{"
			+ "\"flag\":\"link\","
			+ "\"zjbh\":\"hhhhhhhhhhhsss\","
			+ "\"user_code\":\"15738928228\","
			+ "\"code\":\"001\","
			+ "\"starts\":\"2\","
			+ "}";
		public String getInfo(String jsonData) {
			AppBo appBo=new AppBo();
			Map map=JSONObject.fromObject(jsondata1);
			String mark=map.get("flag").toString();;
			String userid=map.get("user_code").toString();
			String conID=map.get("code").toString();
			String zjbh=map.get("zjbh").toString();
			String status=map.get("status").toString();
			String flag="S";
			Map maps=new HashMap();
			try {
				maps=new DataUtil().dataQuery(zjbh,jsonData);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String IP=maps.get("ip").toString();
			int second=Integer.valueOf(maps.get("second").toString());
			int port=Integer.valueOf(maps.get("port").toString());	
	try {
		flag=new Sender().send(jsonData,IP,port);
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//second是检测主机心跳检测的时间可以大于心跳检测时间1-5秒，假设心跳检测时间为60秒
	if(!flag.equals("S")||second>65){
		flag="E";
	}else{
		switch(mark){
		case "device":
			flag=new DataUtil().ControllerDevice(maps,userid, conID, status);
			break;
		case "scene":
			flag=new DataUtil().ControllerScene(maps,userid, conID, status);
			break;
		case "link":
			flag=new DataUtil().ControllerDevice(maps,userid, conID, status);
			break;
		
		}			
	}
			return new PostStyle().getBase64(flag);
		}

}
