package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.PostStyle;

public class QueryDataService implements AppService{
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		String flag="S";
		String msg="�ɹ�";
		AppBo appBo=new AppBo();
		Map map=new  HashMap();
		map.put("flag",flag);
		map.put("msg",msg);
		/**
		 * 
		 * ��ѯApp�ӿ�
		 */
			JSONObject json=JSONObject.fromObject(map);
		return new PostStyle().getBase64("���ýӿڳɹ�"+json);
	}
}
