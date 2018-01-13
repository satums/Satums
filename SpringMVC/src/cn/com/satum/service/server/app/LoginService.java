package cn.com.satum.service.server.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import sun.misc.BASE64Decoder;
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
		String msg="成功";
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
					map.put("data",queryData(username));
				}else{
					map.put("result","E");
					map.put("msg","用户名密码不匹配！");
				}
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			/**
			 * 
			 * 
			 * 查询用户下所有设备状态
			 */
		}else{
			map.put("result","E");
			map.put("msg","用户不存在，清先进行注册。");
		}
		
			JSONObject json=JSONObject.fromObject(map);
		return json.toString();
	}
public List queryData(String userCode){
List lis=new ArrayList();
	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
	 String ip = request.getHeader("x-forwarded-for");  
     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
         ip = request.getHeader("Proxy-Client-IP");  
     }  
     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
         ip = request.getHeader("WL-Proxy-Client-IP");  
     }  
     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
         ip = request.getHeader("HTTP_CLIENT_IP");  
     }  
     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
         ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
     }  
     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
         ip = request.getRemoteAddr();  
     } 
    String str="http://"+ip+":8080"+request.getContextPath()+"/download/picture?flag=image&id=";
   
	List list=AppBo.query("select t.mobile as userCode,t.username,t.email,t.provice_id,t.city_id,t.area_id,t.address,'#' url from sh_user t where mobile='"+userCode+"'");		
	Map map=(Map) list.get(0);
	 List li=AppBo.query("select id from sh_common_icon where user_id='"+userCode+"'");
	    String ids="";
	    
	    if(li.size()>0){
	    	ids=((Map)li.get(0)).get("id").toString();
	    	str=str+ids;
	    	map.put("url", str);
	    }
	    lis.add(map);
	return list;
}
public static void main(String[] args) throws IOException {
	byte[] b=new BASE64Decoder().decodeBuffer("RTpcU3RhdW1zXGltYWdlXDIwMTgtMDEtMTNc");
	String path=new String(b,"utf-8");
	System.out.println(path);
}
}
