package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class RegisterService implements AppService {
	public String getInfo(String jsondata) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> reqmap = JSON.parseObject(jsondata);

		String mobile = (String) reqmap.get("mobile");// 手机号
		if (StringUtils.isBlank(mobile)) {
			resMap.put("result", "E");
			resMap.put("msg", "手机号不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		String userName ="";
		if(reqmap.get("username")!=null){
			userName=(String) reqmap.get("username");// 用户名
		}	
		String passWord = (String) reqmap.get("password");// 密码
		if (StringUtils.isBlank(passWord)) {
			resMap.put("result", "E");
			resMap.put("msg", "密码不能为空！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		String sql = "select password from sh_user where mobile='" + mobile + "'";
		List list = AppBo.query(sql);
		if (list != null && list.size() > 0) {
			resMap.put("result", "E");
			resMap.put("msg", "该手机号已注册！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}
		String email = (String) reqmap.get("email");// 邮箱
		if (StringUtils.isBlank(email)) {
			email = "";
		}
		String areaIdStr = (String) reqmap.get("area_id");// 地区
		int areaId = 0;
		if (StringUtils.isNotBlank(areaIdStr)) {
			areaId = Integer.valueOf(areaIdStr);
		}
		String proviceIdStr = (String) reqmap.get("provice_id");// 省
		int proviceId = 0;
		if (StringUtils.isNotBlank(proviceIdStr)) {
			proviceId = Integer.valueOf(proviceIdStr);
		}
		String cityIdStr = (String) reqmap.get("city_id");// 市
		int cityId = 0;
		if (StringUtils.isNotBlank(cityIdStr)) {
			cityId = Integer.valueOf(cityIdStr);
		}
		String address = (String) reqmap.get("addresss");// 详细地址
		if (StringUtils.isBlank(address)) {
			address = "";
		}
		String house = (String) reqmap.get("house");// 房屋名称
		if (StringUtils.isBlank(house)) {
			house = "";
		}
		String systemPwd = (String) reqmap.get("system_pwd");// 系统密码
		if (StringUtils.isBlank(systemPwd)) {
			systemPwd = "";
		}
		String ip = (String) reqmap.get("ip");// 注册ip
		if (StringUtils.isBlank(ip)) {
			ip = "";
		}
		String hostIdStr = (String) reqmap.get("host_id");// 默认主机id
		int hostId = 0;
		if (StringUtils.isNotBlank(hostIdStr)) {
			hostId = Integer.valueOf(hostIdStr);
		}
		String skinIdStr = (String) reqmap.get("skin_id");// 皮肤id
		int skinId = 0;
		if (StringUtils.isNotBlank(hostIdStr)) {
			skinId = Integer.valueOf(skinIdStr);
		}
		String pic = (String) reqmap.get("pic");// 头像地址
		if (StringUtils.isBlank(pic)) {
			pic = "";
		}
		try {
			if (StringUtils.isNotBlank(passWord))
				passWord = CheckData.EncoderByMd5(passWord);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 密钥
			// int status = (int) reqmap.get("status");// 状态：1禁用2正常
			// int isDel = (int) reqmap.get("is_del");// 数据库是否删除记录:1删除 2正常
		try {

			AppBo.runSQL(
					"INSERT INTO sh_user (id,username,password,salt,mobile,email,area_id,provice_id,city_id,address,house,system_pwd,status,is_del,ip,host_id,skin_id,pic) VALUES ('"
							+ DataUtil.getUUID() + "','" + userName + "','" + passWord + "','','" + mobile + "','"
							+ email + "'," + areaId + "," + proviceId + "," + cityId + ",'" + address + "','" + house
							+ "',''," + Integer.valueOf(2) + "," + Integer.valueOf(2) + ",'" + ip + "'," + hostId + ","
							+ skinId + ",'" + pic + "')");

			resMap.put("result", "S");
			resMap.put("msg", "注册成功！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		} catch (Exception e) {
			resMap.put("result", "E");
			System.out.println("error：" + e.getMessage());
			resMap.put("msg", "系统错误！");
			JSONObject json = new JSONObject(resMap);
			return json.toString();
		}

	}

}
