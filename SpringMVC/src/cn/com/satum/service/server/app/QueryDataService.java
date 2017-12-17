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

public class QueryDataService implements AppService{
	private final static String jsondata="{"
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
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		
		AppBo appBo=new AppBo();
		Map map=JSONObject.fromObject(jsondata);
		String mark=map.get("flag").toString();;
		String userid=map.get("user_code").toString();
		String conID=map.get("code").toString();
		String zjbh=map.get("zjbh").toString();
		String status=map.get("status").toString();
		String flag="S";

//second是检测主机心跳检测的时间可以大于心跳检测时间1-5秒，假设心跳检测时间为60秒

	switch(mark){
	case "device":
		flag=new QueryDevice().getInfo(jsondata);
		break;
	case "scene":
		flag=new QueryScene().getInfo(jsondata);
		break;
	case "link":
		flag=new QueryLink().getInfo(jsondata);
		break;
	
	}			

		return new PostStyle().getBase64(flag);
	}

}
