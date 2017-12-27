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
			resStr = addLike(reqMap);// �������
		} else if ("update".equals(reqType)) {
			resStr = updateLike(reqMap);// �޸�����
		} else if ("delete".equals(reqType)) {
			resStr = deleteLike(reqMap);// ɾ������
		}

		return resStr;
	}

	// �������
	public String addLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		String likeName = (String) resMap.get("likeName");
		if (StringUtils.isBlank(likeName)) {
			resMap.put("result", "E");
			resMap.put("msg", "�������Ʋ���Ϊ�գ�");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		
		
		

		return null;
	}

	// �޸�����
	public String updateLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		return null;
	}

	// ɾ������
	public String deleteLike(Map<String, Object> reqMap) {

		Map<String, Object> resMap = new HashMap<String, Object>();

		return null;
	}

}
