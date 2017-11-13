package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.util.PostStyle;

public class LoginService implements AppService{
	private AppBo appBo=new AppBo();
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		String flag="S";
		String msg="成功";
		AppBo appBo=new AppBo();
		Map map=new  HashMap();
		map.put("flag",flag);
		map.put("msg",msg);
		/**
		 * 
		 * 登录App接口
		 */
			JSONObject json=JSONObject.fromObject(map);
		return new PostStyle().getBase64("调用接口成功"+json);
	}

}
