package cn.com.satum.service.server.app;

import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.PostStyle;

public class RegisterService implements AppService{
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		AppBo appBo=new AppBo();
		List list=appBo.query("select max(id) id from sh_user");
	Map map=(Map)list.get(0);
	int id=0;
	id=Integer.valueOf(map.get("id").toString());
	String pa="";
		try{
			appBo.runSQL("INSERT INTO sh_user VALUES ('"+(id+1)+"', '15811137696', '15811137696', '4110357d6bb88fd622c7e08d93dbf4a5', '481162', '', '', '0', '0', '0', '', '谭正彪的家', '0', '0', '1234', '2', '', '2', '2017-07-09 00:21:55', '2017-08-05 17:41:05', null);");
		}catch(Exception e){
		pa.toString();	
		}	
		return "调用接口成功"+map+pa;
	}

}
