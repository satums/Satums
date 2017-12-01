package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SceneService  implements AppService{
	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String device_code="";
	private static String device_name="";
	private static String link_code="";
	private static String link_name="";
	private static String status="";
	private static String controller="";
	private static String flag="S";
	private static String msg="�ɹ�";
	private static String sqls="select * from sh_common_scene";
	private final static String jsondata="{"
			+ "\"flag\":\"add\","
			+ "\"user_code\":\"15738928228\","
			+ "\"scene_code\":\"00001\","
			+ "\"scene_name\":\"�ؼ�ģʽ\","
			+ "\"device\":[{"			
			+ "\"device_code\":\"001\","
			+ "\"device_name\":\"A��Դһ\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"device_code\":\"002\","
			+ "\"device_name\":\"A��Դ��\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}],"
			+ "\"link\":[{"			
			+ "\"link_code\":\"001\","
			+ "\"link_name\":\"����һ\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "},{"
			+ "\"link_code\":\"002\","
			+ "\"link_name\":\"������\","
			+ "\"status\":\"1\","
			+ "\"controller\":\"0\""
			+ "}]"
			+ "}";
/**
 * 
 * ��������
 * 
 * */
 
	@Override	
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		

		Map map=new  HashMap();
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");
		String user_code=(String)lmap.get("uer_code");
		String scene_code=(String)lmap.get("scene_code");
		String scene_name=(String)lmap.get("scene_name");
		JSONArray device=JSONObject.fromObject(jsondata).getJSONArray("device");
		Object[] devices=device.toArray();		
		JSONArray link=JSONObject.fromObject(jsondata).getJSONArray("link");
		Object[] links=link.toArray();
		if(mark.equals("add")){
			map=add(scene_code,user_code,devices,links);
		}else if(mark.equals("update")){
			map=update(scene_code,devices,links);
		}else{
			map=delete(scene_code,devices,links);
		}		
			JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
	public static Map update(String scene_code,Object[] devices,Object[] links){	
		Map map=new HashMap();
		List lists=appBo.query(sqls+"where scene_code='"+scene_code+"'");
		if(lists.size()<1){
			flag="E";
			msg="���������ڣ�������ӳ�����";
		}
		//�����龰�豸�ӱ�
		for(int i=0;i<devices.length;i++){
			Map dmap=(Map)devices[i];
			device_code=(String)dmap.get("device_code");
			device_name=(String)dmap.get("device_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_scene_devicesub where scene_code='"+scene_code+"' and device_code='"+device_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_scene_devicesub set status='"+status+"',contime='"+controller+"'");	
			}else{
				appBo.runSQL("insert into sh_scene_devicesub (id,scene_code,device_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"'");	
			}			
		} 
		for(int i=0;i<devices.length;i++){
			Map dmap=(Map)devices[i];
			link_code=(String)dmap.get("link_code");
			link_name=(String)dmap.get("link_name");
			status=(String)dmap.get("status");
			controller=(String)dmap.get("controller");
			String sql="select id from sh_scene_linksub where scene_code='"+scene_code+"' and link_code='"+link_code+"'";
			List list=appBo.query(sql);
			if(list.size()>0){
				appBo.runSQL("update sh_scene_linksub set status='"+status+"',contime='"+controller+"'");	
			}else{
				appBo.runSQL("insert into sh_scene_linksub (id,scene_code,link_code,name,status,contime) values"
						+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+link_code+"','"+link_name+"','"+status+"','"+controller+"'");	
			}
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	public static Map add(String scene_code,String user_code,Object[] devices,Object[] links){
		Map map=new HashMap();
		List lists=appBo.query(sqls+"where scene_code='"+scene_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="�����Ѵ��ڣ���Գ������б༭��";
		}else{
			appBo.runSQL("insert into sh_common_scene (id,scene_code,user_code,name) values"
					+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+user_code+"'");
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				device_code=(String)dmap.get("device_code");
				device_name=(String)dmap.get("device_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_scene_devicesub (id,scene_code,device_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+device_code+"','"+device_name+"','"+status+"','"+controller+"'");					
			} 
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				link_code=(String)dmap.get("link_code");
				link_name=(String)dmap.get("link_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_scene_linksub (id,scene_code,link_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+link_code+"','"+link_name+"','"+status+"','"+controller+"'");		
			}
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	public static Map delete(String scene_code,Object[] devices,Object[] links){
		Map map=new HashMap();
		List lists=appBo.query(sqls+"where scene_code='"+scene_code+"'");
		if(lists.size()>0){
			flag="E";
			msg="���������ڣ���ȷ�ϣ�";
		}else{
			appBo.runSQL("update sh_common_scene set is_del='1'");	
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
}
