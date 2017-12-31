package cn.com.satum.service.server.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
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
			resStr = addDevice(reqMap);// ����豸
		} else if ("update".equals(reqType)) {
			resStr = updateDevice(reqMap);// �޸��豸
		} else if ("delete".equals(reqType)) {
			resStr = deleteDevice(reqMap);// ɾ���豸
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
	 */
	public String addDevice(Map<String, Object> reqMap) {

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
		if (commonDeviceList == null ) {
			// ��������豸��
			String id = DataUtil.getUUID();
			AppBo.runSQL("INSERT INTO sh_common_device (id,num,name,user_code,device_type_id) VALUES('" + DataUtil.getUUID()
					+ "','" + num + "','" + name + "','" + userCode + "','" + typeId + "')");
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
		
		if (deviceTypeList == null) {//����������levelΪ��ײ�ʱ
			// ��ѯ�豸���ͱ�
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> deviceTypeList1 = AppBo
					.query("SELECT id,name from sh_common_device_type where id = '" + typeId + "'  AND is_del='2' ");
			for (Map<String, Object> deviceTypeMap : deviceTypeList1) {
				String deviceTypeId = (String) deviceTypeMap.get("id");//�豸����id
				String deviceTypeName = (String) deviceTypeMap.get("name");//�豸��������id
				AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code) values "
						+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"')");
			}
		}
		
		for (Map<String, Object> deviceTypeMap : deviceTypeList) {
			
			String deviceTypeId = (String) deviceTypeMap.get("id");//�豸����id
			String deviceTypeName = (String) deviceTypeMap.get("name");//�豸��������id
			
			AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code) values "
					+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"')");
		} 
		
		
		resMap.put("result", "S");
		resMap.put("msg", "�豸��ӳɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * �޸�����
	 */

	public String updateDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		String linkCode = (String) reqMap.get("LinkCode");
		if (StringUtils.isBlank(linkCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ����Ҫ�޸ĵ�������Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// �޸���������
		String linkName = (String) reqMap.get("linkName");
		if (StringUtils.isNotBlank(linkName)) {
			AppBo.runSQL("UPDATE sh_common_link SET link_name='" + linkName + "' where link_code='" + linkCode + "'");
		}

		// �޸����������ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> virList = (List<Map<String, Object>>) reqMap.get("virList");// ��ȡ���е���������
		if (virList != null && virList.size() > 0) {
			for (Map<String, Object> virMap : virList) {
				String virId = (String) virMap.get("virId");// ����id
				String conVirType = (String) virMap.get("conVirType");// �������ͣ�update���޸ģ�del:ɾ��
				if (StringUtils.isNotBlank(virId)) {
					if ("update".equals(conVirType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> virIdList = AppBo
								.query("SELECT slv.vir_code,slv.vir_type,slv.vir_content,slv.vir_param FROM sh_link_virsub slv where slv.id='"
										+ virId + "'");
						String virCode = (String) virIdList.get(0).get("vir_code");// ��������id��ȡ����code
						String virType = (String) virIdList.get(0).get("vir_type");// ��������id��ȡ��������
						String virContent = (String) virIdList.get(0).get("vir_content");// ��������id��ȡ��������
						String virParam = (String) virIdList.get(0).get("vir_param");// ��������id��ȡ��������

						if (StringUtils.isNotBlank((String) virMap.get("virType"))) {
							virType = (String) virMap.get("virType");// �������ͣ�0����Ϊ��1����ʱ���ã�
						}
						if (StringUtils.isNotBlank((String) virMap.get("virContent"))) {
							virContent = (String) virMap.get("virContent");// �������ͣ�0����Ϊ��1����ʱ���ã�
						}
						@SuppressWarnings("unchecked")
						Map<String, Object> virParamMap = (Map<String, Object>) virMap.get("virParam");// ��������
						if (!virParamMap.isEmpty()) {
							// ��������code��ȡ�������������������ͬ�����Ͳ�ͬ
							if ("time".equals(virCode)) {// ʱ��
								String startTime = (String) virParamMap.get("startTime");// ��ʼʱ��
								if (StringUtils.isNotBlank(startTime)) {
									virParam.replace(virParam.substring(0, virParam.lastIndexOf("-")), startTime);
								}
								String endTime = (String) virParamMap.get("endTime");// ����ʱ��
								if (StringUtils.isNotBlank(endTime)) {
									virParam.replace(
											virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" ")),
											endTime);
								}
								String days = (String) virParamMap.get("days");// �ܼ�
								if (StringUtils.isNotBlank(days)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											days);
								}
							}

							if ("temp".equals(virCode)) {// �¶�
								String lowTemp = (String) virParamMap.get("lowTemp");// ����
								if (StringUtils.isNotBlank(lowTemp)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowTemp);
								}
								String highTemp = (String) virParamMap.get("highTemp");// ����
								if (StringUtils.isNotBlank(lowTemp)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highTemp);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowTemp + "',param_high='"
										+ highTemp + "' where `code`='" + virCode + "'");
							}
							if ("damp".equals(virCode)) {// ʪ��
								String lowDamp = (String) virParamMap.get("lowDamp");// �͵�
								if (StringUtils.isNotBlank(lowDamp)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowDamp);
								}
								String highDamp = (String) virParamMap.get("highDamp");// �ߵ�
								if (StringUtils.isNotBlank(highDamp)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highDamp);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowDamp + "',param_high='"
										+ highDamp + "' where `code`='" + virCode + "'");
							}
							if ("illu".equals(virCode)) {// ���ն�
								String lowIllu = (String) virParamMap.get("lowIllu");// �͵�
								if (StringUtils.isNotBlank(lowIllu)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowIllu);
								}
								String highIllu = (String) virParamMap.get("highIllu");// �ߵ�
								if (StringUtils.isNotBlank(highIllu)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highIllu);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowIllu + "',param_high='"
										+ highIllu + "' where `code`='" + virCode + "'");
							}
							if ("trig".equals(virCode)) {// ����
								String seconds = (String) virParamMap.get("seconds");// ����
								if (StringUtils.isNotBlank(seconds)) {
									virParam = seconds;
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + seconds + "',param_high='" + seconds
										+ "' where `code`='" + virCode + "'");
							}
						}

						AppBo.runSQL("UPDATE sh_link_virsub SET vir_type='" + virType + "',vir_content='" + virContent
								+ "',vir_param='" + virParam + "' where id='" + virId + "'");
					} else if ("del".equals(conVirType)) {
						// ɾ�����������ӱ�
						AppBo.runSQL("UPDATE sh_link_virsub SET is_del='1' WHERE id='" + virId + "'");
					}

				}

			}

		}
		// �޸������豸�ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = (List<Map<String, Object>>) reqMap.get("deviceList");// ��ȡ�����ڵ�ǰ�������豸�б�
		if (deviceList != null && deviceList.size() > 0) {
			for (Map<String, Object> deviceMap : deviceList) {
				String deviceId = (String) deviceMap.get("deviceId");
				String conDeviceType = (String) deviceMap.get("conDeviceType");// �������ͣ�update���޸ģ�del:ɾ��
				if (StringUtils.isNotBlank(deviceId)) {
					if ("update".equals(conDeviceType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> deviceIdList = AppBo
								.query("SELECT sld.device_status,sld.device_contime FROM sh_link_devicesub sld where sld.id='"
										+ deviceId + "'");
						String deviceStatus = (String) deviceIdList.get(0).get("device_status");// �����豸id��ѯ�豸״̬
						String deviceContime = (String) deviceIdList.get(0).get("device_contime");// ����ʱ�䣻0��������

						if (StringUtils.isNotBlank((String) deviceMap.get("deviceStatus"))) {
							deviceStatus = (String) deviceMap.get("deviceStatus");
						}
						if (StringUtils.isNotBlank((String) deviceMap.get("deviceContime"))) {
							deviceContime = (String) deviceMap.get("deviceContime");
						}

						AppBo.runSQL("UPDATE sh_link_devicesub SET device_status = '" + deviceStatus
								+ "',device_contime='" + deviceContime + "' where id='" + deviceId + "'");
					} else if ("del".equals(conDeviceType)) {
						// ɾ�������豸�ӱ�
						AppBo.runSQL("UPDATE sh_link_devicesub SET is_del='1' WHERE id='" + deviceId + "'");
					}

				}
			}
		}
		// �޸������龰�ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> sceneList = (List<Map<String, Object>>) reqMap.get("sceneList");// ��ȡ�����ڵ�ǰ�������龰�б�
		if (sceneList != null && sceneList.size() > 0) {
			for (Map<String, Object> sceneMap : sceneList) {
				String sceneId = (String) sceneMap.get("sceneId");
				String conSceneType = (String) sceneMap.get("conSceneType");// �������ͣ�update���޸ģ�del:ɾ��
				if (StringUtils.isNotBlank(sceneId)) {
					if ("update".equals(conSceneType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> sceneIdList = AppBo.query(
								"SELECT scene_status,scene_contime FROM sh_link_scenesub where id='" + sceneId + "'");
						String sceneStatus = (String) sceneIdList.get(0).get("scene_status");// �����龰id��ѯ״̬
						String sceneContime = (String) sceneIdList.get(0).get("scene_contime");// ����ʱ�䣻0��������

						if (StringUtils.isNotBlank((String) sceneMap.get("sceneStatus"))) {
							sceneStatus = (String) sceneMap.get("sceneStatus");
						}
						if (StringUtils.isNotBlank((String) sceneMap.get("sceneContime"))) {
							sceneContime = (String) sceneMap.get("sceneContime");
						}
						AppBo.runSQL("UPDATE sh_link_scenesub SET scene_status='" + sceneStatus + "',scene_contime='"
								+ sceneContime + "' where id='" + sceneId + "'");
					} else if ("del".equals(conSceneType)) {
						// ɾ�������龰�ӱ�
						AppBo.runSQL("UPDATE sh_link_scenesub SET is_del='1' WHERE id='" + sceneId + "'");
					}
				}
				String sceneName = (String) sceneMap.get("sceneName");// �龰����
				String sceneCode = (String) sceneMap.get("sceneCode");// �龰code
				String sceneStatus = (String) sceneMap.get("sceneStatus");// ״̬
				String sceneContime = (String) sceneMap.get("sceneContime");// ����ʱ�䣻0��������

				AppBo.runSQL(
						"INSERT INTO sh_link_devicesub (id,link_code,scene_name,scene_code,scene_status,scene_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + sceneName + "','" + sceneCode + "','"
								+ sceneStatus + "','" + sceneContime + "')");
			}
		}
		resMap.put("result", "S");
		resMap.put("msg", "�����޸ĳɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * ɾ������
	 * 
	 * @param reqMap
	 * @return
	 */
	public String deleteDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String linkCode = (String) reqMap.get("linkCode");
		// ɾ�����������ӱ�
		AppBo.runSQL("UPDATE sh_link_virsub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// ɾ�������豸�ӱ�
		AppBo.runSQL("UPDATE sh_link_devicesub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// ɾ�������龰�ӱ�
		AppBo.runSQL("UPDATE sh_link_scenesub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// ɾ����������
		AppBo.runSQL("UPDATE sh_common_link SET is_del='1' WHERE link_code='" + linkCode + "'");
		resMap.put("result", "S");
		resMap.put("msg", "����ɾ���ɹ���");
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
				.query("SELECT id,name FROM sh_common_device_type WHERE is_del='2' AND parent_id="
						+ parentId);
		
		if (deviceList == null)  {
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
				.query("SELECT id,name FROM sh_device WHERE is_del='2' AND user_code="
						+ userCode);
		
		if (deviceList == null)  {
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
}
