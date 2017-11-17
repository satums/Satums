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

		String userName = (String) reqmap.get("username");// 用户名
		String passWord = (String) reqmap.get("password");// 密码
		String mobile = (String) reqmap.get("mobile");// 手机号
		String email = (String) reqmap.get("email");// 邮箱
		String areaIdStr = (String) reqmap.get("area_id");// 地区
		int areaId=0;
		if(StringUtils.isNotBlank(areaIdStr)){
			areaId=Integer.valueOf(areaIdStr);
		}
		String proviceIdStr = (String) reqmap.get("provice_id");// 省
		int proviceId=0;
		if(StringUtils.isNotBlank(proviceIdStr)){
			proviceId=Integer.valueOf(proviceIdStr);
		}
		String cityIdStr = (String) reqmap.get("city_id");// 市
		int cityId=0;
		if(StringUtils.isNotBlank(cityIdStr)){
			cityId=Integer.valueOf(cityIdStr);
		}
		String addresss = (String) reqmap.get("addresss");// 详细地址
		String house = (String) reqmap.get("house");// 房屋名称
		String systemPwd = (String) reqmap.get("system_pwd");// 系统密码
		String ip = (String) reqmap.get("ip");// 注册ip
		String hostIdStr = (String) reqmap.get("host_id");// 默认主机id
		int hostId=0;
		if(StringUtils.isNotBlank(hostIdStr)){
			hostId=Integer.valueOf(hostIdStr);
		}
		String skinIdStr = (String) reqmap.get("skin_id");// 皮肤id
		int skinId=0;
		if(StringUtils.isNotBlank(hostIdStr)){
			skinId=Integer.valueOf(skinIdStr);
		}
		String pic = (String) reqmap.get("pic");// 头像地址
		try {
			String salt = CheckData.EncoderByMd5(passWord);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// 密钥
//		int status = (int) reqmap.get("status");// 状态：1禁用2正常
//		int isDel = (int) reqmap.get("is_del");// 数据库是否删除记录:1删除 2正常

		Map<String, Object> resMap = new HashMap<String, Object>();
		try {

			AppBo.runSQL(
					"INSERT INTO sh_user (id,username,password,salt,mobile,email,area_id,provice_id,city_id,addresss,house,system_pwd,status,is_del,ip,host_id,skin_id,pic) VALUES ("
							+ DataUtil.getUUID() + "," + userName + "," + passWord + ",''," + mobile + "," + email + ","
							+ areaId + "," + proviceId + "," + cityId + "," + addresss + "," + house + ",'',"
							+ Integer.valueOf(2) + "," + Integer.valueOf(2) + "," + ip + "," + hostId + "," + skinId
							+ "," + pic + ")");

			resMap.put("result", "S");
			resMap.put("sucmsg", "注册成功！");
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
