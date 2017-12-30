package cn.com.satum.service.server.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;

public class QueryLink implements AppService {

	@Override
	public String getInfo(String jsondata) {

		Map<String, Object> resMap = new HashMap<String, Object>();// �ӿ����շ������ݵ�map
		List<Map<String, Object>> resDataList = new ArrayList<Map<String, Object>>();// �ӿ����շ������ݵ�map�������list

		Map<String, Object> reqMap = JSON.parseObject(jsondata);

		String userID = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userID)) {
			resMap.put("result", "E");
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// �����û�id��ѯ��������
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.link_name,scl.link_status FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userID);
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
						String virCode = (String) virMap.get("vir_code");// ����code

						resVirMap.put("virId", virId);
						resVirMap.put("virType", virType);
						resVirMap.put("virContent", virContent);

						// ��������code��ѯ�����ֵ����������ơ�����������codeΪtime���������⣬������������洢��
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> virDicList = AppBo
								.query("SELECT sld.`name`,sld.param_low,sld.param_high FROM sh_link_dic sld WHERE sld.`code`='"
										+ virCode + "'");
						Map<String, Object> virDicMap = virDicList.get(0);
						String virName = (String) virDicMap.get("name");
						resVirMap.put("virName", virName);

						if ("time".equals(virCode)) {// ʱ��
							String startTime = virParam.substring(0, virParam.lastIndexOf("-"));
							String endTime = virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" "));
							String days = virParam.substring(virParam.indexOf(" ") + 1, virParam.length());
							resVirMap.put("startTime", startTime);// ��ʼʱ��
							resVirMap.put("endTime", endTime);// ����ʱ��
							resVirMap.put("days", days);// �ܼ����ԡ������ָ���ַ�����

						}
						if ("temp".equals(virCode)) {// �¶�
							String lowTemp = (String) virDicMap.get("param_low");
							String highTemp = (String) virDicMap.get("param_high");
							resVirMap.put("lowTemp", lowTemp);// ����
							resVirMap.put("highTemp", highTemp);// ����
						}
						if ("damp".equals(virCode)) {// ʪ��
							String lowDamp = (String) virDicMap.get("param_low");
							String highDamp = (String) virDicMap.get("param_high");
							resVirMap.put("lowDamp", lowDamp);// �͵�
							resVirMap.put("highDamp", highDamp);// �ߵ�
						}
						if ("illu".equals(virCode)) {// ���ն�
							String lowIllu = (String) virDicMap.get("param_low");
							String highIllu = (String) virDicMap.get("param_high");
							resVirMap.put("lowIllu", lowIllu);// �͵�
							resVirMap.put("highIllu", highIllu);// �ߵ�
						}
						if ("trig".equals(virCode)) {// ����
							String seconds = (String) virDicMap.get("param_low");
							resVirMap.put("seconds", seconds);// ����
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
