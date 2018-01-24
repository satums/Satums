package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;

public class SpecialDeviceService implements AppService {
private static AppBo bo=new AppBo();
private static final String queryjson="{"
			 +"\"flag\":\"query\","
			 +"\"userCode\":\"15738928228\","
			 +"\"name\":\"摄像头类\","
			 +"\"type_id\":\"90\""      
			 +"}";
private static final String loginjson="{"
		 +"\"flag\":\"login\","
		 +"\"userCode\":\"15738928228\","
		 +"\"device_id\":\"123132231\","
		 +"\"account\":\"admin\","
		 +"\"password\":\"123456\""      
		 +"}";

	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("flag");
		String json="";
		switch(reqType){
		case "query":
			json=query(jsondata);
			break;
		case "login":
			json=login(jsondata);
			break;
		}
	
		return json;
	}
//特殊设备是查询处理	
public String query(String jsondata){
	Map resMap=new HashMap();
	try{
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String userid=(String) reqMap.get("userCode");
	String type_id=(String) reqMap.get("type_id");
	//String device_id=(String) reqMap.get("device_id");
	//String account=(String) reqMap.get("account");
	//String password=(String) reqMap.get("password");
	List list=bo.query("select * from sh_camer where  usercode='"+userid+"' and type_id='"+type_id+"' and is_del='2'");
	if(list.size()>0){
		resMap.put("result", "S");
		resMap.put("msg", "获取设备类别列表成功！");
		resMap.put("data", list);
	}else{
		resMap.put("result", "E");
		resMap.put("msg", "记录列表为空，请添加。");	
	}
	}catch(Exception e){
		resMap.put("result", "E");
		resMap.put("msg", e.getMessage());	
	}
	JSONObject json = new JSONObject(resMap);
	return json.toString();
}
//特殊设备登陆处理
public String login(String jsondata){
	Map resMap=new HashMap();
	try{
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String userid=(String) reqMap.get("userCode");
	String deviceid=(String) reqMap.get("device_id");
	String account=(String) reqMap.get("account");
	String password=(String) reqMap.get("password");
	String msg="";
	String result="S";
	String sql="select pwd from sh_camer where deviceid='"+deviceid+"' and usercode='"+userid+"' and account='"+account+"'";
	List list=AppBo.query(sql);
	Map map=new  HashMap();
	if(list.size()>0){	
		Map mup=(Map)list.get(0);
		String pwds=mup.get("pwd").toString();
		
			boolean mark=new CheckData().checkpassword(password, pwds);
			if(mark){
				resMap.put("result","S");
				resMap.put("msg","登录成功");				
			}else{
				resMap.put("result","E");
				resMap.put("msg","用户名密码不匹配！");
			}
	}else{
		resMap.put("result", "E");
		resMap.put("msg", "用户名输入有误！");	
	}	
	}catch(Exception e){
		resMap.put("result", "E");
		resMap.put("msg", e.getMessage());	
	}
	JSONObject json = new JSONObject(resMap);
	return json.toString();
}
}
