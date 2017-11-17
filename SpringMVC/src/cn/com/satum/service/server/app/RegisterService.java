package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.util.PostStyle;

public class RegisterService implements AppService {
	public String getInfo(String jsondata) {

		JSONObject jsStr = JSONObject.parseObject(jsondata);

		String userName = (String) jsStr.getString("username");// �û���
		String passWord = (String) jsStr.getString("password");// ����
		String salt = (String) jsStr.getString("salt");// ��Կ
		String mobile = (String) jsStr.getString("mobile");// �ֻ���
		String email = (String) jsStr.getString("email");// ����
		int areaId = (int) jsStr.getInteger("area_id");// ����
		int proviceId = (int) jsStr.getInteger("provice_id");// ʡ
		int cityId = (int) jsStr.getInteger("city_id");// ��
		String address = (String) jsStr.getString("address");// ��ϸ��ַ
		String house = (String) jsStr.getString("house");// ��������
		String systemPwd = (String) jsStr.getString("system_pwd");// ϵͳ����
		int status = (int) jsStr.getInteger("status");// ״̬��1����2����
		int isDel = (int) jsStr.getInteger("is_del");// ���ݿ��Ƿ�ɾ����¼:1ɾ�� 2����
		String ip = (String) jsStr.getString("ip");// ע��ip
		int hostId = (int) jsStr.getInteger("host_id");// Ĭ������id
		int skinId = (int) jsStr.getInteger("skin_id");// Ƥ��id
		String pic = (String) jsStr.getString("pic");// ͷ���ַ

		Map<String, Object> reqMap = new HashMap<String, Object>();
		try {

			AppBo.runSQL(
					"INSERT INTO sh_user (id,username,password,salt,mobile,email,area_id,provice_id,city_id,address,house,system_pwd,status,is_del,ip,host_id,skin_id,pic) VALUES ("
							+ DataUtil.getUUID() + "," + userName + "," + passWord + ",''," + mobile + "," + email + ","
							+ areaId + "," + proviceId + "," + cityId + "," + address + "," + house + ",'',"
							+ Integer.valueOf(2) + "," + Integer.valueOf(2) + "," + ip + "," + hostId + "," + skinId
							+ "," + pic + ")");

			reqMap.put("result", "S");
			reqMap.put("sucmsg", "ע��ɹ���");
			JSONObject json = new JSONObject(reqMap);
			return json.toString();
		} catch (Exception e) {
			reqMap.put("result", "E");
			reqMap.put("errmsg", e.getMessage());
			JSONObject json = new JSONObject(reqMap);
			return json.toString();
		}

	}

}
