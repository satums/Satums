package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class LinkControlService implements AppService {

	@Override
	public String getInfo(String jsondata) {

		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("reqType");

		String resStr = "";
		if ("add".equals(reqType)) {
			resStr = addLike(reqMap);// 添加联动
		} else if ("update".equals(reqType)) {
			resStr = updateLike(reqMap);// 修改联动
		} else if ("delete".equals(reqType)) {
			resStr = deleteLike(reqMap);// 删除联动
		}

		return resStr;
	}

	// 添加联动
	public String addLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String likeName = (String) resMap.get("likeName");
		if (StringUtils.isBlank(likeName)) {
			resMap.put("result", "E");
			resMap.put("msg", "联动名称不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		
		
		

		return null;
	}

	// 修改联动
	public String updateLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		return null;
	}

	// 删除联动
	public String deleteLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		return null;
	}

}
