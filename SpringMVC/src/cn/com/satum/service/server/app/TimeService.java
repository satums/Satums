package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.service.server.util.DataUtil;

public class TimeService  implements AppService{
	private static AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	private static String time_code="";
	private static String time_name="";
	private static String starts="";
	private static String ends="";
	private static String status="";
	private static String flag="S";
	private static String msg="成功";
	private static String sqls="select * from sh_common_time ";
	private final static String jsondata="{"
			+ "\"flag\":\"update\","
			+ "\"user_code\":\"15738928228\","
			+ "\"time_code\":\"001\","
			+ "\"time_name\":\"上午\","
			+ "\"starts\":\"0\","
			+ "\"ends\":\"10\""	
			+ "}";
	/**
	 * 
	 * 时间设置
	 * 
	 * */
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map map=new  HashMap();
		Map lmap=JSONObject.fromObject(jsondata);
		String mark=(String)lmap.get("flag");
		String user_code=(String)lmap.get("user_code");
		time_code=(String)lmap.get("time_code");
		time_name=(String)lmap.get("time_name");
		starts=(String)lmap.get("starts");
		ends=(String)lmap.get("ends");
		status=(String)lmap.get("status");
	try{		
	sqls+=" where user_code='"+user_code+"' and time_code='"+time_code+"'";
	List list=appBo.query(sqls);
	if(list.size()>0){
		appBo.runSQL("insert into sh_common_time  set time_name='"+time_name+"',starts='"+starts+"',ends='"+ends+"',status='"+status+"'"
				+ "where user_code='"+user_code+"' and time_code='"+time_code+"'");
	}else{
		appBo.runSQL("insert into sh_common_time (id,user_code,time_code,time_name,starts,ends,status) values"
				+ "('"+DataUtil.getUUID()+"','"+user_code+"','"+time_code+"','"+time_name+"','"+starts+"','"+ends+"','"+user_code+"')");
	}
		flag="S";
		msg="成功";
	}catch(Exception e){
				flag="E";
				msg=e.getMessage();
			}		
			
		map.put("flag", flag);
		map.put("msg", msg);
		JSONObject json=JSONObject.fromObject(map);
		return json.toString();	
	}
}
