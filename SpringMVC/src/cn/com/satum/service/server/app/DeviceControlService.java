package cn.com.satum.service.server.app;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
			}
			else{
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
		
		String typeId = (String) reqMap.get("typeId"); //�豸����id
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> commonDeviceList = AppBo
				.query("SELECT * from sh_common_device where name='"+ name +"' AND num = '" + num + "' AND user_code = '" + userCode + "' AND device_type_id = '" + typeId + "' AND is_del='2' ");
		if (commonDeviceList.size()==0) {
			String soft = getSoft("sh_common_device", userCode);
			// ��������豸��
			AppBo.runSQL("INSERT INTO sh_common_device (id,num,name,user_code,device_type_id,soft,parent_id) VALUES('" + DataUtil.getUUID()
					+ "','" + num + "','" + name + "','" + userCode + "','" + typeId + "','" + soft + "','1')");
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
				AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code,soft) values "
						+ "('"+ids+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"','"+soft+"')");				
				//�������ID������ͷ
				if(typeId.equals("90")){
					AppBo.runSQL("insert into sh_camer (id,usercode,deviceid,type_id,type_name,code,name,account,pwd,status,is_del) values "
							+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+ids+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','admin','"+CheckData.EncoderByMd5("888888")+"','2','2')");
				}			
			}
		}
		
		for (Map<String, Object> deviceTypeMap : deviceTypeList) {
			
			String deviceTypeId = (String) deviceTypeMap.get("id");//�豸����id
			String deviceTypeName = (String) deviceTypeMap.get("name");//�豸��������id
			String soft = getSoft("sh_device", userCode);
			AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code,soft) values "
					+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"','"+soft+"')");
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
		String userCode = (String) reqMap.get("userCode");
		String id = (String) reqMap.get("id");
		String name = (String) reqMap.get("name");
		
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
			AppBo.runSQL("UPDATE sh_device SET name = '" + name + "' WHERE is_del='2' AND id='" + id + "' AND user_code='" + userCode + "'");
		List listc=AppBo.query("select * from sh_camer where deviceid='"+id+"'");
		if(listc.size()>0){
			AppBo.runSQL("update sh_camer set name ='"+name+"' where devicedi='"+id+"'");					
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

		String id = (String) reqMap.get("id");
		//ɾ�������豸
		AppBo.runSQL("UPDATE sh_device SET is_del='1' WHERE id='" + id + "'");
		resMap.put("result", "S");
		resMap.put("msg", "�豸ɾ���ɹ���");
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
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// ����userCode��ѯ�豸
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT id,name FROM sh_device WHERE is_del='2' AND user_code='"
						+ userCode + "' ORDER BY soft " ); 
		
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
				.query("SELECT MAX(soft) as soft from " + table + " where is_del='2' AND user_code = '" + userCode + "' ");
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
