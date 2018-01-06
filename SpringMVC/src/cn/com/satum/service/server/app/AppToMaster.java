package cn.com.satum.service.server.app;

import cn.com.Data.Bo.AppBo;

public class AppToMaster {
	private static AppBo bo=new AppBo();
public String Control(String userid,String code,String type){
	String json="";
	if(type.equals("secene")){
		json=SeceneQuery(userid,code);
	}else{
		json=LinkQuery(userid,code);
	}
	return json;
}
public static String SeceneQuery(String userid,String code){
	return null;
}
public static String LinkQuery(String userid,String code){
	return null;
}
public static String DeviceUpdate(String code,String status){
	String sql="update sh_device set status='"+status+"' where id='"+code+"'";
	bo.runSQL(sql);
	//调用App接口告诉前端该设备开启，动态改变状态。
	
	return "S";
}
}
