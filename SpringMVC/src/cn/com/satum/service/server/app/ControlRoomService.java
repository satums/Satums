package cn.com.satum.service.server.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;

public class ControlRoomService implements AppService{
private static AppBo bo=new AppBo();
private final static String addjson="{"
		+ "\"flag\":\"add\","
		+ "\"user_code\":\"15738928228\","
		+ "\"floor_id\":\"1\","
		+ "\"floor_name\":\"1��\","
		+ "\"room_id\":\"\""
		+ "\"room_name\":\"����1\""		
		+ "}";
private final static String updtejson="{"
		+ "\"flag\":\"update\","
		+ "\"user_code\":\"15738928228\","
		+ "\"floor_id\":\"1\","
		+ "\"floor_name\":\"1��\","
		+ "\"room_id\":\"\""
		+ "\"room_name\":\"����1\""		
		+ "}";
private final static String saltjson="{"
		+ "\"flag\":\"salt\","
		+ "\"user_code\":\"15738928228\","
		+ "\"data\":[{"
		+ "\"room_id\":\"1\""
		+ "\"room_name\":\"����1\""
		+ "\"salt\":\"1\""
		+ "},{"
		+ "\"room_id\":\"2\""
		+ "\"room_name\":\"����1\""
		+ "\"salt\":\"2\""
		+ "}]"
		+ "}";
private final static String queryjosn="{\"flag\":\"query\",\"user_code\":\"15738928228\"}";
private final static String deletejosn="{\"flag\":\"delete\",\"user_code\":\"15738928228\",\"room_id\":\"1213131\"}";
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
		case "salt"://��������ͬʱҲ���ԶԷ�������ƽ����޸�
			rtg=RoomSalt(jsondata);
			break;
		}
		}catch(Exception e){
			rtg="{\"result\":\"E\",\"msg\":\"����json��ʽ����\"}";
		}		
		return rtg;
	}
	public static String RoomAdd(String jsondata){
		String rtg="";
		try{
		Map lmap=JSONObject.fromObject(jsondata);		
		String userCode=lmap.get("user_code").toString();
		String floor_id="";
		String floor_name="";
		if(lmap.get("floor_id")!=null){
			floor_id=lmap.get("floor_id").toString();
			floor_name=lmap.get("floor_name").toString();
		}
		String room_name=lmap.get("room_name").toString();
		System.out.println(jsondata);
		String soft=getSoft("sh_room", userCode);
		System.out.println(jsondata+"------------------");
		bo.runSQL("insert into sh_room (id,user_id,name,floor_id,floor_name,soft) values"
				+ "('"+new DataUtil().getUUID()+"','"+userCode+"','"+room_name+"','"+floor_id+"','"+floor_name+"','"+soft+"')");
		rtg="{\"result\":\"S\",\"msg\":\"������ӳɹ���\"}";
		}catch(Exception e){
			rtg="{\"result\":\"E\",\"msg\":\""+e.getMessage()+"\"}";
		}
		return rtg;
	}
	public static String RoomQuery(String jsondata){
		String rtg="";
		Map rmap=new HashMap();
		List list=new ArrayList();
		try{
		Map lmap=JSONObject.fromObject(jsondata);		
		String userCode=lmap.get("user_code").toString();
		list=bo.query("select id as room_id,name as room_name,floor_id,floor_name,soft as salt from sh_room where user_id='"+userCode+"' and is_del='2' order by CAST(soft AS SIGNED)");
		rmap.put("result","S");
		rmap.put("msg","��ȡ�����б�ɹ���");
		rmap.put("data",list);
		}catch(Exception e){		
			rmap.put("result","E");
			rmap.put("msg","ϵͳ����");
		}
		rtg=JSONObject.fromObject(rmap).toString();
		return rtg;
	}
	public static String RoomUpdate(String jsondata){
		String rtg="";
		try{
		Map lmap=JSONObject.fromObject(jsondata);		
		String userCode=lmap.get("user_code").toString();
		String floor_id="";
		String floor_name="";
		String room_id=lmap.get("room_id").toString();
		String room_name=lmap.get("room_name").toString();
		if(lmap.get("floor_id")!=null){
			floor_id=lmap.get("floor_id").toString();
			floor_name=lmap.get("floor_name").toString();
		}
		
		bo.runSQL("update sh_room set name='"+room_name+"',floor_id='"+floor_id+"',floor_name='"+floor_name+"' where id='"+room_id+"'");
		bo.runSQL("update sh_device set room_id='"+room_id+"',room_name='"+room_name+"' where room_id='"+room_id+"' and user_code='"+userCode+"'");
		rtg="{\"result\":\"S\",\"msg\":\"�����޸ĳɹ���\"}";
		}catch(Exception e){
			rtg="{\"result\":\"E\",\"msg\":\""+e.getMessage()+"\"}";
		}
		
		return rtg;	
	}
	public static String RoomSalt(String jsondata){
		Map rmap=new HashMap();
		
		try{
			Map reqMap=JSONObject.fromObject(jsondata);
			String userCode=(String)reqMap.get("user_code");
		List<Map<String, Object>> deviceList = (List<Map<String, Object>>) reqMap.get("data");// ��ȡ�����ڵ�ǰ�������豸�б�
		if (deviceList != null && deviceList.size() > 0) {
			for (Map<String, Object> deviceMap : deviceList) {
				String room_id = (String) deviceMap.get("room_id");// ����id
				String room_name = (String) deviceMap.get("room_name");// ��������
				String salt = (String) deviceMap.get("salt");//�����
				AppBo.runSQL("update sh_device set room_id='"+room_id+"',room_name='"+room_name+"' where room_id='"+room_id+"' and user_code='"+userCode+"'");

				AppBo.runSQL("update sh_room set soft='"+salt+"',name='"+room_name+"' where id='"+room_id+"'");
			}
		}
		rmap.put("result", "S");
		rmap.put("msg", "����ɹ�");
		}catch(Exception e){
			rmap.put("result", "E");
			rmap.put("msg", e.getMessage());
		}
		return JSONObject.fromObject(rmap).toString();
	}	
	public static String RoomDelete(String jsondata){
		Map lmap=new HashMap();
		try{
		Map rmap=JSONObject.fromObject(jsondata);
		String room_id=(String) rmap.get("room_id");
		String user_id=(String) rmap.get("user_code");
		AppBo.runSQL("update sh_room set is_del='1' where id='"+room_id+"',user_id='"+user_id+"'");
		AppBo.runSQL("updatea sh_device set room_id='',room_name='' where room_id='"+room_id+"' and user_code='"+user_id+"'");
		rmap.put("result", "S");
		rmap.put("msg", "ɾ���ɹ�");
		}catch(Exception e){
			lmap.put("result", "E");
			lmap.put("msg", e.getMessage());
		}
		return JSONObject.fromObject(lmap).toString();
	}	
public static String getSoft(String table, String userCode) {
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> softLists = AppBo
				.query("SELECT max(cast(soft as SIGNED INTEGER)) as soft  from " + table + " where is_del='2' AND user_id = '" + userCode + "' ");
	    Integer soft = 0;
	    if (softLists != null && softLists.size() > 0) { 
	    	for (Map<String, Object> softList : softLists) {
				String softs =softList.get("soft").toString();
				if(softs != null){					
					soft = Integer.parseInt(softs) + 1;
				}
		    }
	    }
		return soft.toString();

	}
}
