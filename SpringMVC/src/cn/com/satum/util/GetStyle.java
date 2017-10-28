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
}
