package cn.com.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.Data.Bo.AppBo;
import cn.com.moudle.user.dao.UserDAO;
import cn.com.moudle.user.entity.User;
import cn.com.moudle.user.mapper.UserMapper;
import cn.com.satum.util.GetStyle;
import cn.com.satum.util.PostStyle;

public class Test1 {
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {
	this.template = template;
}

	public static void main(String[] args) throws SQLException {		
		AppBo bo=new AppBo();
		GetStyle gs=new GetStyle();
		PostStyle ps=new PostStyle();
	String url="http://service.zhahehe.com/services/SatumService?wsdl";
	String services="appService";
	//String method="Login";//登录接口方法
	String method="Register";//注册接口方法
	String jsondata="{\"city_id\":\"2\","
			+ "\"mobile\":\"15738928221\","
			+ "\"username\":\"̷����\","
			+ "\"password\":\"123456\","
			+ "\"salt\":\"1\""
			+ "}";
	System.out.println(jsondata);
String str="";
	try {
		str=ps.postData(url, services, method, jsondata);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Map map=JSONObject.fromObject(str);
	String msg=map.get("msg").toString();
	String flag=map.get("result").toString();
	System.out.println(flag+"============="+msg);
	}
	
}
