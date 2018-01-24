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
			 +"\"name\":\"����ͷ��\","
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
//�����豸�ǲ�ѯ����	
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
		resMap.put("msg", "��ȡ�豸����б�ɹ���");
		resMap.put("data", list);
	}else{
		resMap.put("result", "E");
		resMap.put("msg", "��¼�б�Ϊ�գ�����ӡ�");	
	}
	}catch(Exception e){
		resMap.put("result", "E");
		resMap.put("msg", e.getMessage());	
	}
	JSONObject json = new JSONObject(resMap);
	return json.toString();
}
//�����豸��½����
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
				resMap.put("msg","��¼�ɹ�");				
			}else{
				resMap.put("result","E");
				resMap.put("msg","�û������벻ƥ�䣡");
			}
	}else{
		resMap.put("result", "E");
		resMap.put("msg", "�û�����������");	
	}	
	}catch(Exception e){
		resMap.put("result", "E");
		resMap.put("msg", e.getMessage());	
	}
	JSONObject json = new JSONObject(resMap);
	return json.toString();
}
}
