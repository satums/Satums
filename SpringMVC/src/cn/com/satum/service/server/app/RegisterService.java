package cn.com.satum.service.server.app;

import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.PostStyle;

public class RegisterService implements AppService{
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		AppBo appBo=new AppBo();
		List list=appBo.query("select * from sh_user where id='"+jsondata+"'");
		Map<String,String> map=(Map<String,String>)list.get(0);
		return "调用接口成功"+map;
	}

}
