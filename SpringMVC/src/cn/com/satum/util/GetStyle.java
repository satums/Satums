package cn.com.satum.util;

import java.rmi.RemoteException;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class GetStyle {
	public String getData(String url,String services,String method,String jsondata){
		// String endpoint = "http://localhost:8080/services/SatumService?wsdl";    
	     Service service = new Service();  
	     Call call;
	     String result="";	
		try {
			call = (Call) service.createCall();
	     call.setTargetEndpointAddress(url);  
	     call.setOperationName(services);  
	     // 接口方法的参数名, 参数类型,参数模式  IN(输入), OUT(输出) or INOUT(输入输出)  
	     call.addParameter("param", XMLType.XSD_STRING, ParameterMode.IN);
	    call.addParameter("jsondata", XMLType.XSD_STRING, ParameterMode.IN);  
	     // 设置被调用方法的返回值类型  
	     call.setReturnType(XMLType.XSD_STRING);  
	     //设置方法中参数的值  
	     Object[] paramValues = new Object[] {method,jsondata};  
	     // 给方法传递参数，并且调用方法      
			result = (String) call.invoke(paramValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	   
	     return result;	
	}  
}
