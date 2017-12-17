package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.CheckData;
import cn.com.satum.util.PostStyle;

public class LoginService implements AppService{
	private AppBo appBo=new AppBo();
	private CheckData cd=new CheckData();
	public String getInfo(String jsondata){
		System.out.println("===111111111====="+jsondata);
		String flag="S";
		String msg="�ɹ�";
		Map lmap=JSONObject.fromObject(jsondata);
		String username=lmap.get("mobile").toString();
		String pwd=lmap.get("password").toString();
		String sql="select password from sh_user where mobile='"+username+"'";
		List list=appBo.query(sql);
		Map map=new  HashMap();
		if(list.size()>0){	
			Map mup=(Map)list.get(0);
			String pwds=mup.get("password").toString();
			try {
				boolean mark=cd.checkpassword(pwd, pwds);
				if(mark){
					map.put("result",flag);
					map.put("msg",msg);
				}else{
					map.put("result","E");
					map.put("msg","�û������벻ƥ�䣡");
				}
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			/**
			 * 
			 * 
			 * ��ѯ�û��������豸״̬
			 */
		}else{
			map.put("result","E");
			map.put("msg","�û������ڣ����Ƚ���ע�ᡣ");
		}
			JSONObject json=JSONObject.fromObject(map);
		return json.toString();
	}

}
