package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class DeviceService implements AppService{
	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String device_type_id="";
	private static String device_type_name="";
	private static String num="";
	private static String name="";
	private static String status="";
	private static String flag="S";
	private static String msg="成功";
	private static String sqls="select * from sh_device ";
	private final static String jsondata="{"
			+ "\"flag\":\"add\","
			+ "\"user_code\":\"15738928228\","
			+ "\"device_type_id\":\"A\","
			+ "\"device_type_name\":\"A类性\","
			+ "\"num\":\"A01\","
			+ "\"name\":\"A电源一\","
			+ "\"status\":\"1\","
			+ "}";
	/**
	 * 
	 * 设备设置
	 * 
	 * */
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		
		Map map=new  HashMap();
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");
		String user_code=(String)lmap.get("user_code");
		device_type_id=(String)lmap.get("device_type_id");
		device_type_name=(String)lmap.get("device_type_name");
		num=(String)lmap.get("num");
		name=(String)lmap.get("name");
		if(mark.equals("add")){
			map=add(user_code);
		}else{
			map=delete(user_code);
		}		
			JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
	public static Map add(String user_code){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where user_code='"+user_code+"' and num='"+num+"'");
		if(lists.size()>0){
			flag="E";
			msg="设备编号已存在，请对设备进行编辑！";
		}else{
			try{
			appBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,status) values "
					+ "('"+DataUtil.getUUID()+"','"+user_code+"','"+device_type_id+"','"+device_type_name+"','"+num+"','"+name+"','"+status+"')");			
			flag="S";
			msg="成功";
			}catch(Exception e){
				flag="E";
				msg=e.getMessage();
			}
			
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	public static Map delete(String user_code){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where user_code='"+user_code+"' and num='"+num+"'");
		if(lists.size()>0){
			try{
			appBo.runSQL("update sh_device set is_del='1' where num='"+num+"' and user_code='"+user_code+"'");		
			flag="S";
			msg="成功";			
			}catch(Exception e){
			flag="E";
			msg=e.getMessage();
			}
			
		}else{	
			flag="E";
			msg="设备不存在，请确认！";
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
}
