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
 *         联动增、删、改
 */
public class LinkControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("reqType");
		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addLink(reqMap);// 添加联动
		} else if ("update".equals(reqType)) {
			resStr = updateLink(reqMap);// 修改联动
		} else if ("delete".equals(reqType)) {
			resStr = deleteLink(reqMap);// 删除联动
		} else if ("query".equals(reqType)) {
			resStr = queryLink(reqMap);// 删除联动
		}

		return resStr;
	}

	/**
	 * 添加联动
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
			resMap.put("msg", "联动名称不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 插入联动主表
		String linkCode = DataUtil.getUUID();
		AppBo.runSQL("INSERT INTO sh_common_link (id,link_name,link_code,user_code) VALUES('" + DataUtil.getUUID()
				+ "','" + linkName + "','" + linkCode + "','" + userCode + "')");

		// 插入联动条件子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> virList = (List<Map<String, Object>>) reqMap.get("virList");// 获取所有的联动条件
		if (virList != null && virList.size() > 0) {
			for (Map<String, Object> virMap : virList) {
				String virName = (String) virMap.get("envirCode");// 条件名称
				String envirCode = (String) virMap.get("envirCode");// 条件code
				// @SuppressWarnings("unchecked")
				// List<Map<String, Object>> linkDicList = AppBo
				// .query("SELECT sld.`code` from sh_link_dic sld where
				// sld.`name`=" + virName);// 查联动条件字典表查询条件code
				// if (linkDicList != null && linkDicList.size() > 0) {
				// virCode = (String) linkDicList.get(0).get("code");
				// }
				String virType = (String) virMap.get("virType");// 条件类型（0：变为；1：此时正好）
				String virContent = (String) virMap.get("virContent");// 条件内容
				@SuppressWarnings("unchecked")
				Map<String, Object> virParamMap = (Map<String, Object>) virMap.get("virParam");// 条件参数
				String virParam = "";// 存进数据库的条件参数格式
				// 根据条件code获取条件具体参数，条件不同参数就不同
				if ("time".equals(envirCode)) {// 时间
					String startTime = (String) virParamMap.get("startTime");// 开始时间
					String endTime = (String) virParamMap.get("endTime");// 结束时间
					String days = (String) virParamMap.get("days");// 周几
					virParam = startTime + "-" + endTime + " " + days;
				}

				AppBo.runSQL(
						"INSERT INTO sh_link_virsub (id,link_code,vir_code,vir_type,vir_content,vir_param) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + envirCode + "','" + virType + "','"
								+ virContent + "','" + virParam + "')");
			}

		} else {
			resMap.put("result", "E");
			resMap.put("msg", "必须添加一项联动条件项！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		// 插入联动设备子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = (List<Map<String, Object>>) reqMap.get("deviceList");// 获取所有在当前联动的设备列表
		if (deviceList != null && deviceList.size() > 0) {
			for (Map<String, Object> deviceMap : deviceList) {
				String deviceName = (String) deviceMap.get("deviceName");// 设备名称
				String deviceCode = (String) deviceMap.get("deviceCode");// 设备code
				String deviceStatus = (String) deviceMap.get("deviceStatus");// 状态；1：关闭；2：打开
				String deviceContime = (String) deviceMap.get("deviceContime");// 控制时间；0代表立即

				AppBo.runSQL(
						"INSERT INTO sh_link_devicesub (id,link_code,device_name,device_code,device_status,device_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + deviceName + "','" + deviceCode
								+ "','" + deviceStatus + "','" + deviceContime + "')");
			}
		}
		// 插入联动情景子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> sceneList = (List<Map<String, Object>>) reqMap.get("sceneList");// 获取所有在当前联动的情景列表
		if (sceneList != null && sceneList.size() > 0) {
			for (Map<String, Object> sceneMap : sceneList) {
				
				String sceneName = (String) sceneMap.get("sceneName");// 情景名称
				String sceneCode = (String) sceneMap.get("sceneCode");// 情景code
//				String sceneStatus = (String) sceneMap.get("sceneStatus");// 状态
				String sceneContime = (String) sceneMap.get("sceneContime");// 控制时间；0代表立即

				AppBo.runSQL(
						"INSERT INTO sh_link_scenesub (id,link_code,scene_name,scene_code,scene_status,scene_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + sceneName + "','" + sceneCode + "','" + sceneContime + "')");
			}
		}
		if (deviceList.size() == 0 && sceneList.size() == 0) {
			resMap.put("result", "E");
			resMap.put("msg", "必须添加一项联动动作项！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "联动添加成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * 修改联动
	 */

	public String updateLink(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		String linkCode = (String) reqMap.get("LinkCode");
		if (StringUtils.isBlank(linkCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到要修改的联动信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 修改联动主表
		String linkName = (String) reqMap.get("linkName");
		if (StringUtils.isNotBlank(linkName)) {
			AppBo.runSQL("UPDATE sh_common_link SET link_name='" + linkName + "' where link_code='" + linkCode + "'");
		}

		// 修改联动条件子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> virList = (List<Map<String, Object>>) reqMap.get("virList");// 获取所有的联动条件
		if (virList != null && virList.size() > 0) {
			for (Map<String, Object> virMap : virList) {
				String virId = (String) virMap.get("virId");// 条件id
				String conVirType = (String) virMap.get("conVirType");// 操作类型；update：修改；del:删除
				if (StringUtils.isNotBlank(virId)) {
					if ("update".equals(conVirType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> virIdList = AppBo
								.query("SELECT slv.vir_code,slv.vir_type,slv.vir_content,slv.vir_param FROM sh_link_virsub slv where slv.id='"
										+ virId + "'");
						String envirCode = (String) virIdList.get(0).get("vir_code");// 根据条件id获取条件code
						String virType = (String) virIdList.get(0).get("vir_type");// 根据条件id获取条件类型
						String virContent = (String) virIdList.get(0).get("vir_content");// 根据条件id获取条件内容
						String virParam = (String) virIdList.get(0).get("vir_param");// 根据条件id获取条件参数

						if (StringUtils.isNotBlank((String) virMap.get("virType"))) {
							virType = (String) virMap.get("virType");// 条件类型（0：变为；1：此时正好）
						}
						if (StringUtils.isNotBlank((String) virMap.get("virContent"))) {
							virContent = (String) virMap.get("virContent");// 条件类型（0：变为；1：此时正好）
						}
						@SuppressWarnings("unchecked")
						Map<String, Object> virParamMap = (Map<String, Object>) virMap.get("virParam");// 条件参数
						if (!virParamMap.isEmpty()) {
							// 根据条件code获取条件具体参数，条件不同参数就不同
							if ("time".equals(envirCode)) {// 时间
								String startTime = (String) virParamMap.get("startTime");// 开始时间
								if (StringUtils.isNotBlank(startTime)) {
									virParam.replace(virParam.substring(0, virParam.lastIndexOf("-")), startTime);
								}
								String endTime = (String) virParamMap.get("endTime");// 结束时间
								if (StringUtils.isNotBlank(endTime)) {
									virParam.replace(
											virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" ")),
											endTime);
								}
								String days = (String) virParamMap.get("days");// 周几
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
						// 删除联动条件子表
						AppBo.runSQL("UPDATE sh_link_virsub SET is_del='1' WHERE id='" + virId + "'");
					}

				}

			}

		}
		// 修改联动设备子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = (List<Map<String, Object>>) reqMap.get("deviceList");// 获取所有在当前联动的设备列表
		if (deviceList != null && deviceList.size() > 0) {
			for (Map<String, Object> deviceMap : deviceList) {
				String deviceId = (String) deviceMap.get("deviceId");
				String conDeviceType = (String) deviceMap.get("conDeviceType");// 操作类型；update：修改；del:删除
				if (StringUtils.isNotBlank(deviceId)) {
					if ("update".equals(conDeviceType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> deviceIdList = AppBo
								.query("SELECT sld.device_status,sld.device_contime FROM sh_link_devicesub sld where sld.id='"
										+ deviceId + "'");
						String deviceStatus = (String) deviceIdList.get(0).get("device_status");// 根据设备id查询设备状态
						String deviceContime = (String) deviceIdList.get(0).get("device_contime");// 控制时间；0代表立即

						if (StringUtils.isNotBlank((String) deviceMap.get("deviceStatus"))) {
							deviceStatus = (String) deviceMap.get("deviceStatus");
						}
						if (StringUtils.isNotBlank((String) deviceMap.get("deviceContime"))) {
							deviceContime = (String) deviceMap.get("deviceContime");
						}

						AppBo.runSQL("UPDATE sh_link_devicesub SET device_status = '" + deviceStatus
								+ "',device_contime='" + deviceContime + "' where id='" + deviceId + "'");
					} else if ("del".equals(conDeviceType)) {
						// 删除联动设备子表
						AppBo.runSQL("UPDATE sh_link_devicesub SET is_del='1' WHERE id='" + deviceId + "'");
					}

				}
			}
		}
		// 修改联动情景子表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> sceneList = (List<Map<String, Object>>) reqMap.get("sceneList");// 获取所有在当前联动的情景列表
		if (sceneList != null && sceneList.size() > 0) {
			for (Map<String, Object> sceneMap : sceneList) {
				String sceneId = (String) sceneMap.get("sceneId");
				String conSceneType = (String) sceneMap.get("conSceneType");// 操作类型；update：修改；del:删除
				if (StringUtils.isNotBlank(sceneId)) {
					if ("update".equals(conSceneType)) {
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> sceneIdList = AppBo.query(
								"SELECT scene_status,scene_contime FROM sh_link_scenesub where id='" + sceneId + "'");
						String sceneStatus = (String) sceneIdList.get(0).get("scene_status");// 根据情景id查询状态
						String sceneContime = (String) sceneIdList.get(0).get("scene_contime");// 控制时间；0代表立即

						if (StringUtils.isNotBlank((String) sceneMap.get("sceneStatus"))) {
							sceneStatus = (String) sceneMap.get("sceneStatus");
						}
						if (StringUtils.isNotBlank((String) sceneMap.get("sceneContime"))) {
							sceneContime = (String) sceneMap.get("sceneContime");
						}
						AppBo.runSQL("UPDATE sh_link_scenesub SET scene_status='" + sceneStatus + "',scene_contime='"
								+ sceneContime + "' where id='" + sceneId + "'");
					} else if ("del".equals(conSceneType)) {
						// 删除联动情景子表
						AppBo.runSQL("UPDATE sh_link_scenesub SET is_del='1' WHERE id='" + sceneId + "'");
					}
				}
			}
		}
		resMap.put("result", "S");
		resMap.put("msg", "联动修改成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * 删除联动
	 * 
	 * @param reqMap
	 * @return
	 */
	public String deleteLink(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String linkCode = (String) reqMap.get("linkCode");
		// 删除联动条件子表
		AppBo.runSQL("UPDATE sh_link_virsub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// 删除联动设备子表
		AppBo.runSQL("UPDATE sh_link_devicesub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// 删除联动情景子表
		AppBo.runSQL("UPDATE sh_link_scenesub SET is_del='1' WHERE link_code='" + linkCode + "'");
		// 删除联动主表
		AppBo.runSQL("UPDATE sh_common_link SET is_del='1' WHERE link_code='" + linkCode + "'");
		resMap.put("result", "S");
		resMap.put("msg", "联动删除成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}

	/**
	 * 查询联动
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryLink(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// 接口最终返回数据的map
		List<Map<String, Object>> resDataList = new ArrayList<Map<String, Object>>();// 接口最终返回数据的map里的联动list

		String userCode = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 根据用户id查询联动主表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.link_name,scl.link_status FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userCode);
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
						String envirCode = (String) virMap.get("vir_code");// 条件code

						resVirMap.put("virId", virId);
						resVirMap.put("virType", virType);
						resVirMap.put("virContent", virContent);

						// 根据条件code查询条件字典表的条件名称、条件参数（code为time的条件除外，参数在条件表存储）
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> envirList = AppBo
								.query("SELECT envir_name,starts,ends FROM sh_common_envir  WHERE envir_code='"
										+ envirCode + "'");
						Map<String, Object> envirMap = envirList.get(0);
						String envirName = (String) envirMap.get("envir_name");
						resVirMap.put("envirName", envirName);

						if ("time".equals(envirCode)) {// 时间
							String startTime = virParam.substring(0, virParam.lastIndexOf("-"));
							String endTime = virParam.substring(virParam.lastIndexOf("-") + 1, virParam.indexOf(" "));
							String days = virParam.substring(virParam.indexOf(" ") + 1, virParam.length());
							resVirMap.put("startTime", startTime);// 开始时间
							resVirMap.put("endTime", endTime);// 结束时间
							resVirMap.put("days", days);// 周几（以“，”分割的字符串）

						} else {
							String starts = (String) envirMap.get("starts");
							String ends = (String) envirMap.get("ends");
							resVirMap.put("starts", starts);
							resVirMap.put("ends", ends);
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
