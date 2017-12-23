package cn.com.satum.service.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.HEX2And16;

public class DataUtil {
	private static AppBo appBo;
	//����
	private final static String jsondata="{"
			+ "\"zjbh\":\"hhhhhhhhhhhsss\""		
			+ "data:["
			+ "{\"num\":\"A01\","
			+ "\"name\":\"A��Դһ\","
			+ "\"status\":\"1\"},"
			+ "{\"num\":\"B01\","
			+ "\"name\":\"A��Դ��\","
			+ "\"status\":\"2\"},"
			+ "{\"num\":\"A02\","
			+ "\"name\":\"A��Դһ\","
			+ "\"status\":\"1\"}"
			+ "]}";
	// ���������Ʒ��ʷ����ʱ���͵�����
	public String dataParse(String data, String IPs, int ports) {
		/**
		 * ��Data���н�������ȡ������� String zjbhs=""; this.zjbh=zjbhs
		 * 
		 */
		System.out.println(IPs);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(date);
		String zjbh = "hhhhhhhhhhhsss";
		//�����豸��Ϣ
		updateDevice(data);
		//����������������
		return new DataUtil().dataUpdate(zjbh, IPs, ports, dates);
	}
public static void updateDevice(String data){
	String json=new HEX2And16().hex2To16(data);
	Map map=JSONObject.fromObject(json);
	String zjbh=(String)map.get("zjbh");
	List list=appBo.query("select sh_masterdata where zjbh='"+zjbh+"'");
	String userid="";
	if(list.size()>0){
		userid=(String) ((Map)list.get(0)).get("user_code");
	}else{
		
	}
	JSONArray datas=JSONObject.fromObject(jsondata).getJSONArray("data");
	String dnum="";
	String status="";
	Object[] devices=datas.toArray();	
	for(int i=0;i<devices.length;i++){
		Map mapd=(Map)devices[i];		
		dnum=(String)mapd.get("num");
		status=(String)mapd.get("status");
		String sql="update sh_device set status='"+status+"' where user_code='"+userid+"'";
	}
}
	// ���������ݽ��и���
	public String dataUpdate(String zjbhs, String iPs, int ports, String dates) {
		List list = appBo.query("select id from sh_onlinedata where zjbh='" + zjbhs + "'");
		String message = "S";
		if (list.size() > 0) {
			appBo.runSQL("update sh_onlinedata set updated_date='" + dates + "',IP='" + iPs + "',port='" + ports
					+ "' where zjbh='" + zjbhs + "'");
		} else {
			List list2 = appBo.query("select max(id) id from sh_masterdata");
			Map map2 = (Map) list2.get(0);
			int id2 = 0;
			if (map2.get("id") != null) {
				id2 = Integer.valueOf(map2.get("id").toString());
			} else {
				id2 = 0;
			}
			message="�������Ǳ�����������û�����������¼��";
			try {
				appBo.runSQL("INSERT INTO sh_masterdata VALUES ('" + (id2 + 1) + "','','" + zjbhs + "','','0','','')");
			} catch (Exception e) {
				message = e.getMessage();
			}
		}
		return message;
	}

	// ͨ��������Ų�ѯ��ȡ��������ϸ��Ϣ,�����û�����������Ϣ�������
	public Map dataQuery(String zjbhs, String userid) throws ParseException {
		List listu = appBo.query("select id from sh_masterdata where user_code='" + userid + "' and zjbh='"+zjbhs+"'");
		if (listu.size() > 0) {
			List listu1 = appBo.query("select id from sh_masterdata where user_code='" + userid + "' and zjbh='"+zjbhs+"'");
			//appBo.runSQL("update sh_masterdata set user_code='" + userid + "' where zjbh='" + zjbhs + "'");
		if(listu1.size()>0){}else{
			appBo.runSQL("update sh_masterdata set user_code='" + userid + "' where zjbh='" + zjbhs + "'");	
		}}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map map = null;
		List list1 = appBo.query("select * from sh_onlinedata where zjbh='" + zjbhs + "'");
		// �����mm��ָ������������ʱ�䣬���ʱ�������������ʱ����˵���������Ͽ����ӣ��ƶ˽�״̬���ظ�APP����״̬�ı�
		int mm = 60;
		if (list1.size() > 0) {
			map = (Map) list1.get(0);
			String rq = map.get("updated_date").toString();
			Date date = sdf.parse(rq);
			long a = new Date().getTime();
			long b = date.getTime();
			mm = (int) ((a - b) / 1000);
		} else {
			mm = 9999;
		}
		map.put("second", mm);	
		return map;
	}
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
public String ControllerDevice(Map mapm,String user_code,String device_code,String status){
	String flag="S";
	String msg="�ɹ�";
	String sqls="select * from sh_device where usercode='"+user_code+"' and num='"+device_code+"'";
	Map maps=new HashMap();
	List list=appBo.query(sqls);
	if(list.size()>0){
		Map map=new HashMap();
		map.put("device_code",device_code);
		map.put("status", status);
		JSONObject json=JSONObject.fromObject(map);
		String text=new HEX2And16().hex16tTo2("");
		appBo.runSQL("update sh_device set status='"+status+"' where usercode='"+user_code+"' and num='"+device_code+"'");
	}else{
		flag="E";
		msg="�豸�����ڣ���������豸";
	}
		maps.put("flag",flag);
		maps.put("msg",msg);
	//return JSONObject.fromObject(maps).toString();
	return flag;
}

public String ControllerScene(Map mapm,String user_code,String scene_code,String status){	
	String flag="S";
	String msg="�ɹ�";
	String sqls="select * from sh_common_scene where usercode='"+user_code+"' and num='"+scene_code+"'";
	Map maps=new HashMap();
	List list=appBo.query(sqls);
	if(list.size()>0){
	appBo.runSQL("update sh_common_scene set status='"+status+"' where usercode='"+user_code+"' and num='"+scene_code+"'");
	Map map=new HashMap();
	String sqld="select * from sh_scene_devicesub where  user_code='"+user_code+"' and scene_code='"+scene_code+"'";
	String sqll="select * from sh_scene_linksub where  user_code='"+user_code+"' and scene_code='"+scene_code+"'";
	List listd=appBo.query(sqld);
	List listl=appBo.query(sqll);
	String statu1="1";
	String statu2="2";
	//��ѯ�龰��ص��豸��Ϣ��������Ϣ
	if(status.equals("1")){//����ǹر�	
		statu1="2";
		statu2="1";
	}
		if(listd.size()>0&&listl.size()>0){
			if(listd.size()>0&&listl.size()<1){
				List listds=new ArrayList();
				Map mapds=new HashMap();
				appBo.runSQL("update sh_common_link set status='"+statu2+"' where  user_code='"+user_code+"' and scene_code='"+scene_code+"' and status='"+statu1+"'");
				appBo.runSQL("update sh_scene_linksub set status='"+statu1+"' where  user_code='"+user_code+"' and scene_code='"+scene_code+"' and status='"+statu1+"'");	
				List listd1=appBo.query("select * from sh_scene_devicesub where  user_code='"+user_code+"' and scene_code='"+scene_code+"' and status='"+statu1+"'");
				List listd2=appBo.query("select * from sh_scene_linksub where  user_code='"+user_code+"' and scene_code='"+scene_code+"' and status='"+statu2+"'");			
				for(int i=0;i<listd1.size();i++){
					//ִ��sql�豸�ر�
					Map m=(Map)listd1.get(i);
					mapds.put("device_code", m.get("device_code"));
					mapds.put("status", "2");
					listds.add(mapds);
				}
				for(int i=0;i<listd2.size();i++){
					//ִ��sql�豸��
					Map m=(Map)listd1.get(i);
					mapds.put("device_code", m.get("device_code"));
					mapds.put("status", "1");
					listds.add(mapds);
				}
				map.put("device", listds);
			}else if(listd.size()<1&&listl.size()>0){
				
			}else{
				
			}
		}else{
			flag="E";
			msg="û������豸��������Ϣ��ȷ����ӡ�";
		}
		Map mapd=new HashMap();
	

	}else{
		flag="E";
		msg="�徻������";
	}
	return flag;
}
public String ControllerLink(Map mapm,String user_code,String link_code,String status){
	String flag="S";
	String msg="�ɹ�";
	String sqls="select * from sh_common_link where usercode='"+user_code+"' and num='"+link_code+"'";
	Map maps=new HashMap();
	List list=appBo.query(sqls);
	if(list.size()>0){
		appBo.runSQL("update sh_common_link set status='"+status+"' where usercode='"+user_code+"' and num='"+link_code+"'");
	Map map=new HashMap();
	//��ѯ�龰��ص��豸��Ϣ���龰��Ϣ
	}else{
		flag="E";
		msg="�徻������";
	}
	return flag;
}
}
