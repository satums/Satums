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

		String userName = (String) jsStr.getString("username");// 用户名
		String passWord = (String) jsStr.getString("password");// 密码
		String salt = (String) jsStr.getString("salt");// 密钥
		String mobile = (String) jsStr.getString("mobile");// 手机号
		String email = (String) jsStr.getString("email");// 邮箱
		int areaId = (int) jsStr.getInteger("area_id");// 地区
		int proviceId = (int) jsStr.getInteger("provice_id");// 省
		int cityId = (int) jsStr.getInteger("city_id");// 市
		String address = (String) jsStr.getString("address");// 详细地址
		String house = (String) jsStr.getString("house");// 房屋名称
		String systemPwd = (String) jsStr.getString("system_pwd");// 系统密码
		int status = (int) jsStr.getInteger("status");// 状态：1禁用2正常
		int isDel = (int) jsStr.getInteger("is_del");// 数据库是否删除记录:1删除 2正常
		String ip = (String) jsStr.getString("ip");// 注册ip
		int hostId = (int) jsStr.getInteger("host_id");// 默认主机id
		int skinId = (int) jsStr.getInteger("skin_id");// 皮肤id
		String pic = (String) jsStr.getString("pic");// 头像地址

		Map<String, Object> reqMap = new HashMap<String, Object>();
		try {

			AppBo.runSQL(
					"INSERT INTO sh_user (id,username,password,salt,mobile,email,area_id,provice_id,city_id,address,house,system_pwd,status,is_del,ip,host_id,skin_id,pic) VALUES ("
							+ DataUtil.getUUID() + "," + userName + "," + passWord + ",''," + mobile + "," + email + ","
							+ areaId + "," + proviceId + "," + cityId + "," + address + "," + house + ",'',"
							+ Integer.valueOf(2) + "," + Integer.valueOf(2) + "," + ip + "," + hostId + "," + skinId
							+ "," + pic + ")");

			reqMap.put("result", "S");
			reqMap.put("sucmsg", "注册成功！");
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
