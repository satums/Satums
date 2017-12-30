package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;

public class LinkControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("reqType");
		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addLike(reqMap);// �������
		} else if ("update".equals(reqType)) {
			resStr = updateLike(reqMap);// �޸�����
		} else if ("delete".equals(reqType)) {
			resStr = deleteLike(reqMap);// ɾ������
		}

		return resStr;
	}

	/**
	 * �������
	 * 
	 * @param reqMap
	 * @return
	 */
	public String addLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String userCode = (String) reqMap.get("userCode");

		String linkName = (String) reqMap.get("linkName");
		if (StringUtils.isBlank(linkName)) {
			resMap.put("result", "E");
			resMap.put("msg", "�������Ʋ���Ϊ�գ�");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// ������������
		String linkCode = DataUtil.getUUID();
		AppBo.runSQL("INSERT INTO sh_common_link (id,link_name,link_code,user_code) VALUES('" + DataUtil.getUUID()
				+ "','" + linkName + "','" + linkCode + "','" + userCode + "')");

		// �������������ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> virList = (List<Map<String, Object>>) reqMap.get("virList");// ��ȡ���е���������
		if (virList != null && virList.size() > 0) {
			for (Map<String, Object> virMap : virList) {
				String virName = (String) virMap.get("virName");// ��������
				String virCode = "";// ����code
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> linkDicList = AppBo
						.query("SELECT sld.`code` from sh_link_dic sld where sld.`name`=" + virName);// �����������ֵ���ѯ����code
				if (linkDicList != null && linkDicList.size() > 0) {
					virCode = (String) linkDicList.get(0).get("code");
				}
				String virType = (String) virMap.get("virType");// �������ͣ�0����Ϊ��1����ʱ���ã�
				String virContent = (String) virMap.get("virContent");// ��������
				@SuppressWarnings("unchecked")
				Map<String, Object> virParamMap = (Map<String, Object>) virMap.get("virParam");// ��������
				String virParam = "";// ������ݿ������������ʽ
				// ��������code��ȡ�������������������ͬ�����Ͳ�ͬ
				if ("time".equals(virCode)) {// ʱ��
					String startTime = (String) virParamMap.get("startTime");// ��ʼʱ��
					String endTime = (String) virParamMap.get("endTime");// ����ʱ��
					String days = (String) virParamMap.get("days");// �ܼ�
					virParam = startTime + "-" + endTime + " " + days;
				}

				if ("temp".equals(virCode)) {// �¶�
					String lowTemp = (String) virParamMap.get("lowTemp");// ����
					String highTemp = (String) virParamMap.get("highTemp");// ����
					virParam = lowTemp + " " + highTemp;
					AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowTemp + "',param_high='" + highTemp
							+ "' where `code`='" + virCode + "'");
				}
				if ("damp".equals(virCode)) {// ʪ��
					String lowDamp = (String) virParamMap.get("lowDamp");// �͵�
					String highDamp = (String) virParamMap.get("highDamp");// �ߵ�
					virParam = lowDamp + " " + highDamp;
					AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowDamp + "',param_high='" + highDamp
							+ "' where `code`='" + virCode + "'");
				}
				if ("illu".equals(virCode)) {// ���ն�
					String lowIllu = (String) virParamMap.get("lowIllu");// �͵�
					String highIllu = (String) virParamMap.get("highIllu");// �ߵ�
					virParam = lowIllu + " " + highIllu;
					AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowIllu + "',param_high='" + highIllu
							+ "' where `code`='" + virCode + "'");
				}
				if ("trig".equals(virCode)) {// ����
					String seconds = (String) virParamMap.get("seconds");// ����
					virParam = seconds;
					AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + seconds + "',param_high='" + seconds
							+ "' where `code`='" + virCode + "'");
				}

				AppBo.runSQL(
						"INSERT INTO sh_link_virsub (id,link_code,vir_code,vir_type,vir_content,vir_param) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + virCode + "','" + virType + "','"
								+ virContent + "','" + virParam + "')");
			}

		} else {
			resMap.put("result", "E");
			resMap.put("msg", "�������һ�����������");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		// ���������豸�ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = (List<Map<String, Object>>) reqMap.get("deviceList");// ��ȡ�����ڵ�ǰ�������豸�б�
		if (deviceList != null && deviceList.size() > 0) {
			for (Map<String, Object> deviceMap : deviceList) {
				String deviceName = (String) deviceMap.get("deviceName");// �豸����
				String deviceCode = (String) deviceMap.get("deviceCode");// �豸code
				String deviceStatus = (String) deviceMap.get("deviceStatus");// ״̬��1���رգ�2����
				String deviceContime = (String) deviceMap.get("deviceContime");// ����ʱ�䣻0��������

				AppBo.runSQL(
						"INSERT INTO sh_link_devicesub (id,link_code,device_name,device_code,device_status,device_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + deviceName + "','" + deviceCode
								+ "','" + deviceStatus + "','" + deviceContime + "')");
			}
		}
		// ���������龰�ӱ�
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> sceneList = (List<Map<String, Object>>) reqMap.get("sceneList");// ��ȡ�����ڵ�ǰ�������龰�б�
		if (sceneList != null && sceneList.size() > 0) {
			for (Map<String, Object> sceneMap : sceneList) {
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
		if (deviceList.size() == 0 && sceneList.size() == 0) {
			resMap.put("result", "E");
			resMap.put("msg", "�������һ�����������");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "������ӳɹ���");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * �޸�����
	 */

	public String updateLike(Map<String, Object> reqMap) {

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
						AppBo.runSQL("DELETE FROM sh_link_virsub WHERE id='" + virId + "'");
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
						AppBo.runSQL("DELETE FROM sh_link_devicesub WHERE id='" + deviceId + "'");
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
						AppBo.runSQL("DELETE FROM sh_link_scenesub WHERE id='" + sceneId + "'");
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
	public String deleteLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String linkCode = (String) reqMap.get("linkCode");
		// ɾ�����������ӱ�
		AppBo.runSQL("DELETE FROM sh_link_virsub WHERE link_code='" + linkCode + "'");
		// ɾ�������豸�ӱ�
		AppBo.runSQL("DELETE FROM sh_link_devicesub WHERE link_code='" + linkCode + "'");
		// ɾ�������龰�ӱ�
		AppBo.runSQL("DELETE FROM sh_link_scenesub WHERE link_code='" + linkCode + "'");
		// ɾ����������
		AppBo.runSQL("DELETE FROM sh_common_link WHERE link_code='" + linkCode + "'");
		resMap.put("result", "S");
		resMap.put("msg", "����ɾ���ɹ���");
		JSONObject json = new JSONObject(resMap);
		//ceshi
		return json.toString();
	
	}
}
