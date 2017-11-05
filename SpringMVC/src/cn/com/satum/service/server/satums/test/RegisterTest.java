package cn.com.satum.service.server.satums.test;

import java.io.IOException;

import cn.com.satum.util.GetStyle;
import cn.com.satum.util.PostStyle;

public class RegisterTest {
public static void main(String[] args) {
	
	String json="{\"username\":\"Ì·Õý±ë\"}";
	String url="http://192.168.1.100:8080/SpringMVC/services/SatumService?wsdl";
	//String urls="http://localhost:8080/SpringMVC/services/SatumService";
	String service="appService";
	String method="ConAdd";
	GetStyle gs=new GetStyle();
	PostStyle ps=new PostStyle();
	String gep=gs.getData(url,service,method,json);
	String pep="";
	
	System.out.println(gep+"--------------000");
}
}
