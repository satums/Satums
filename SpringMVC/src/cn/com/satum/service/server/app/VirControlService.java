package cn.com.satum.service.server.app;

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
 *         环境条件类型增、删、改
 */
public class VirControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("reqType");
		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addEnvir(reqMap);// 添加条件类型
		} else if ("update".equals(reqType)) {
			resStr = updateEnvir(reqMap);// 修改条件类型
		} else if ("delete".equals(reqType)) {
			resStr = deleteEnvir(reqMap);// 删除条件类型
		} else if ("query".equals(reqType)) {
			resStr = queryEnvir(reqMap);// 删除条件类型
		}

		return resStr;
	}

	/**
	 * 添加条件类型
	 * 
	 * @param reqMap
	 * @return
	 */
	public String addEnvir(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String userCode = (String) reqMap.get("userCode");
		String deviceName = (String) reqMap.get("deviceName");
		String deviceCode = (String) reqMap.get("deviceCode");
		String starts = (String) reqMap.get("starts");
		String ends = (String) reqMap.get("ends");

		String envirName = (String) reqMap.get("envirName");
		if (StringUtils.isBlank(envirName)) {
			resMap.put("result", "E");
			resMap.put("msg", "类型名称不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> envirList = AppBo
				.query("SELECT envir_name,starts,ends FROM sh_common_envir where envir_name='" + envirName + "'");
		if (envirList.size() > 0) {
			resMap.put("result", "E");
			resMap.put("msg", "该类型名称已存在！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 插入环境条件表
		String envirCode = DataUtil.getUUID();

		AppBo.runSQL(
				"INSERT INTO sh_common_envir (id,envir_name,envir_code,user_code,device_code,device_name,starts,ends) VALUES('"
						+ DataUtil.getUUID() + "','" + envirName + "','" + envirCode + "','" + userCode + "','"
						+ deviceCode + "','" + deviceName + "','" + starts + "','" + ends + "')");

		resMap.put("result", "S");
		resMap.put("msg", "环境条件添加成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * 修改条件类型
	 */

	public String updateEnvir(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		String envirCode = (String) reqMap.get("envirCode");
		if (StringUtils.isBlank(envirCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到要修改的环境条件类型！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> envirList = AppBo
				.query("SELECT envir_name,starts,ends FROM sh_common_envir where envir_code='" + envirCode + "'");
		String envirName = (String) envirList.get(0).get("envir_name");
		String starts = (String) envirList.get(0).get("starts");
		String ends = (String) envirList.get(0).get("ends");

		if (StringUtils.isNotBlank((String) reqMap.get("envirName"))) {
			envirName = (String) reqMap.get("envirName");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> envirNameList = AppBo
					.query("SELECT envir_name,starts,ends FROM sh_common_envir where envir_name='" + envirName + "'");
			if (envirNameList.size() > 0) {
				resMap.put("result", "E");
				resMap.put("msg", "该类型名称已存在！");
				JSONObject json = new JSONObject(resMap);
				return json.toString();
			}
		}
		if (StringUtils.isNotBlank((String) reqMap.get("starts"))) {
			starts = (String) reqMap.get("starts");
		}
		if (StringUtils.isNotBlank((String) reqMap.get("ends"))) {
			ends = (String) reqMap.get("ends");
		}

		AppBo.runSQL("UPDATE sh_common_envir SET envir_name = '" + envirName + "',ends='" + ends + "',starts='" + starts
				+ "' where envir_code='" + envirCode + "'");

		resMap.put("result", "S");
		resMap.put("msg", "环境条件修改成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();
	}

	/**
	 * 删除条件类型
	 * 
	 * @param reqMap
	 * @return
	 */
	public String deleteEnvir(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String envirCode = (String) reqMap.get("envirCode");
		AppBo.runSQL("UPDATE sh_common_envir SET is_del='1' WHERE envir_code='" + envirCode + "'");

		resMap.put("result", "S");
		resMap.put("msg", "环境条件删除成功！");
		JSONObject json = new JSONObject(resMap);
		return json.toString();

	}

	/**
	 * 查询条件类型
	 * 
	 * @param reqMap
	 * @return
	 */
	public String queryEnvir(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();// 接口最终返回数据的map

		String userCode = (String) reqMap.get("userCode");
		if (StringUtils.isBlank(userCode)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> envirList = AppBo
				.query("SELECT sce.user_code,sce.envir_code,sce.envir_name,sce.device_code,sce.device_name,sce.`starts`,sce.`ends` FROM sh_common_envir sce where sce.is_del='2' and sce.user_code='"
						+ userCode + "'");

		if (envirList != null && envirList.size() > 0) {
			resMap.put("result", "S");
			resMap.put("msg", "获取环境条件列表成功！");
			resMap.put("data", envirList);
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		} else {
			resMap.put("result", "S");
			resMap.put("msg", "该用户没有添加环境条件！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

	}
}
