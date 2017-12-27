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
			resMap.put("msg", "��ȡ�����û���Ϣ��");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		// �����û�id��ѯ��������
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> linkList = AppBo
				.query("SELECT scl.id,scl.link_code,scl.`name`,scl.`status` FROM sh_common_link scl WHERE scl.is_del='2' AND scl.user_code="
						+ userID);

		if (linkList != null && linkList.size() > 0) {
			for (Map<String, Object> linkMap : linkList) {
				String likeId = (String) linkMap.get("id");// ����id
				String likeCode = (String) linkMap.get("link_code");// ����code
				String likeName = (String) linkMap.get("name");// ��������
				String likeStatus = (String) linkMap.get("status");// ����״̬
																	// 1���رգ�2����

				// ��������code��ѯ����������
				
			}
		} else {
			resMap.put("result", "S");
			resMap.put("msg", "���û�û�����������");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

		return null;
	}

}
