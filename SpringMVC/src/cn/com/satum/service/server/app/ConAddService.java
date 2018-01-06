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
	//second�Ǽ��������������ʱ����Դ����������ʱ��1-5�룬�����������ʱ��Ϊ60��
	if(!flag.equals("S")||second>65){
		flag="E";
		maps.put("message", "����������");
	}else{
		switch(mark){
		case "device":
			//�����豸����Ϣ������
			try {
				String json="{"		
						+ "\"code\":\"041b93cad51347c28f4c6126d5d6058f\","
						+ "\"status\":\"1\""
						+ "}";
				new Sender().send(json,IP,port);
				atm.DeviceUpdate(conID, status);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "scene":
			//�����龰����Ϣ������
			break;
		case "link":
			//������������Ϣ������
			break;
		
		}			
	}
			return JSONObject.fromObject(maps).toString();
		}

}
