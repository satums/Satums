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
 * �龰����ɾ���ġ���
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
		String user_code=(String)lmap.get("user_code");
		String scene_code=(String)lmap.get("scene_code");
		String scene_name=(String)lmap.get("scene_name");
		JSONArray device=JSONObject.fromObject(jsondata).getJSONArray("device");
		Object[] devices=device.toArray();		
		JSONArray link=JSONObject.fromObject(jsondata).getJSONArray("link");
		Object[] links=link.toArray();
		if(mark.equals("add")){
			map=add(scene_code,scene_name,user_code,devices,links);
		}else if(mark.equals("update")){
			map=update(scene_code,user_code,devices,links);
		}else if(mark.equals("delete")){
			map=delete(user_code,scene_code);
		}
		else if(mark.equals("query")){
			String type = (String)lmap.get("type");
			if("1".equals(type)){
				map=queryScene(user_code);// ��ѯ�龰
			}
			else if("2".equals(type)){
				map=queryDevice(scene_code);// ��ѯ�豸
			}
			else if("3".equals(type)){
				map=queryLink(scene_code);// ��ѯ����
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
			msg="���������ڣ�������ӳ�����";
		}else{
			flag="S";
			msg="�ɹ�";
		}
		try{
		//�����龰�豸�ӱ�
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
			
			//��ѯ�����龰�������豸
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> deviceLists = appBo
					.query("SELECT device_code from sh_scene_devicesub where is_del='2' and  scene_code='"+scene_code+"'");
			
			Boolean falg=true;
			if (deviceLists != null && deviceLists.size() > 0) {
				//�����龰�������豸
				for (Map<String, Object> deviceList : deviceLists) {
					
					String deviceCode = (String) deviceList.get("device_code");//�豸code
					//�����ӿڴ��������豸
					for(int j=0;j<devices.length;j++){
						Map dmaps=(Map)devices[j];
						device_code=(String)dmaps.get("device_code");
						if(device_code.equals(deviceCode)){
							//��device_code���ڣ���falg=false
							falg = false;
							break;
						}
						
					}
					
					//��falg=true device_code�����ڣ�ɾ��֮
					if(falg){
						appBo.runSQL("update sh_scene_devicesub set is_del='1' where scene_code='"+scene_code+"' and device_code='"+deviceCode+"'");
					}
					
					//��ǿ�н�falg=true�������´��ж�
					falg=true;
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
			
			//��ѯ�����龰����������
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> linkLists = appBo
					.query("SELECT link_code from sh_scene_linksub where is_del='2' and  scene_code='"+scene_code+"'");
			
			Boolean falg=true;
			if (linkLists != null && linkLists.size() > 0) {
				//�����龰�������豸
				for (Map<String, Object> linkList : linkLists) {
					
					String linkCode = (String) linkList.get("link_code");//����code
					//�����ӿڴ��������豸
					for(int j=0;j<links.length;j++){
						Map dmaps=(Map)links[j];
						link_code=(String)dmaps.get("link_code");
						if(link_code.equals(linkCode)){
							//��link_code���ڣ���falg=false
							falg = false;
							break;
						}
						
					}
					
					//��falg=true link_code�����ڣ�ɾ��֮
					if(falg){
						appBo.runSQL("update sh_scene_linksub set is_del='1' where scene_code='"+scene_code+"' and link_code='"+linkCode+"'");
					}
					
					//��ǿ�н�falg=true�������´��ж�
					falg=true;
				} 
			}
		}
		flag="S";
		msg="�ɹ�";
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
			msg="�����Ѵ��ڣ���Գ������б༭��";
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
			for(int i=0;i<devices.length;i++){
				Map dmap=(Map)devices[i];
				link_code=(String)dmap.get("link_code");
				link_name=(String)dmap.get("link_name");
				status=(String)dmap.get("status");
				controller=(String)dmap.get("controller");			
					appBo.runSQL("insert into sh_scene_linksub (id,scene_code,link_code,name,status,contime) values"
							+ "('"+DataUtil.getUUID()+"','"+scene_code+"','"+link_code+"','"+link_name+"','"+status+"','"+controller+"')");		
			}
			
			flag="S";
			msg="�ɹ�";
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
			msg="���������ڣ���ȷ�ϣ�";
		}else{
			appBo.runSQL("update sh_common_scene set is_del='1' where user_code='"+user_code+"' and  scene_code='"+scene_code+"'");	
			appBo.runSQL("update sh_scene_devicesub set is_del='1' where scene_code='"+scene_code+"'");	
			appBo.runSQL("update sh_scene_linksub set is_del='1' where scene_code='"+scene_code+"'");	
				flag="S";
				msg="�ɹ�";	
		}
		map.put("result",flag);
		map.put("msg",msg);
		return map;
	}
	
	/**
	 * ��ѯ����
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
			map.put("msg", "δ��ѯ��������");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "��ȡ�����б�ɹ���");
		map.put("data", sceneList);
		return map;

	}
	
	/**
	 * ��ѯ�豸
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
			map.put("msg", "δ��ѯ���豸��");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "��ȡ�豸�б�ɹ���");
		map.put("data", deviceList);
		return map;

	}
	
	/**
	 * ��ѯ����
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
			map.put("msg", "δ��ѯ��������");
			return map;
		}

		map.put("result", "S");
		map.put("msg", "��ȡ�����б�ɹ���");
		map.put("data", linkList);
		return map;

	}
}
