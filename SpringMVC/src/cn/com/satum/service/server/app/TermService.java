package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class TermService  implements AppService{
	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String code="";
	private static String name="";
	private static String starts="";
	private static String ends="";
	private static String flag="S";
	private static String msg="成功";
	private static String sqls="select * from sh_common_envir ";
	private final static String jsondata="{"
			+ "\"flag\":\"update\","
			+ "\"user_code\":\"15738928228\","
			+ "\"envir_code\":\"001\","
			+ "\"envir_name\":\"温度\","
			+ "\"device_code\":\"B01\","
			+ "\"device_name\":\"B设备温度触发器\","
			+"\"data\":[{"
			+ "\"code\":\"lower\","
			+ "\"name\":\"低档\","
			+ "\"starts\":\"0\","
			+ "\"ends\":\"10\""	
			+"},{"
			+ "\"code\":\"mid\","
			+ "\"name\":\"中档\","	
			+ "\"starts\":\"11\","
			+ "\"ends\":\"30\""	
			+"},{"
			+ "\"code\":\"high\","
			+ "\"name\":\"高档\","
			+ "\"starts\":\"31\","
			+ "\"ends\":\"40\""	
			+"}]"
			+ "}";
	/**
	 * 
	 * 环境条件设置
	 * 
	 * */
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		
		Map map=new  HashMap();
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");
		String user_code=(String)lmap.get("user_code");
		String envir_code=(String)lmap.get("envir_code");
		String envir_name=(String)lmap.get("envir_name");
		String device_code=(String)lmap.get("device_code");
		String device_name=(String)lmap.get("device_name");
		JSONArray data=JSONObject.fromObject(jsondata).getJSONArray("data");
		Object[] datas=data.toArray();
		sqls+=" where envir_code='"+envir_code+"' and user_code='"+user_code+"'";
		List list=appBo.query(sqls);
		if(list.size()>0){
			for(int i=0;i<datas.length;i++){
				Map dmap=(Map)JSONObject.fromObject(datas[i]);
				code=(String)dmap.get("code");			
				starts=(String)dmap.get("starts");
				ends=(String)dmap.get("ends");
				try{
				appBo.runSQL("update sh_common_envir set starts='"+starts+"',ends='"+ends+"' where user_code='"+user_code+"' and envir_code='"+envir_code+"' and code='"+code+"'");
				}catch(Exception e){
					flag="E";
					msg=e.getMessage();
				}
				}	
		}else{
			for(int i=0;i<datas.length;i++){
				Map dmap=(Map)JSONObject.fromObject(datas[i]);
				code=(String)dmap.get("code");
				name=(String)dmap.get("name");
				starts=(String)dmap.get("starts");
				ends=(String)dmap.get("ends");
				try{
					appBo.runSQL("insert into sh_common_envir (id,user_code,envir_code,envir_name,,device_code,device_name,code,name,starts,ends) values"
							+ "('"+DataUtil.getUUID()+"','"+user_code+"','"+envir_code+"','"+envir_name+"','"+device_code+"','"+device_name+"','"+code+"','"+name+"','"+starts+"','"+ends+"')");
			}catch(Exception e){
				flag="E";
				msg=e.getMessage();
			}		
			}			
		}
		map.put("flag", flag);
		map.put("msg", msg);
		JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
}
