package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;

public class ControlRoom implements AppService{
private AppBo bo=new AppBo();
private final static String json="{"
		+ "\"flag\":\"add\","
		+ "\"user_code\":\"15738928228\","
		+ "\"floor_data\":[{"
		+ "\"floor_id\":\"1\","
		+ "\"floor_name\":\"1��\","
		+ "\"room_data\":[{"
		+ "\"room_id\":\"1\""
		+ "\"room_name\":\"����1\""
		+ "},{"
		+ "\"room_id\":\"2\""
		+ "\"room_name\":\"����1\""
		+ "}]"
		+ "},{"
		+ "\"floor_id\":\"2\","
		+ "\"floor_name\":\"2��\","
		+ "\"room_data\":[{"
		+ "\"room_id\":\"1\""
		+ "\"room_name\":\"����2\""
		+ "},{"
		+ "\"room_id\":\"2\""
		+ "\"room_name\":\"����2\""
		+ "}]"
		+ "}]"
		+ "}";
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map map=new  HashMap();
		String rtg="";
		try{
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");		
		switch(mark){
		case "add":
			rtg=RoomAdd(jsondata);
			break;
		case "query":
			rtg=RoomQuery(jsondata);
			break;
		case "update"://���������豸����������Ϣ
			rtg=RoomUpdate(jsondata);
			break;
		case "delete"://ɾ�������豸�����ķ���
			rtg=RoomDelete(jsondata);
			break;
		}
		}catch(Exception e){
			rtg="{\"result\":\"E\",\"msg\":\"����json��ʽ����\"}";
		}
		
		return rtg;
	}
	public static String RoomAdd(String jsondata){
		Map lmap=JSONObject.fromObject(jsondata);
		
		JSONArray device=JSONObject.fromObject(jsondata).getJSONArray("floor_data");
		Object[] devices=device.toArray();	
		return null;
	}
	public static String RoomQuery(String jsondata){
		return null;
	}
	public static String RoomUpdate(String jsondata){
		return null;
	}
	public static String RoomDelete(String jsondata){
		return null;
	}
}
