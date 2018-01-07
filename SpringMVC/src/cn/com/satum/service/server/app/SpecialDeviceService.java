package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.Data.Bo.AppBo;

public class SpecialDeviceService implements AppService {
private static AppBo bo=new AppBo();
private static final String json="{"
			 +"\"flag\":\"query\","
			 +"\"userCode\":\"15738928228\","
			 +"\"name\":\"摄像头类\","
			 +"\"typeId\":\"90\""      
			 +"}";

	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("flag");
		String json="";
		if(reqType.equals("query")){
			json=query(jsondata);
		}
		return json;
	}
//特殊设备是查询处理	
public String query(String jsondata){
	Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String userid=(String) reqMap.get("userCode");
	String typeid=(String) reqMap.get("typeId");
	String msg="";
	String result="S";
	Map resMap=new HashMap();
	List list=bo.query("select * from sh_camer where  usercode='"+userid+"' and type_id='"+typeid+"'");
	if(list.size()>0){
		resMap.put("result", "S");
		resMap.put("msg", "获取设备类别列表成功！");
		resMap.put("data", list);
	}else{
		resMap.put("result", "S");
		resMap.put("msg", "您的添加记录为空，请先添加设备。");	
	}
	JSONObject json = new JSONObject(resMap);
	return json.toString();
}
//特殊设备登陆处理
public String login(String userid,String typeid,String num,String account,String pwd){
	return null;
}
}
