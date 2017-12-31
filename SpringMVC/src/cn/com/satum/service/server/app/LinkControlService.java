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
 * @author yxb
 * 
 *         ��������ɾ����
 */
public class LinkControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("reqType");
		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addLink(reqMap);// �������
		} else if ("update".equals(reqType)) {
			resStr = updateLink(reqMap);// �޸�����
		} else if ("delete".equals(reqType)) {
			resStr = deleteLink(reqMap);// ɾ������
		} else if ("query".equals(reqType)) {
			resStr = queryLink(reqMap);// ɾ������
		}

		return resStr;
	}

	/**
	 * �������
	 * 
	 * @param reqMap
	 * @return
	 */
	public String addLink(Map<String, Object> reqMap) {

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
				String virName = (String) virMap.get("envirCode");// ��������
				String envirCode = (String) virMap.get("envirCode");// ����code
				// @SuppressWarnings("unchecked")
				// List<Map<String, Object>> linkDicList = AppBo
				// .query("SELECT sld.`code` from sh_link_dic sld where
				// sld.`name`=" + virName);// �����������ֵ���ѯ����code
				// if (linkDicList != null && linkDicList.size() > 0) {
				// virCode = (String) linkDicList.get(0).get("code");
				// }
				String virType = (String) virMap.get("virType");// �������ͣ�0����Ϊ��1����ʱ���ã�
				String virContent = (String) virMap.get("virContent");// ��������
				@SuppressWarnings("unchecked")
				Map<String, Object> virParamMap = (Map<String, Object>) virMap.get("virParam");// ��������
				String virParam = "";// ������ݿ������������ʽ
				// ��������code��ȡ�������������������ͬ�����Ͳ�ͬ
				if ("time".equals(envirCode)) {// ʱ��
					String startTime = (String) virParamMap.get("startTime");// ��ʼʱ��
					String endTime = (String) virParamMap.get("endTime");// ����ʱ��
					String days = (String) virParamMap.get("days");// �ܼ�
					virParam = startTime + "-" + endTime + " " + days;
				}

				AppBo.runSQL(
						"INSERT INTO sh_link_virsub (id,link_code,vir_code,vir_type,vir_content,vir_param) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + envirCode + "','" + virType + "','"
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
//				String sceneStatus = (String) sceneMap.get("sceneStatus");// ״̬
				String sceneContime = (String) sceneMap.get("sceneContime");// ����ʱ�䣻0��������

				AppBo.runSQL(
						"INSERT INTO sh_link_scenesub (id,link_code,scene_name,scene_code,scene_status,scene_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + sceneName + "','" + sceneCode + "','" + sceneContime + "')");
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

	public String updateLink(Map<String, Object> reqMap) {

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
						String envirCode = (String) virIdList.get(0).get("vir_code");// ��������id��ȡ����code
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
							if ("time".equals(envirCode)) {// ʱ��
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
							@SuppressWarnings("unchecked")
							List<Map<String, Object>> envirList = AppBo
									.query("SELECT envir_name,starts,ends FROM sh_common_envir where envir_code='"
											+ envirCode + "'");
							String starts = (String) envirList.get(0).get("starts");
							String ends = (String) envirList.get(0).get("ends");

							if (StringUtils.isNotBlank((String) virParamMap.get("starts"))) {
								starts = (String) virParamMap.get("starts");
							}
							if (StringUtils.isNotBlank((String) virParamMap.get("ends"))) {
								ends = (String) virParamMap.get("ends");
							}
							AppBo.runSQL("UPDATE sh_common_envir SET ends='" + ends + "',starts='" + starts
									+ "' where envir_code='" + envirCode + "'");
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
	public String deleteLink(Map<String, Object> reqMap) {

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
	 * ��ѯ����
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryLink(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// �ӿ����շ������ݵ�map
		List<Map<String, Object>> resDataList = new ArrayList<Map<String, Object>>();// �ӿ����շ������ݵ�map�������list

		String userCode = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// �����û�id��ѯ��������
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.link_name,scl.link_status FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userCode);
		// ���е�����
		if (linkList != null && linkList.size() > 0) {
			for (Map<String, Object> linkMap : linkList) {
				Map<String, Object> resLinkMap = new HashMap<String, Object>();// �ӿ����շ������ݵ�map�������list�ĵ�������map
				// ��������
				String linkCode = (String) linkMap.get("link_code");// ����code
				String linkName = (String) linkMap.get("link_name");// ��������
				String linkStatus = (String) linkMap.get("link_status");// ����״̬1���رգ�2����

				resLinkMap.put("linkCode", linkCode);
				resLinkMap.put("linkName", linkName);
				resLinkMap.put("linkStatus", linkStatus);

				// ��������code��ѯ���������ӱ�
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> virList = AppBo
						.query("SELECT slv.id,slv.vir_code,slv.vir_type,slv.vir_content,slv.vir_param FROM sh_link_virsub slv WHERE slv.link_code='"
								+ linkCode + "'");
				// ���������������е�����
				List<Map<String, Object>> resVirList = new ArrayList<Map<String, Object>>();// �ӿ����շ������ݵ�map�������list�ĵ�������map�������list
				if (virList != null && virList.size() > 0) {
					for (Map<String, Object> virMap : virList) {
						Map<String, Object> resVirMap = new HashMap<>();// �ӿ����շ������ݵ�map�������list�ĵ�������map�������list��ĵ�������map
						String virId = (String) virMap.get("id");// ����id
						String virType = (String) virMap.get("vir_type");// ��������
						String virContent = (String) virMap.get("vir_content");// ��������
						String virParam = (String) virMap.get("vir_param");// ����������ֻ��codeΪtime������õ���
						String envirCode = (String) virMap.get("vir_code");// ����code

						resVirMap.put("virId", virId);
						resVirMap.put("virType", virType);
						resVirMap.put("virContent", virContent);

						// ��������code��ѯ�����ֵ����������ơ�����������codeΪtime���������⣬������������洢��
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> envirList = AppBo
								.query("SELECT envir_name,starts,ends FROM sh_common_envir  WHERE envir_code='"
										+ envirCode + "'");
						Map<String, Object> envirMap = envirList.get(0);
						String envirName = (String) envirMap.get("envir_name");
						resVirMap.put("envirName", envirName);

						if ("time".equals(envirCode)) {// ʱ��
							String startTime = virParam.substring(0, virParam.lastIndexOf("-"));
							String endTime = virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" "));
							String days = virParam.substring(virParam.indexOf(" ") + 1, virParam.length());
							resVirMap.put("startTime", startTime);// ��ʼʱ��
							resVirMap.put("endTime", endTime);// ����ʱ��
							resVirMap.put("days", days);// �ܼ����ԡ������ָ���ַ�����

						} else {
							String starts = (String) envirMap.get("starts");
							String ends = (String) envirMap.get("ends");
							resVirMap.put("starts", starts);
							resVirMap.put("ends", ends);
						}

						resVirList.add(resVirMap);
					}
				}
				resLinkMap.put("virList", resVirList);// ���ص������������е�����list

				// ��������code��ѯ�����豸�ӱ�
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> deviceList = AppBo
						.query("SELECT sld.id,sld.device_name,sld.device_status,sld.device_contime FROM sh_link_devicesub sld where sld.link_code='"
								+ linkCode + "'");
				// ���������������е��豸
				List<Map<String, Object>> resDeviceList = new ArrayList<Map<String, Object>>();// �ӿ����շ������ݵ�map�������list�ĵ�������map����豸list
				if (deviceList != null && deviceList.size() > 0) {
					for (Map<String, Object> deviceMap : deviceList) {
						Map<String, Object> resDeviceMap = new HashMap<>();// �ӿ����շ������ݵ�map�������list�ĵ�������map�������list��ĵ����豸map
						resDeviceMap.put("deviceId", deviceMap.get("id"));
						resDeviceMap.put("deviceName", deviceMap.get("device_name"));// �豸����
						resDeviceMap.put("deviceStatus", deviceMap.get("device_status"));// �豸״̬
						resDeviceMap.put("deviceContime", deviceMap.get("device_contime"));// �豸����ʱ��
						resDeviceList.add(resDeviceMap);
					}
				}
				resLinkMap.put("deviceList", resDeviceList);// ���صĵ������������е��豸list

				// ��������code��ѯ�����龰�ӱ�
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> sceneList = AppBo
						.query("SELECT sls.id,sls.scene_name,sls.scene_status,sls.scene_contime FROM sh_link_scenesub sls where sls.link_code='"
								+ linkCode + "'");
				// ���������������е��龰
				List<Map<String, Object>> resSceneList = new ArrayList<Map<String, Object>>();// �ӿ����շ������ݵ�map�������list�ĵ�������map����龰list
				if (sceneList != null && sceneList.size() > 0) {
					for (Map<String, Object> sceneMap : sceneList) {
						Map<String, Object> resSceneMap = new HashMap<>();// �ӿ����շ������ݵ�map�������list�ĵ�������map�������list��ĵ�������map
						resSceneMap.put("sceneId", sceneMap.get("id"));
						resSceneMap.put("sceneName", sceneMap.get("scene_name"));// �龰����
						resSceneMap.put("sceneStatus", sceneMap.get("scene_status"));// �龰״̬
						resSceneMap.put("sceneContime", sceneMap.get("scene_contime"));// �龰����ʱ��
						resSceneList.add(resSceneMap);
					}
				}
				resLinkMap.put("deviceList", resSceneList);// ���صĵ������������е��龰list

				resDataList.add(resLinkMap);
			}

		} else {
			resMap.put("result", "S");
			resMap.put("msg", "���û�û�����������");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "��ȡ�����б�ɹ���");
		resMap.put("data", resDataList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}
}
