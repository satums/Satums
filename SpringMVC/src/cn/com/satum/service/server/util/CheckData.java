package cn.com.satum.service.server.util;

import cn.com.satum.service.server.app.AppService;
import cn.com.satum.service.server.master.MasterService;
import cn.com.satum.util.Register;

public class CheckData {
	private   MasterService masterService;
	private  AppService appService;
public  String  appService(String param,String jsondata){
	//AppService service;
	String rep="";
	try {
		appService=(AppService)Register.toBean("cn.com.satum.service.server.app."+param+"Service");
		
		rep=appService.getInfo(jsondata);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return rep;
}
public  String masterService(String param,String jsondata){
	MasterService service;
	String rep="";
	try {
		masterService=(MasterService)Register.toBean("cn.com.satum.service.server.master."+param+"Service");
		rep=masterService.getInfo(jsondata);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return rep;
}

}
