package cn.com.satum.util;

import java.rmi.RemoteException;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class GetStyle {
	public String getData(String url,String services,String method,String jsondata){
		 //url = "http://10.2.25.215:8089/services/erpservic?wsdl";  
		//services="startService";
	//	method="Gys";
		jsondata="{\"username\":\"tata\"}";
	     Service service = new Service();  
	     Call call;
	     String result="";	
		try {
			call = (Call) service.createCall();
	     call.setTargetEndpointAddress(url);  
	     call.setOperationName(services);  
	     // �ӿڷ����Ĳ�����, ��������,����ģʽ  IN(����), OUT(���) or INOUT(�������)  
	     call.addParameter("param", XMLType.XSD_STRING, ParameterMode.IN);
	    call.addParameter("jsondata", XMLType.XSD_STRING, ParameterMode.IN);  
	     // ���ñ����÷����ķ���ֵ����  
	     call.setReturnType(XMLType.XSD_STRING);  
	     //���÷����в�����ֵ  
	     Object[] paramValues = new Object[] {method,jsondata};  
	     // ���������ݲ��������ҵ��÷���      
			result = (String) call.invoke(paramValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	   
	     return result;	
	}
	/**
	public static void main(String[] args) {
		 String url = "http://10.2.25.215:8089/services/erpservice?wsdl";  
			String services="startService";
			String method="Gys";
			String jsondata="{\"username\":\"tata\"}";
		     Service service = new Service();  
		     Call call;
		     String result="";	
			try {
				call = (Call) service.createCall();
		     call.setTargetEndpointAddress(url);  
		     call.setOperationName(services);  
		     // �ӿڷ����Ĳ�����, ��������,����ģʽ  IN(����), OUT(���) or INOUT(�������)  
		     call.addParameter("param", XMLType.XSD_STRING, ParameterMode.IN);
		    call.addParameter("jsondata", XMLType.XSD_STRING, ParameterMode.IN);  
		     // ���ñ����÷����ķ���ֵ����  
		     call.setReturnType(XMLType.XSD_STRING);  
		     //���÷����в�����ֵ  
		     Object[] paramValues = new Object[] {method,jsondata};  
		     // ���������ݲ��������ҵ��÷���      
				result = (String) call.invoke(paramValues);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println(result);
	}**/
}
