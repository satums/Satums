package cn.com.satum.service.server.app;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

/**
 * @author lwf
 * �豸����ɾ���ġ���
 */
public class DeviceControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("flag");
		String resStr = "";
		if ("add".equals(reqType)) {
			try {
				resStr = addDevice(reqMap);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// ����豸
		} else if ("update".equals(reqType)) {
			String type = (String) reqMap.get("type");
			if("1".equals(type)){
				resStr = updateDevice(reqMap);// �޸��豸
			}
			else{
				resStr = updateDeviceSoft(reqMap);// �޸��豸����
			}
		} else if ("delete".equals(reqType)) {
			String type = (String) reqMap.get("type");
			if("1".equals(type)){
				resStr = deleteCommoDevice(reqMap);// ɾ�������豸����
			}
			else{
				resStr = deleteDevice(reqMap);// ɾ�������豸
			}
		} else if ("query".equals(reqType)) {
			String type = (String) reqMap.get("type");
			if("1".equals(type)){
				resStr = queryDeviceType(reqMap);// ��ѯ�豸����
			}else{
				resStr = queryDevice(reqMap);// ��ѯ�豸
			}
		}

		return resStr;
	}

	/**
	 * ����豸
	 * 
	 * @param reqMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public String addDevice(Map<String, Object> reqMap) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String userCode = (String) reqMap.get("userCode");
		String name = (String) reqMap.get("name");
		String zjbh = (String) reqMap.get("zjbh");
		
		if (StringUtils.isBlank(name)) {
			resMap.put("result", "E");
			resMap.put("msg", "�����豸���Ʋ���Ϊ�գ�");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
	
		String num = (String) reqMap.get("num");
		if (StringUtils.isBlank(num)) {
			resMap.put("result", "E");
			resMap.put("msg", "�����豸���벻��Ϊ�գ�");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		Map maps=new HashMap();
		try {
			maps=new DataUtil().dataQuery(zjbh,userCode);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String IP=maps.get("ip").toString();
		String flag=maps.get("result").toString();
		String message=maps.get("message").toString();
		int second=Integer.valueOf(maps.get("second").toString());
		int port=Integer.valueOf(maps.get("port").toString());	
		if(!flag.equals("S")||second>65){
			resMap.put("result", "E");
			resMap.put("msg", "���������߲�������豸�������������磡");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		String typeId = (String) reqMap.get("typeId"); //�豸����id
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> commonDeviceList = AppBo
				.query("SELECT * from sh_common_device where name='"+ name +"' AND num = '" + num + "' AND user_code = '" + userCode + "' AND device_type_id = '" + typeId + "' AND is_del='2' ");
		if (commonDeviceList.size()==0) {
			String soft = getSoft("sh_common_device", userCode);
			// ��������豸��
			AppBo.runSQL("INSERT INTO sh_common_device (id,num,name,user_code,device_type_id,soft,parent_id,zjbh) VALUES('" + DataUtil.getUUID()
					+ "','" + num + "','" + name + "','" + userCode + "','" + typeId + "','" + soft + "','1','"+zjbh+"')");
		}
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT * from sh_device where num = '" + num + "' AND user_code = '" + userCode + "' AND is_del='2' ");
		
		if (deviceList != null && deviceList.size() > 0) {
			
			resMap.put("result", "E");
			resMap.put("msg", "��ؿ����豸�Ѿ���ӣ�������޸Ĳ�����");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// ��ѯ�豸���ͱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceTypeList = AppBo
				.query("SELECT id,name from sh_common_device_type where parent_id = '" + typeId + "'  AND is_del='2' ");
		
		if (deviceTypeList.size()==0) {//����������levelΪ��ײ�ʱ		
			// ��ѯ�豸���ͱ�
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> deviceTypeList1 = AppBo
					.query("SELECT id,name from sh_common_device_type where id = '" + typeId + "'  AND is_del='2' ");
			for (Map<String, Object> deviceTypeMap : deviceTypeList1) {
				String deviceTypeId = (String) deviceTypeMap.get("id");//�豸����id
				String deviceTypeName = (String) deviceTypeMap.get("name");//�豸��������id
				String soft = getSoft("sh_device", userCode);
				String ids=DataUtil.getUUID();
				AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code,soft,zjbh) values "
						+ "('"+ids+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+name+"','"+DataUtil.getUUID()+"','"+soft+"','"+zjbh+"')");				
				//�������ID������ͷ
				if(typeId.equals("90")){
					String uid = (String) reqMap.get("uid");
					String account = (String) reqMap.get("account");
					String password = (String) reqMap.get("password");
					AppBo.runSQL("insert into sh_camer (id,usercode,deviceid,type_id,type_name,code,name,account,pwd,status,is_del) values "
							+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+ids+"','"+deviceTypeId+"','"+deviceTypeName+"','"+uid+"','"+name+"','"+account+"','"+password+"','2','2')");
				}			
			}
		}
		
		for (Map<String, Object> deviceTypeMap : deviceTypeList) {
			
			String deviceTypeId = (String) deviceTypeMap.get("id");//�豸����id
			String deviceTypeName = (String) deviceTypeMap.get("name");//�豸��������id
			String soft = getSoft("sh_device", userCode);
			AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code,soft,zjbh) values "
					+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"','"+soft+"','"+zjbh+"')");
		} 
		
		
		resMap.put("result", "S");
		resMap.put("msg", "�豸��ӳɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * �޸Ŀ����豸
	 */
	public String updateDevice(Map<String, Object> reqMap) {
		Map<String, Object> resMap = new HashMap<String, Object>();
try{
	
		String userCode = (String) reqMap.get("userCode");
		String id = (String) reqMap.get("id");
		String name = (String) reqMap.get("name");
		String room_name="";
		String room_id="";
		if(reqMap.get("room_name")!=null){
			room_name=(String) reqMap.get("room_name");
		}
		if(reqMap.get("room_id")!=null){
			room_id=(String) reqMap.get("room_id");
		}	
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT id,name FROM sh_device WHERE is_del='2' AND id='"
						+ id + "' ");
		
		if (deviceList.size()==0)  {
			resMap.put("result", "S");
			resMap.put("msg", "δ��ѯ���豸��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		
		if (StringUtils.isNotBlank(name)) {
			AppBo.runSQL("UPDATE sh_device SET name = '" + name + "',room_id='"+room_id+"',room_name='"+room_name+"' WHERE is_del='2' AND id='" + id + "' AND user_code='" + userCode + "'");
		List listc=AppBo.query("select * from sh_camer where deviceid='"+id+"'");
		if(listc.size()>0){
			AppBo.runSQL("update sh_camer set name ='"+name+"' where deviceid='"+id+"'");					
		}
		
		}
		else{
			resMap.put("result", "S");
			resMap.put("msg", "�豸����Ϊ�գ�");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "�豸�޸ĳɹ���");
	}catch(Exception e){
		resMap.put("result", "E");
		resMap.put("msg", e.getMessage());
	}
		
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}
	
	/**
	 * �޸Ŀ����豸����
	 */
	public String updateDeviceSoft(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		String userCode = (String) reqMap.get("userCode");
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> devices = (List<Map<String, Object>>) reqMap.get("device"); 
		for(int i = 0;i<devices.size();i++){
			Map<String,Object> deviceMap=devices.get(i);
			String id = (String) deviceMap.get("id");   
			String soft = (String) deviceMap.get("soft"); 
			AppBo.runSQL("UPDATE sh_device SET soft = '" + soft + "' WHERE is_del='2' AND id='" + id + "' AND user_code='" + userCode + "'");
		}
		
		resMap.put("result", "S");
		resMap.put("msg", "�豸�����޸ĳɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * ɾ�������豸
	 * 
	 * @param reqMap
	 * @return
	 */
	public String deleteDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();
try{
		String id = (String) reqMap.get("id");
		//ɾ�������豸
		AppBo.runSQL("UPDATE sh_device SET is_del='1' WHERE id='" + id + "'");
		List listc=AppBo.query("select * from sh_camer where deviceid='"+id+"'");
		if(listc.size()>0){
			AppBo.runSQL("update sh_camer set is_del='1' where deviceid='"+id+"'");					
		}
		resMap.put("result", "S");
		resMap.put("msg", "�豸ɾ���ɹ���");
}catch(Exception e){
	resMap.put("result", "E");
	resMap.put("msg", e.getMessage());	
}
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}
	
	/**
	 * ɾ�������豸
	 * 
	 * @param reqMap
	 * @return
	 */
	public String deleteCommoDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String num = (String) reqMap.get("num");
		//ɾ�������豸
		AppBo.runSQL("UPDATE sh_device SET is_del='1' WHERE num='" + num + "'");
		//ɾ�������豸
		AppBo.runSQL("UPDATE sh_common_device SET is_del='1' WHERE num='" + num + "'");
		resMap.put("result", "S");
		resMap.put("msg", "�豸ɾ���ɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}
	
	/**
	 * ��ѯ�豸����
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryDeviceType(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// �ӿ����շ������ݵ�map

		String userCode = (String) reqMap.get("userCode");
		String parentId = (String) reqMap.get("parentTypeId");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// ����parent_id��ѯ�豸���
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT id,name FROM sh_common_device_type WHERE is_del='2' AND parent_id='"
						+ parentId + "' ORDER BY soft ");
		
		if (deviceList.size()==0)  {
			resMap.put("result", "S");
			resMap.put("msg", "δ��ѯ���豸���");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "��ȡ�豸����б�ɹ���");
		resMap.put("data", deviceList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}

	/**
	 * ��ѯ�豸
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// �ӿ����շ������ݵ�map

		String userCode = (String) reqMap.get("userCode");
		String type=(String) reqMap.get("type");
		String zjbh=(String) reqMap.get("zjbh");
		String sql="";
		if(type.equals("2")){
			sql="select t2.id,t2.name,t2.room_id,t2.room_name,t2.status,t2.device_type_name,t2.device_type_id,t2.account,t2.pwd,t2.code,t2.zjbh  from (SELECT t.id,t.name,t.room_id,t.room_name,t.status,t.device_type_name,t.device_type_id,t1.account,t1.pwd,t1.code,t.zjbh,t.is_del,t.user_code,t.soft FROM sh_device t left join sh_camer t1 on t.id=t1.deviceid) t2 WHERE t2.is_del='2' AND t2.user_code='"
						+ userCode + "' and t2.zjbh='"+zjbh+"'  ORDER BY soft ";
		}else{
			String source_type=(String) reqMap.get("source_type");
			String source_id=(String) reqMap.get("source_id");
			//��������չ���Ժ������Ӹ����豸���Ͳ�ѯ
			switch(source_type){
			case "room":
				sql="select t2.id,t2.name,t2.room_id,t2.room_name,t2.status,t2.device_type_name,t2.device_type_id,t2.account,t2.pwd,t2.code,t2.zjbh  from (SELECT t.id,t.name,t.room_id,t.room_name,t.status,t.device_type_name,t.device_type_id,t1.account,t1.pwd,t1.code,t.zjbh,t.is_del,t.user_code,t.soft FROM sh_device t left join sh_camer t1 on t.id=t1.deviceid) t2 WHERE t2.is_del='2' AND t2.user_code='"
						+ userCode + "' and t2.room_id='"+source_id+"'  and t2.zjbh='"+zjbh+"'  ORDER BY soft ";
				break;
			
			default:
				sql="select t2.id,t2.name,t2.room_id,t2.room_name,t2.status,t2.device_type_name,t2.device_type_id,t2.account,t2.pwd,t2.code,t2.zjbh  from (SELECT t.id,t.name,t.room_id,t.room_name,t.status,t.device_type_name,t.device_type_id,t1.account,t1.pwd,t1.code,t.zjbh,t.is_del,t.user_code,t.soft FROM sh_device t left join sh_camer t1 on t.id=t1.deviceid) t2 WHERE t2.is_del='2' AND t2.user_code='"
						+ userCode + "' and t2.room_id='"+source_id+"'  and t2.zjbh='"+zjbh+"'  ORDER BY soft ";
				break;
			}
			
		}
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// ����userCode��ѯ�豸
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query(sql); 
		
		if (deviceList.size()==0)  {
			resMap.put("result", "S");
			resMap.put("msg", "δ��ѯ���豸��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "��ȡ�豸�б�ɹ���");
		resMap.put("data", deviceList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}
	
	/**
	 * ��ѯ���ݿ�����
	 * @param table
	 * @param userCode
	 * @return
	 */
	public String getSoft(String table, String userCode) {
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> softLists = AppBo
				.query("SELECT max(soft) as soft from " + table + " where is_del='2' AND user_code = '" + userCode + "' ");
	    Integer soft = 0;
	    if (softLists != null && softLists.size() > 0) { 
	    	for (Map<String, Object> softList : softLists) {
				String softs = (String) softList.get("soft");
				if(softs != null){					
					soft = Integer.parseInt(softs) + 1;
				}
		    }
	    }
		return soft.toString();

	}
}
