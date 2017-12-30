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

		Map<String, Object> resMap = new HashMap<String, Object>();// 接口最终返回数据的map
		List<Map<String, Object>> resDataList = new ArrayList<Map<String, Object>>();// 接口最终返回数据的map里的联动list

		Map<String, Object> reqMap = JSON.parseObject(jsondata);

		String userID = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userID)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 根据用户id查询联动主表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.link_name,scl.link_status FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userID);
		// 所有的联动
		if (linkList != null && linkList.size() > 0) {
			for (Map<String, Object> linkMap : linkList) {
				Map<String, Object> resLinkMap = new HashMap<String, Object>();// 接口最终返回数据的map里的联动list的单个联动map
				// 单个联动
				String linkCode = (String) linkMap.get("link_code");// 联动code
				String linkName = (String) linkMap.get("link_name");// 联动名称
				String linkStatus = (String) linkMap.get("link_status");// 联动状态1：关闭；2：打开

				resLinkMap.put("linkCode", linkCode);
				resLinkMap.put("linkName", linkName);
				resLinkMap.put("linkStatus", linkStatus);

				// 根据联动code查询联动条件子表
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> virList = AppBo
						.query("SELECT slv.id,slv.vir_code,slv.vir_type,slv.vir_content,slv.vir_param FROM sh_link_virsub slv WHERE slv.link_code='"
								+ linkCode + "'");
				// 单个联动里面所有的条件
				List<Map<String, Object>> resVirList = new ArrayList<Map<String, Object>>();// 接口最终返回数据的map里的联动list的单个联动map里的条件list
				if (virList != null && virList.size() > 0) {
					for (Map<String, Object> virMap : virList) {
						Map<String, Object> resVirMap = new HashMap<>();// 接口最终返回数据的map里的联动list的单个联动map里的条件list里的单个条件map
						String virId = (String) virMap.get("id");// 条件id
						String virType = (String) virMap.get("vir_type");// 条件类型
						String virContent = (String) virMap.get("vir_content");// 条件内容
						String virParam = (String) virMap.get("vir_param");// 条件参数（只在code为time的情况用到）
						String virCode = (String) virMap.get("vir_code");// 条件code

						resVirMap.put("virId", virId);
						resVirMap.put("virType", virType);
						resVirMap.put("virContent", virContent);

						// 根据条件code查询条件字典表的条件名称、条件参数（code为time的条件除外，参数在条件表存储）
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> virDicList = AppBo
								.query("SELECT sld.`name`,sld.param_low,sld.param_high FROM sh_link_dic sld WHERE sld.`code`='"
										+ virCode + "'");
						Map<String, Object> virDicMap = virDicList.get(0);
						String virName = (String) virDicMap.get("name");
						resVirMap.put("virName", virName);

						if ("time".equals(virCode)) {// 时间
							String startTime = virParam.substring(0, virParam.lastIndexOf("-"));
							String endTime = virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" "));
							String days = virParam.substring(virParam.indexOf(" ") + 1, virParam.length());
							resVirMap.put("startTime", startTime);// 开始时间
							resVirMap.put("endTime", endTime);// 结束时间
							resVirMap.put("days", days);// 周几（以“，”分割的字符串）

						}
						if ("temp".equals(virCode)) {// 温度
							String lowTemp = (String) virDicMap.get("param_low");
							String highTemp = (String) virDicMap.get("param_high");
							resVirMap.put("lowTemp", lowTemp);// 低温
							resVirMap.put("highTemp", highTemp);// 高温
						}
						if ("damp".equals(virCode)) {// 湿度
							String lowDamp = (String) virDicMap.get("param_low");
							String highDamp = (String) virDicMap.get("param_high");
							resVirMap.put("lowDamp", lowDamp);// 低点
							resVirMap.put("highDamp", highDamp);// 高点
						}
						if ("illu".equals(virCode)) {// 光照度
							String lowIllu = (String) virDicMap.get("param_low");
							String highIllu = (String) virDicMap.get("param_high");
							resVirMap.put("lowIllu", lowIllu);// 低点
							resVirMap.put("highIllu", highIllu);// 高点
						}
						if ("trig".equals(virCode)) {// 触发
							String seconds = (String) virDicMap.get("param_low");
							resVirMap.put("seconds", seconds);// 秒数
						}
						resVirList.add(resVirMap);
					}
				}
				resLinkMap.put("virList", resVirList);// 返回单个联动里所有的条件list

				// 根据联动code查询联动设备子表
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> deviceList = AppBo
						.query("SELECT sld.id,sld.device_name,sld.device_status,sld.device_contime FROM sh_link_devicesub sld where sld.link_code='"
								+ linkCode + "'");
				// 单个联动里面所有的设备
				List<Map<String, Object>> resDeviceList = new ArrayList<Map<String, Object>>();// 接口最终返回数据的map里的联动list的单个联动map里的设备list
				if (deviceList != null && deviceList.size() > 0) {
					for (Map<String, Object> deviceMap : deviceList) {
						Map<String, Object> resDeviceMap = new HashMap<>();// 接口最终返回数据的map里的联动list的单个联动map里的条件list里的单个设备map
						resDeviceMap.put("deviceId", deviceMap.get("id"));
						resDeviceMap.put("deviceName", deviceMap.get("device_name"));// 设备名称
						resDeviceMap.put("deviceStatus", deviceMap.get("device_status"));// 设备状态
						resDeviceMap.put("deviceContime", deviceMap.get("device_contime"));// 设备控制时间
						resDeviceList.add(resDeviceMap);
					}
				}
				resLinkMap.put("deviceList", resDeviceList);// 返回的单个联动里所有的设备list

				// 根据联动code查询联动情景子表
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> sceneList = AppBo
						.query("SELECT sls.id,sls.scene_name,sls.scene_status,sls.scene_contime FROM sh_link_scenesub sls where sls.link_code='"
								+ linkCode + "'");
				// 单个联动里面所有的情景
				List<Map<String, Object>> resSceneList = new ArrayList<Map<String, Object>>();// 接口最终返回数据的map里的联动list的单个联动map里的情景list
				if (sceneList != null && sceneList.size() > 0) {
					for (Map<String, Object> sceneMap : sceneList) {
						Map<String, Object> resSceneMap = new HashMap<>();// 接口最终返回数据的map里的联动list的单个联动map里的条件list里的单个条件map
						resSceneMap.put("sceneId", sceneMap.get("id"));
						resSceneMap.put("sceneName", sceneMap.get("scene_name"));// 情景名称
						resSceneMap.put("sceneStatus", sceneMap.get("scene_status"));// 情景状态
						resSceneMap.put("sceneContime", sceneMap.get("scene_contime"));// 情景控制时间
						resSceneList.add(resSceneMap);
					}
				}
				resLinkMap.put("deviceList", resSceneList);// 返回的单个联动里所有的情景list

				resDataList.add(resLinkMap);
			}

		} else {
			resMap.put("result", "S");
			resMap.put("msg", "该用户没有添加联动！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "获取联动列表成功！");
		resMap.put("data", resDataList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}

}
