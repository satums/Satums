package cn.com.satum.service.server.satums.test;

import java.io.IOException;

import cn.com.satum.util.GetStyle;
import cn.com.satum.util.PostStyle;

public class RegisterTest {
public static void main(String[] args) {
	
	String json="2";
	String url="http://localhost:8080/SpringMVC/services/SatumService?wsdl";
	String urls="http://localhost:8080/SpringMVC/services/SatumService";
	String service="appService";
	String method="Register";
	GetStyle gs=new GetStyle();
	PostStyle ps=new PostStyle();
	String gep=gs.getData(url,service,method,json);
	String pep="";
	try {
		pep = ps.postData(url,service,method,json,true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(gep+"--------------000"+pep);
}
}
