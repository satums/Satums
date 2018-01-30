package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import net.sf.json.JSONObject;

public class AppMasterControlService implements AppService{
private final String json="{"
		+ "\"flag\":\"query\","
		+ "\"userCode\":\"15738928228\""
		+ "}";
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map map=JSONObject.fromObject(jsondata);
		Map retMap=new HashMap();
		if(map.get("flag")==null){
			retMap.put("result", "E");
			retMap.put("msg", "�������Ͳ���Ϊ��");
			String str=JSONObject.fromObject(retMap).toString();
			return str;
		}else if(map.get("userCode")==null){
			retMap.put("result", "E");
			retMap.put("msg", "�û��Ų���Ϊ��");
			String str=JSONObject.fromObject(retMap).toString();
			return str;
		}
		String flag=map.get("flag").toString();
		String userCode=map.get("userCode").toString();
		String ret="";
		if(flag.equals("query")){
			ret=query(userCode);
		}else if(flag.equals("update")){
			ret=update(jsondata,userCode);
		}
		return ret;
	}
public String query(String userCode){
	String sql="select user_code,zjbh,content from sh_masterdata where user_code='"+userCode+"' and b2='2'";
	Map map=new HashMap();
	try {
		List list=AppBo.query(sql);
		if(list.size()>0){
			map.put("result", "S");
			map.put("data", list);
			map.put("msg","��ȡ�����б�ɹ�");
		}else{
			map.put("result", "S");
			map.put("data", "");
			map.put("msg","����û�����������������ӡ�");
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		map.put("result", "E");
		map.put("msg",e.getMessage());
		e.printStackTrace();
	}
	String ret=JSONObject.fromObject(map).toString();
	return ret;
}
public String update(String jsondata,String userCode){
	Map map=new HashMap();
	Map maps=JSONObject.fromObject(jsondata);
	if(maps.get("content")==null){
		map.put("result", "E");
		map.put("msg", "������������Ϊ��");
		String str=JSONObject.fromObject(map).toString();
		return str;
	}else{
		String content=maps.get("content").toString();
		String zjbh=maps.get("zjbh").toString();
	String sql="select user_code,zjbh,content from sh_masterdata where user_code='"+userCode+"'and zjbh='"+zjbh+"' and b2='2'";
	
	try {
		List list=AppBo.query(sql);
		if(list.size()>0){
			AppBo.runSQL("update sh_masterdata set content='"+content+"' where zjbh='"+zjbh+"'");
			map.put("result", "S");
			map.put("msg","�����޸ĳɹ�");
		}else{
			map.put("result", "E");
			map.put("msg","���������ڣ����Ƚ������");
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		map.put("result", "E");
		map.put("msg",e.getMessage());
		e.printStackTrace();
	}
	String ret=JSONObject.fromObject(map).toString();
	return ret;
	}
}
}
