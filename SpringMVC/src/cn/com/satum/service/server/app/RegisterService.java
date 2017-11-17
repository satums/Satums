package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class RegisterService implements AppService {
	public String getInfo(String jsondata) {

		Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(jsondata);

		String userName = (String) reqmap.get("username");// �û���
		String passWord = (String) reqmap.get("password");// ����
		String mobile = (String) reqmap.get("mobile");// �ֻ���
		String email = (String) reqmap.get("email");// ����
		String areaIdStr = (String) reqmap.get("area_id");// ����
		int areaId=0;
		if(StringUtils.isNotBlank(areaIdStr)){
			areaId=Integer.valueOf(areaIdStr);
		}
		String proviceIdStr = (String) reqmap.get("provice_id");// ʡ
		int proviceId=0;
		if(StringUtils.isNotBlank(proviceIdStr)){
			proviceId=Integer.valueOf(proviceIdStr);
		}
		String cityIdStr = (String) reqmap.get("city_id");// ��
		int cityId=0;
		if(StringUtils.isNotBlank(cityIdStr)){
			cityId=Integer.valueOf(cityIdStr);
		}
		String addresss = (String) reqmap.get("addresss");// ��ϸ��ַ
		String house = (String) reqmap.get("house");// ��������
		String systemPwd = (String) reqmap.get("system_pwd");// ϵͳ����
		String ip = (String) reqmap.get("ip");// ע��ip
		String hostIdStr = (String) reqmap.get("host_id");// Ĭ������id
		int hostId=0;
		if(StringUtils.isNotBlank(hostIdStr)){
			hostId=Integer.valueOf(hostIdStr);
		}
		String skinIdStr = (String) reqmap.get("skin_id");// Ƥ��id
		int skinId=0;
		if(StringUtils.isNotBlank(hostIdStr)){
			skinId=Integer.valueOf(skinIdStr);
		}
		String pic = (String) reqmap.get("pic");// ͷ���ַ
		try {
			String salt = CheckData.EncoderByMd5(passWord);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// ��Կ
//		int status = (int) reqmap.get("status");// ״̬��1����2����
//		int isDel = (int) reqmap.get("is_del");// ���ݿ��Ƿ�ɾ����¼:1ɾ�� 2����

		Map<String, Object> resMap = new HashMap<String, Object>();
		try {

			AppBo.runSQL(
					"INSERT INTO sh_user (id,username,password,salt,mobile,email,area_id,provice_id,city_id,addresss,house,system_pwd,status,is_del,ip,host_id,skin_id,pic) VALUES ("
							+ DataUtil.getUUID() + "," + userName + "," + passWord + ",''," + mobile + "," + email + ","
							+ areaId + "," + proviceId + "," + cityId + "," + addresss + "," + house + ",'',"
							+ Integer.valueOf(2) + "," + Integer.valueOf(2) + "," + ip + "," + hostId + "," + skinId
							+ "," + pic + ")");

			resMap.put("result", "S");
			resMap.put("sucmsg", "ע��ɹ���");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		} catch (Exception e) {
			resMap.put("result", "E");
			resMap.put("errmsg", e.getMessage());
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

	}

}
