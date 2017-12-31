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
 * 设备增、删、改、查
 */
public class DeviceControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("flag");
		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addDevice(reqMap);// 添加设备
		} else if ("update".equals(reqType)) {
			resStr = updateDevice(reqMap);// 修改设备
		} else if ("delete".equals(reqType)) {
			resStr = deleteDevice(reqMap);// 删除设备
		} else if ("query".equals(reqType)) {
			String type = (String) reqMap.get("type");
			if("1".equals(type)){
				resStr = queryDeviceType(reqMap);// 查询设备类型
			}
			else{
				resStr = queryDevice(reqMap);// 查询设备
			}
		}

		return resStr;
	}

	/**
	 * 添加设备
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
			resMap.put("msg", "基础设备名称不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
	
		String num = (String) reqMap.get("num");
		if (StringUtils.isBlank(num)) {
			resMap.put("result", "E");
			resMap.put("msg", "基础设备编码不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		
		String typeId = (String) reqMap.get("typeId"); //设备类型id
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> commonDeviceList = AppBo
				.query("SELECT * from sh_common_device where name='"+ name +"' AND num = '" + num + "' AND user_code = '" + userCode + "' AND device_type_id = '" + typeId + "' AND is_del='2' ");
		if (commonDeviceList == null ) {
			// 插入基础设备表
			String id = DataUtil.getUUID();
			AppBo.runSQL("INSERT INTO sh_common_device (id,num,name,user_code,device_type_id) VALUES('" + DataUtil.getUUID()
					+ "','" + num + "','" + name + "','" + userCode + "','" + typeId + "')");
		}
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT * from sh_device where num = '" + num + "' AND user_code = '" + userCode + "' AND is_del='2' ");
		
		if (deviceList != null && deviceList.size() > 0) {
			
			resMap.put("result", "E");
			resMap.put("msg", "相关控制设备已经添加，请进行修改操作！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 查询设备类型表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceTypeList = AppBo
				.query("SELECT id,name from sh_common_device_type where parent_id = '" + typeId + "'  AND is_del='2' ");
		
		if (deviceTypeList == null) {//当控制类型level为最底层时
			// 查询设备类型表
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> deviceTypeList1 = AppBo
					.query("SELECT id,name from sh_common_device_type where id = '" + typeId + "'  AND is_del='2' ");
			for (Map<String, Object> deviceTypeMap : deviceTypeList1) {
				String deviceTypeId = (String) deviceTypeMap.get("id");//设备类型id
				String deviceTypeName = (String) deviceTypeMap.get("name");//设备类型名称id
				AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code) values "
						+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"')");
			}
		}
		
		for (Map<String, Object> deviceTypeMap : deviceTypeList) {
			
			String deviceTypeId = (String) deviceTypeMap.get("id");//设备类型id
			String deviceTypeName = (String) deviceTypeMap.get("name");//设备类型名称id
			
			AppBo.runSQL("insert into sh_device (id,user_code,device_type_id,device_type_name,num,name,device_code) values "
					+ "('"+DataUtil.getUUID()+"','"+userCode+"','"+deviceTypeId+"','"+deviceTypeName+"','"+num+"','"+deviceTypeName+"','"+DataUtil.getUUID()+"')");
		} 
		
		
		resMap.put("result", "S");
		resMap.put("msg", "设备添加成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * 修改联动
	 */

	public String updateDevice(Map<String, Object> reqMap) {

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
						String virCode = (String) virIdList.get(0).get("vir_code");// 根据条件id获取条件code
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
							if ("time".equals(virCode)) {// 时间
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

							if ("temp".equals(virCode)) {// 温度
								String lowTemp = (String) virParamMap.get("lowTemp");// 低温
								if (StringUtils.isNotBlank(lowTemp)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowTemp);
								}
								String highTemp = (String) virParamMap.get("highTemp");// 高温
								if (StringUtils.isNotBlank(lowTemp)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highTemp);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowTemp + "',param_high='"
										+ highTemp + "' where `code`='" + virCode + "'");
							}
							if ("damp".equals(virCode)) {// 湿度
								String lowDamp = (String) virParamMap.get("lowDamp");// 低点
								if (StringUtils.isNotBlank(lowDamp)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowDamp);
								}
								String highDamp = (String) virParamMap.get("highDamp");// 高点
								if (StringUtils.isNotBlank(highDamp)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highDamp);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowDamp + "',param_high='"
										+ highDamp + "' where `code`='" + virCode + "'");
							}
							if ("illu".equals(virCode)) {// 光照度
								String lowIllu = (String) virParamMap.get("lowIllu");// 低点
								if (StringUtils.isNotBlank(lowIllu)) {
									virParam.replace(virParam.substring(0, virParam.indexOf(" ")), lowIllu);
								}
								String highIllu = (String) virParamMap.get("highIllu");// 高点
								if (StringUtils.isNotBlank(highIllu)) {
									virParam.replace(virParam.substring(virParam.indexOf(" ") + 1, virParam.length()),
											highIllu);
								}
								AppBo.runSQL("UPDATE sh_link_dic SET param_low='" + lowIllu + "',param_high='"
										+ highIllu + "' where `code`='" + virCode + "'");
							}
							if ("trig".equals(virCode)) {// 触发
								String seconds = (String) virParamMap.get("seconds");// 秒数
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
				String sceneName = (String) sceneMap.get("sceneName");// 情景名称
				String sceneCode = (String) sceneMap.get("sceneCode");// 情景code
				String sceneStatus = (String) sceneMap.get("sceneStatus");// 状态
				String sceneContime = (String) sceneMap.get("sceneContime");// 控制时间；0代表立即

				AppBo.runSQL(
						"INSERT INTO sh_link_devicesub (id,link_code,scene_name,scene_code,scene_status,scene_contime) VALUES ('"
								+ DataUtil.getUUID() + "','" + linkCode + "','" + sceneName + "','" + sceneCode + "','"
								+ sceneStatus + "','" + sceneContime + "')");
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
	public String deleteDevice(Map<String, Object> reqMap) {

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
	 * 查询设备类型
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryDeviceType(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// 接口最终返回数据的map

		String userCode = (String) reqMap.get("userCode");
		String parentId = (String) reqMap.get("parentTypeId");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 根据parent_id查询设备类别
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT id,name FROM sh_common_device_type WHERE is_del='2' AND parent_id="
						+ parentId);
		
		if (deviceList == null)  {
			resMap.put("result", "S");
			resMap.put("msg", "未查询到设备类别！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "获取设备类别列表成功！");
		resMap.put("data", deviceList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}

	/**
	 * 查询设备
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryDevice(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// 接口最终返回数据的map

		String userCode = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 根据userCode查询设备
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deviceList = AppBo
				.query("SELECT id,name FROM sh_device WHERE is_del='2' AND user_code="
						+ userCode);
		
		if (deviceList == null)  {
			resMap.put("result", "S");
			resMap.put("msg", "未查询到设备！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		resMap.put("result", "S");
		resMap.put("msg", "获取设备列表成功！");
		resMap.put("data", deviceList);
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}
}
