package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class LinkService  implements AppService{
	/**
	 * 
	 * 联动设置
	 * 
	 * */
	
	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String device_code="";
	private static String device_name="";
	private static String scene_code="";
	private static String scene_name="";
	private static String vir_code="";
	private static String vir_name="";
	private static String time_code="";
	private static String time_name="";
	private static String status="1";
	private static String controller="0";
	private static String flag="S";
	private static String msg="成功";
	private static String sqls="select * from sh_common_link ";
	private final static String jsondata="{"
			+ "\"flag\":\"add\","
			+ "\"user_code\":\"15738928228\","
			+ "\"link_code\":\"003\","
			+ "\"link_name\":\"温度控制联动\","
			+ "\"status\":\"1\","
			+ "\"device\":[{"			
			+ "\"device_code\":\"003\","
			+ "\"device_name\":\"A电源三\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"device_code\":\"004\","
			+ "\"device_name\":\"A电源四\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}],"
			+ "\"scene\":[{"			
			+ "\"scene_code\":\"00001\","
			+ "\"scene_name\":\"回家模式\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"scene_code\":\"00002\","
			+ "\"scene_name\":\"离家模式\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}],"
			+ "\"term\":[{"			
			+ "\"vir_code\":\"001\","
			+ "\"vir_name\":\"温度\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"vir_code\":\"002\","
			+ "\"vir_name\":\"湿度\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}],"
			+ "\"time\":[{"			
			+ "\"time_code\":\"001\","
			+ "\"time_name\":\"中午\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"time_code\":\"002\","
			+ "\"time_name\":\"下午\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}]"
			+ "}";
/**
 * 
 * 场景设置
 * 
 * */
 
	@Override	
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		
		Map map=new  HashMap();
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");
		String user_code=(String)lmap.get("user_code");
		String link_code=(String)lmap.get("link_code");
		String link_name=(String)lmap.get("link_name");
		JSONArray device=JSONObject.fromObject(jsondata).getJSONArray("device");
		Object[] devices=device.toArray();		
		JSONArray scene=JSONObject.fromObject(jsondata).getJSONArray("scene");
		Object[] scenes=scene.toArray();
		JSONArray term=JSONObject.fromObject(jsondata).getJSONArray("device");
		Object[] terms=term.toArray();		
		JSONArray time=JSONObject.fromObject(jsondata).getJSONArray("scene");
		Object[] times=time.toArray();
		if(mark.equals("add")){
			map=add(link_code,link_name,user_code,devices,scenes,terms,times);
		}else if(mark.equals("update")){
			map=update(link_code,devices,scenes,terms,times);
		}else{
			map=delete(user_code,link_code,devices,scenes,terms,times);
		}		
			JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
	public static Map update(String link_code,Object[] devices,Object[] scenes,Object[] terms,Object[] times){	
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where link_code='"+link_code+"'");
		if(lists.size()<1){
			flag="E";
			msg="联动不存在，请先添加联动！";
		}else{
			flag="S";
			msg="成功";
		}
		try{
		//更新联动设备子表
		for(int i=0;i<devices.length;i++){
			Map dmap=(Map)devices[i];
			device_code=(String)dmap.get("device_code");
			device_name=(String)dmap.get("device_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_link_devicesub where link_code='"+link_code+"' and device_code='"+device_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_link_devicesub set status='"+status+"',contime='"+controller+"'");	
			}else{
				appBo.runSQL("insert into sh_link_devicesub (id,link_code,device_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"')");	
			}			
		} 
		//更新联动情景子表
		for(int i=0;i<devices.length;i++){
			Map dmap=(Map)devices[i];
			scene_code=(String)dmap.get("scene_code");
			scene_name=(String)dmap.get("scene_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_link_scenesub where link_code='"+link_code+"' and scene_code='"+scene_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_link_scenesub set status='"+status+"',contime='"+controller+"'");	
			}else{
				appBo.runSQL("insert into sh_link_scenesub (id,link_code,scene_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+scene_code+"','"+scene_name+"','"+status+"','"+controller+"')");	
			}
		}
		//更新联动条件子表
				for(int i=0;i<devices.length;i++){
					Map dmap=(Map)devices[i];
					vir_code=(String)dmap.get("vir_code");
					vir_name=(String)dmap.get("vir_name");
					status=(String)dmap.get("status");
					controller=(String)dmap.get("controller");
					String sql="select id from sh_link_virsub where scene_code='"+link_code+"' and link_code='"+link_code+"'";
					List list=appBo.query(sql);
					if(list.size()>0){
						appBo.runSQL("update sh_link_virsub set status='"+status+"',contime='"+controller+"'");	
					}else{
						appBo.runSQL("insert into sh_link_virsub (id,link_code,vir_code,name,status,contime) values"
								+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+vir_code+"','"+vir_name+"','"+status+"','"+controller+"')");	
					}
				}
				//更新联动时间子表
				for(int i=0;i<devices.length;i++){
					Map dmap=(Map)devices[i];
					time_code=(String)dmap.get("time_code");
					time_name=(String)dmap.get("time_name");
					status=(String)dmap.get("status");
					controller=(String)dmap.get("controller");
					String sql="select id from sh_link_timesub where scene_code='"+link_code+"' and link_code='"+link_code+"'";
					List list=appBo.query(sql);
					if(list.size()>0){
						appBo.runSQL("update sh_link_timesub set status='"+status+"',contime='"+controller+"'");	
					}else{
						appBo.runSQL("insert into sh_link_timesub (id,link_code,time_code,name,status,contime) values"
								+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+time_code+"','"+time_name+"','"+status+"','"+controller+"')");	
					}
				}
				flag="S";
				msg="成功";
		}catch(Exception e){
			flag="E";
			msg=e.getMessage();
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	public static Map add(String link_code,String link_name,String user_code,Object[] devices,Object[] scenes,Object[] terms,Object[] times){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where link_code='"+link_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="联动已存在，请对联动进行编辑！";
		}else{
			try{
			appBo.runSQL("insert into sh_common_link (id,link_code,user_code,name) values"
					+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+user_code+"','"+link_name+"')");
			//设备子表配置
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				device_code=(String)dmap.get("device_code");
				device_name=(String)dmap.get("device_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_link_devicesub (id,link_code,device_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"')");					
			} 
			//情景子表配置
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				scene_code=(String)dmap.get("scene_code");
				scene_name=(String)dmap.get("scene_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_link_scenesub (id,link_code,scene_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+scene_code+"','"+scene_name+"','"+status+"','"+controller+"')");		
			}
			//条件子表配置
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				vir_code=(String)dmap.get("vir_code");
				vir_name=(String)dmap.get("vir_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_link_virsub (id,link_code,vir_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+vir_code+"','"+vir_name+"','"+status+"','"+controller+"')");		
			}
			//时间子表配置
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				time_code=(String)dmap.get("time_code");
				time_name=(String)dmap.get("time_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_link_timesub (id,link_code,time_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+link_code+"','"+time_code+"','"+time_name+"','"+status+"','"+controller+"')");		
			}
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
	public static Map delete(String user_code,String link_code,Object[] devices,Object[] scenes,Object[] terms,Object[] times){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where link_code='"+scene_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="场景不存在，请确认！";
		}else{
			
				flag="S";
				msg="成功";		
			appBo.runSQL("update sh_common_link set is_del='1' where user_code='"+user_code+"' and link_code='"+link_code+"'");	
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
}
