package cn.com.satum.service.server.app;

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

		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> reqMap = JSON.parseObject(jsondata);

		String userID = (String) reqMap.get("userId");
		if (StringUtils.isBlank(userID)) {
			resMap.put("result", "E");
			resMap.put("msg", "获取不到用户信息！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// 根据用户id查询联动主表
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.`name`,scl.`status` FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userID);

		if (linkList != null && linkList.size() > 0) {
			for (Map<String, Object> linkMap : linkList) {
				String likeId = (String) linkMap.get("id");// 联动id
				String likeCode = (String) linkMap.get("link_code");// 联动code
				String likeName = (String) linkMap.get("name");// 联动名称
				String likeStatus = (String) linkMap.get("status");// 联动状态
																	// 1：关闭；2：打开

				// 根据联动code查询联动条件表
				
			}
		} else {
			resMap.put("result", "S");
			resMap.put("msg", "该用户没有添加联动！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		return null;
	}

}
