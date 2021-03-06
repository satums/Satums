package cn.com.satum.service.server.app;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sun.corba.se.pept.broker.Broker;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

/**
 * @author lwf
 * 情景增、删、改、查
 */
public class SceneControlService implements AppService {

	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String device_code="";
	private static String device_name="";
	private static String link_code="";
	private static String link_name="";
	private static String status="";
	private static String controller="";
	private static String flag="S";
	private static String msg="成功";
	private static String sqls="select * from sh_common_scene";
	private final static String jsondata="{"
			+ "\"flag\":\"add\","
			+ "\"user_code\":\"15738928228\","
			+ "\"scene_name\":\"回家模式\","
			+ "\"device\":[{"			
			+ "\"device_code\":\"001\","
			+ "\"device_name\":\"A电源一\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"device_code\":\"002\","
			+ "\"device_name\":\"A电源二\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}],"
			+ "\"link\":[{"			
			+ "\"link_code\":\"001\","
			+ "\"link_name\":\"联动一\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"link_code\":\"002\","
			+ "\"link_name\":\"联动二\","
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
		String scene_code=DataUtil.getUUID();
		String scene_name=(String)lmap.get("scene_name");
		if(mark.equals("add")||mark.equals("update")){
		JSONArray device=JSONObject.fromObject(jsondata).getJSONArray("device");
		Object[] devices=device.toArray();		
		JSONArray link=JSONObject.fromObject(jsondata).getJSONArray("link");
		Object[] links=link.toArray();
		if(mark.equals("add")){
			map=add(scene_code,scene_name,user_code,devices,links);
		}else if(mark.equals("update")){
			scene_code=(String)lmap.get("scene_code");
			map=update(scene_code,user_code,devices,links);
		}
		}else{
		if(mark.equals("delete")){
			scene_code=(String)lmap.get("scene_code");
			map=delete(user_code,scene_code);
		}else if(mark.equals("query")){
			scene_code=(String)lmap.get("scene_code");
			String type = (String)lmap.get("type");
			if("1".equals(type)){
				map=queryScene(user_code);// 查询情景
			}
			else if("2".equals(type)){
				map=queryDevice(scene_code);// 查询设备
			}
			else if("3".equals(type)){
				map=queryLink(scene_code);// 查询联动
			}
			
		}
		}
		JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
	public static Map update(String scene_code,String user_code,Object[] devices,Object[] links){	
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where is_del='2' and  scene_code='"+scene_code+"' and user_code='"+user_code+"' ");
		if(lists.size()<1){
			flag="E";
			msg="场景不存在，请先添加场景！";
		}else{
			flag="S";
			msg="成功";
		}
		try{
		//更新情景设备子表
		for(int i=0;i<devices.length;i++){
			Map dmap=(Map)devices[i];
			device_code=(String)dmap.get("device_code");
			device_name=(String)dmap.get("device_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_scene_devicesub where is_del='2' and scene_code='"+scene_code+"' and device_code='"+device_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_scene_devicesub set status='"+status+"',contime='"+controller+"' where scene_code='"+scene_code+"' and device_code='"+device_code+"'");	
			}else{
				appBo.runSQL("insert into sh_scene_devicesub (id,scene_code,device_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"')");	
			}
			
			//查询出此情景下所有设备
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> deviceLists = appBo
					.query("SELECT device_code from sh_scene_devicesub where is_del='2' and  scene_code='"+scene_code+"'");
			
		
			if (deviceLists != null && deviceLists.size() > 0) {
				//遍历情景下所有设备
				for (Map<String, Object> deviceList : deviceLists) {
					
					String deviceCode = (String) deviceList.get("device_code");//设备code
					//遍历接口传过来的设备
					for(int j=0;j<devices.length;j++){
						Boolean falg=true;
						Map dmaps=(Map)devices[j];
						device_code=(String)dmaps.get("device_code");
						if(device_code.equals(deviceCode)){
							//若device_code存在，则将falg=false
							falg = false;
							
						}else{
							//若falg=true device_code不存在，删除之							
								appBo.runSQL("update sh_scene_devicesub set is_del='1' where scene_code='"+scene_code+"' and device_code='"+deviceCode+"'");
							
						}
						
					}	
				} 
			}
		}
		
		for(int i=0;i<links.length;i++){
			Map dmap=(Map)links[i];
			link_code=(String)dmap.get("link_code");
			link_name=(String)dmap.get("link_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_scene_linksub where is_del='2' and scene_code='"+scene_code+"' and link_code='"+link_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_scene_linksub set status='"+status+"',contime='"+controller+"'  where scene_code='"+scene_code+"' and link_code='"+link_code+"'");	
			}else{
				appBo.runSQL("insert into sh_scene_linksub (id,scene_code,link_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+link_code+"','"+link_name+"','"+status+"','"+controller+"')");	
			}
			
			//查询出此情景下所有联动
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> linkLists = appBo
					.query("SELECT link_code from sh_scene_linksub where is_del='2' and  scene_code='"+scene_code+"'");
			
			Boolean falg=true;
			if (linkLists != null && linkLists.size() > 0) {
				//遍历情景下所有设备
				for (Map<String, Object> linkList : linkLists) {
					
					String linkCode = (String) linkList.get("link_code");//联动code
					//遍历接口传过来的设备
					for(int j=0;j<links.length;j++){
						Map dmaps=(Map)links[j];
						link_code=(String)dmaps.get("link_code");
						if(link_code.equals(linkCode)){
							//若link_code存在，则将falg=false
							falg = false;
							
						}else{
							//若falg=true link_code不存在，删除之
							appBo.runSQL("update sh_scene_linksub set is_del='1' where scene_code='"+scene_code+"' and link_code='"+linkCode+"'");

						}						
					}								
				} 
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
	public static Map add(String scene_code,String scene_name,String user_code,Object[] devices,Object[] links){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where is_del='2' and scene_code='"+scene_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="场景已存在，请对场景进行编辑！";
		}else{
			try{
			appBo.runSQL("insert into sh_common_scene (id,scene_code,user_code,name) values"
					+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+user_code+"','"+scene_name+"')");
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				device_code=(String)dmap.get("device_code");
				device_name=(String)dmap.get("device_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_scene_devicesub (id,scene_code,device_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"')");					
			} 
			for(int i=0;i<links.length;i++){
				Map dmap=(Map)links[i];
				link_code=(String)dmap.get("link_code");
				link_name=(String)dmap.get("link_name");		
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_scene_linksub (id,scene_code,link_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+link_code+"','"+link_name+"','"+status+"','"+controller+"')");		
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
	public static Map delete(String user_code,String scene_code){
		Map map=new HashMap();
		List lists=appBo.query(sqls+" where is_del='2' and scene_code='"+scene_code+"' and user_code='"+user_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="场景不存在，请确认！";
		}else{
			appBo.runSQL("update sh_common_scene set is_del='1' where user_code='"+user_code+"' and  scene_code='"+scene_code+"'");	
			appBo.runSQL("update sh_scene_devicesub set is_del='1' where scene_code='"+scene_code+"'");	
			appBo.runSQL("update sh_scene_linksub set is_del='1' where scene_code='"+scene_code+"'");	
				flag="S";
				msg="成功";	
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	
	/**
	 * 查询场景
	 * 
	 * @return
	 */
	public static Map queryScene(String user_code) {

		Map map=new HashMap();
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> sceneList = AppBo
				.query("SELECT scene_code,name,status,user_code FROM sh_common_scene WHERE is_del='2' AND user_code='"
						+ user_code + "' ");
		
		if (sceneList.size()==0)  {
			map.put("result", "E");
			map.put("msg", "未查询到场景！");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "获取场景列表成功！");
		map.put("data", sceneList);
		return map;

	}
	
	/**
	 * 查询设备
	 * 
	 * @return
	 */
	public static Map queryDevice(String scene_code) {

		Map map=new HashMap();
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT scene_code,name,device_code,status,contime FROM sh_scene_devicesub WHERE is_del='2' AND scene_code='"
						+ scene_code + "' ");
		
		if (deviceList.size()==0)  {
			map.put("result", "E");
			map.put("msg", "未查询到设备！");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "获取设备列表成功！");
		map.put("data", deviceList);
		return map;

	}
	
	/**
	 * 查询联动
	 * 
	 * @return
	 */
	public static Map queryLink(String scene_code) {

		Map map=new HashMap();
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scene_code,name,link_code,status,contime FROM sh_scene_linksub WHERE is_del='2' AND scene_code='"
						+ scene_code + "' ");
		
		if (linkList.size()==0)  {
			map.put("result", "E");
			map.put("msg", "未查询到联动！");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "获取联动列表成功！");
		map.put("data", linkList);
		return map;

	}
}
