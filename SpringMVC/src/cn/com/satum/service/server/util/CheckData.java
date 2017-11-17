package cn.com.satum.service.server.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;
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
public static  String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    //确定计算方法
    MessageDigest md5=MessageDigest.getInstance("MD5");
    BASE64Encoder base64en = new BASE64Encoder();
    //加密后的字符串
    String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
    return newstr;
}
/**判断用户密码是否正确
 * @param newpasswd  用户输入的密码
 * @param oldpasswd  数据库中存储的密码－－用户密码的摘要
 * @return
 * @throws NoSuchAlgorithmException
 * @throws UnsupportedEncodingException
 */
public static boolean checkpassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    if(EncoderByMd5(newpasswd).equals(oldpasswd))
        return true;
    else
        return false;
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
