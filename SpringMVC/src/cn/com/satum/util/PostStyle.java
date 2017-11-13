package cn.com.satum.util;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class PostStyle {

		 public static String getBase64(String str) {  
	         byte[] b = null;  
	        String s = null;  
	         try {  
	             b = str.getBytes("utf-8");  
	         } catch (UnsupportedEncodingException e) {  
	             e.printStackTrace();  
	         }  
	         if (b != null) {  
	             s = new BASE64Encoder().encode(b);  
	         }  
	         return s;  
	     }  
	public static String getFromBase64(String s) {  
	    byte[] b = null;  
	     String result = null;  
	         if (s != null) {  
	             BASE64Decoder decoder = new BASE64Decoder();  
	             try {  
	                 b = decoder.decodeBuffer(s);  
	                 result = new String(b, "utf-8");  
	             } catch (Exception e) {  
	                 e.printStackTrace();  
	             }  
	         }  
	         return result;  
	     }  	
		@SuppressWarnings("unchecked")
		public static String postData(String url,String service,String method,List list,boolean flag) throws IOException {
			String param="";
			StringBuffer sbf=new StringBuffer();
			sbf.append("Data:[");
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				JSONObject json=JSONObject.fromObject(map);
				sbf.append(json);
				
				if(i==list.size()-1){
					sbf.append("]");
					
				}else{
					sbf.append(",");
				}
			}
			param=sbf.toString();
			//System.out.println(param);
			//使用加密
			if(flag){
				param=getBase64(param);	
			}
			
			  URL wsUrl = new URL(url);    
		        HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();   		
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json;");
		    String soap = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hder=\"http://hderp.server.service.ehl.com.cn\">"
	        		+" <soapenv:Header/>"             
	               
	+"<soapenv:Body>"
	  +"<hder:"+service+" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
	     +"<services xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"+method+"</services>"
	     +"<json xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"+param+
	     "</json>"+
	     " </hder:"+service+">"
	                      + " </soapenv:Body> "
	                      + "</soapenv:Envelope>";
		    conn.setRequestProperty("SOAPAction", service);
		    conn.setRequestProperty("Content-Language", "en-US");
		   
		    conn.setUseCaches(false);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    // Send request
		    OutputStreamWriter wr = new
		    OutputStreamWriter(conn.getOutputStream());
		    wr.write(soap.toCharArray());
		    wr.flush();
		    wr.close();
		    conn.connect(); 
		    // Get Response
		    String str="";
		    InputStream is = null;
		    BufferedReader rd = null;
		    StringBuffer response = new StringBuffer();
		    try {
		    if (conn.getResponseCode() >= 400) {
		    is = conn.getErrorStream();
		    } else {
		    is = conn.getInputStream();
		    }
		    rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    while ((line = rd.readLine()) != null) {
		  		    response.append(line);
		    }
		    System.out.println(response.toString());
		    String s=response.toString().split(">")[4];  
		    str=s.split("<")[0];
		    } catch (IOException ioe) {
		    response.append(ioe.getMessage());
		    } finally {
		    if (is != null) {
		    try {
		    is.close();
		    } catch (Exception e) {
		    }
		    }
		    if (rd != null) {
		    try {
		    rd.close();
		    } catch (Exception e) {
		    }
		    }
		    }
			return str;
		 }
		public static String postData(String url,String service,String method,String param,boolean flag) throws IOException {	
			if(flag){
				  URL wsUrl = new URL(url);    
			        HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();   		
			    conn.setRequestMethod("POST");
			    conn.setRequestProperty("Content-Type", "application/json;");
			    String soap = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:util=\"http://util.server.service.satum.com.cn\">"
		        		+" <soapenv:Header/>"             
		               
		+"<soapenv:Body>"
		  +"<util:"+service+" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		     +"<param xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"+method+"</param>"
		     +"<jsondata xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"+param+
		     "</jsondata>"+
		     " </util:"+service+">"
		                      + " </soapenv:Body> "
		                      + "</soapenv:Envelope>";
			   
			    conn.setRequestProperty("SOAPAction",service);
			    conn.setRequestProperty("Content-Language", "en-US");
			   System.out.println(soap);
			    conn.setUseCaches(false);
			    conn.setDoInput(true);
			    conn.setDoOutput(true);
			    // Send request
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(soap.toCharArray());
			    wr.flush();
			    wr.close(); 
			    conn.connect(); 
			    // Get Response
			    String str="";
			    InputStream is = null;
			    BufferedReader rd = null;
			    StringBuffer response = new StringBuffer();
			    try {
			    if (conn.getResponseCode() >= 400) {
			    is = conn.getErrorStream();
			    } else {
			    is = conn.getInputStream();
			    }
			    rd = new BufferedReader(new InputStreamReader(is));
			    String line;
			    while ((line = rd.readLine()) != null) {
			    	 
			    response.append(line);  
			    response.append('\n');
			   
			    String s=response.toString().split(">")[3];  
			    str=s.split("<")[0];
			    }
			    } catch (IOException ioe) {
			    response.append(ioe.getMessage());
			    } finally {
			    if (is != null) {
			    try {
			    is.close();
			    } catch (Exception e) {
			    }
			    }
			    if (rd != null) {
			    try {
			    rd.close();
			    } catch (Exception e) {
			    }
			    }
			    }
			    
				return str;
			}else{
				return "没有进行调用。。。。。。。。。。";
				
			}
			
			
		 }
		public static String getData(String url,List list) throws IOException {
			String param="";
			StringBuffer sbf=new StringBuffer();
			
			for(int i=0;i<list.size();i++){
				sbf.append("<hrs:string>");
				String id=(String) list.get(i);	
				sbf.append(id);
			sbf.append("</hrs:string>");		
			}
			param=sbf.toString();
			//System.out.println(param);
			//使用加密
			
			  URL wsUrl = new URL(url);    
		        HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();   		
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json;");
		    String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hrs=\"http://www.hjsj.com/HrService\">"
	        		+" <soapenv:Header/>"             
	               
	+"<soapenv:Body>"
	  +"<hrs:getData>"
	     +"<hrs:systemid>OA</hrs:systemid>"
	     +"<hrs:type>person</hrs:type>"
	     +"<hrs:ids>" +param 
	     +"</hrs:ids>"
	         +"<hrs:etoken>ORs1P</hrs:etoken>"
	      +"</hrs:getData>"
	                      + " </soapenv:Body> "
	                      + "</soapenv:Envelope>";
		    conn.setRequestProperty("SOAPAction", "getData");
		    conn.setRequestProperty("Content-Language", "en-US");
		    conn.setUseCaches(false);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    // Send request
		    OutputStreamWriter wr = new
		    OutputStreamWriter(conn.getOutputStream());
		    //System.out.println(soap+"---");
		    wr.write(soap.toCharArray());
		    wr.flush();
		    wr.close();
		    conn.connect(); 
		    // Get Response
		    String str="";
		    InputStream is = null;
		    BufferedReader rd = null;
		    StringBuffer response = new StringBuffer();
		    try {
		    if (conn.getResponseCode() >= 400) {
		    is = conn.getErrorStream();
		    } else {
		    is = conn.getInputStream();
		    }
		    rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	 
		    response.append(line);
		   
		    response.append('\n');
		    String s=response.toString();  
		   
		    	str= s.toString().split(">")[4];
			
		   
		    }
		    } catch (IOException ioe) {
		    response.append(ioe.getMessage());
		    } finally {
		    if (is != null) {
		    try {
		    is.close();
		    } catch (Exception e) {
		    }
		    }
		    if (rd != null) {
		    try {
		    rd.close();
		    } catch (Exception e) {
		    }
		    }
		    }
			return str;
		 }
	

	
	/**
	 * 鑾峰彇utf-8缂栫爜鏍煎紡鐨勫瓧绗︿覆
	 *
	 * @param str
	 * @return
	 */
	public static String chatSet(String str){
		try {
			
		//str= URLDecoder.decode(str,"GBK");
		str=new String(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 
	 * 
	 * 鑾峰彇鎺ュ彛鐨勬湇鍔°�鏂规硶銆佺敤鎴峰悕銆佸瘑鐮�
	 * @param 浼犲叆娴佺▼鍥哄畾瀛楁
	 * @return
	 */
	public static Map getParam(String str){
		AppBo appBo=new AppBo();
		List lists =new ArrayList();
		String sqls="select * from ehl_services_info where xmname='"+str+"'";
		try {
			lists=appBo.query(sqls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map map=(Map)lists.get(0);
		return map;
	}
	/**
	 * 
	 * 
	 * 鑾峰彇鏂扮殑linkedmap
	 * @param map1鍥哄畾map
	 * @param map 淇敼鍐呭
	 * @return 杩斿洖鏂扮殑linkmap
	 */

	public static LinkedHashMap copyMap(LinkedHashMap map1,HashMap map){
		LinkedHashMap map2=new LinkedHashMap();
		Iterator iter = map.keySet().iterator();
		StringBuffer sb=new StringBuffer();
		String str="";
		while(iter.hasNext()){
			Object key=iter.next();
			Object value=map.get(key);
			sb.append((String)key+",");
		}
		str=sb.toString();
			Iterator iter1 = map1.keySet().iterator();
			while (iter1.hasNext()) {
				Object key1 = iter1.next();
				int i=str.indexOf((String) key1);		
				int j=key1.toString().length();
				if(i>0){
					if((char)str.charAt(i+j)==','&&(char)str.charAt(i-1)==','){
						map2.put(key1, map.get(key1));
					}else{
						map2.put(key1, map1.get(key1));
					}
					
				}else{
					if(i>-1&&(char)str.charAt(i+j)==','){
						map2.put(key1, map.get(key1));
					}else{	
						map2.put(key1, map1.get(key1));
					}		
				}
				
			}
		return map2;
	}
	public static Map getDeptAdd(String str,String strname){
		String dept1="";
		String deptname1="";
		String dept2="";
		String deptname2="";
		List lists =new ArrayList();
		AppBo appBo=new AppBo();
		if(str.length()==9){
			dept1=str;
			deptname1=strname;
			String sqls="select * from ou_department where parentcode='"+str+"'";
			try {
				lists=appBo.query(sqls);		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lists.size()>0){
				Map map_dept=(Map)lists.get(0);
				dept2=map_dept.get("CODE").toString();
				deptname2=map_dept.get("DEPARTNAME").toString();
			}else{
				dept2=str.substring(0,9)+"999";
				deptname2=strname;
			}	
		}else if(str.length()==12){	
			dept1=str.substring(0,9);
			String sqls="select * from ou_department where code='"+dept1+"'";
			try {
				lists=appBo.query(sqls);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lists.size()>0){
				Map map_dept=(Map)lists.get(0);
				deptname1=map_dept.get("DEPARTNAME").toString();	
			}
			dept2=str.substring(0,12);
			deptname2=strname;
		}else if(str.length()>12){
			dept1=str.substring(0,9);
			String sqls="select * from ou_department where code='"+dept1+"'";
			try {
				lists=appBo.query(sqls);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lists.size()>0){
				Map map_dept=(Map)lists.get(0);
				deptname1=map_dept.get("DEPARTNAME").toString();	
			}
			dept2=str.substring(0,12);
			String sqls2="select * from ou_department where code='"+dept2+"'";
			try {
				lists=appBo.query(sqls2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lists.size()>0){
				Map map_dept=(Map)lists.get(0);
				deptname2=map_dept.get("DEPARTNAME").toString();	
			}
		}
	Map mapd=new HashMap();
	mapd.put("LEV1_DEPARTMENT_CODE",dept1);
	mapd.put("LEV1_DEPARTMENT_NAME",deptname1);
	mapd.put("LEV2_DEPARTMENT_CODE",dept2);
	mapd.put("LEV2_DEPARTMENT_NAME",deptname2);
		
		return mapd;
	}
	public static String getDeptDel(String str){
		AppBo appBo=new AppBo();
		List lists =new ArrayList();
		String sqls="select * from ou_department where code='"+str+"'";
		try {
			lists=appBo.query(sqls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(lists.size()>0){
			
		}else{
			str=str.substring(0,9);
		}
		
		return str;
	}

	public static LinkedHashMap getBase64Map(LinkedHashMap map){
		LinkedHashMap map2=new LinkedHashMap();	
			Iterator iter1 = map.keySet().iterator();
			while (iter1.hasNext()) {
				String key1 = iter1.next().toString();
				String value=map.get(key1).toString();
						//map2.put(key1,new checkData().getBase64(value));		
			}
		return map2;
	}
	public static boolean getStatus(String json) throws FileNotFoundException{
		JSONObject wlspJSON = JSONObject.fromObject(JSONObject.fromObject(json).getString("OutputParameters"));
		String status="";
		boolean param=false;
		int i=0;
		try{
			status=wlspJSON.getString("X_RETURN_STATUS");
			
		}catch(Exception e){
			i++;
			//PrintWriter pw = new PrintWriter(new File("D:/excep.txt"));
			//PrintStream s=new PrintStream("D:/excep1.txt");
			//pw.write("2017-09-08");
			           	//e.printStackTrace(pw);  
			           //	e.printStackTrace(s);
			           	System.out.println(e.toString());
			           // pw.flush();  
			           // pw.close();  
		}
		if(i>0){
			try{
				status=wlspJSON.getString("RETURN_STATUS");	
			}catch(Exception e){
				i++;
			}
		}
		if(i>0){
			param=false;
		}else{
			if(status=="S"||status.equals("S")){
				param=true;
			}
		}
		
		return param;
	}
	public static String getException(String json) throws FileNotFoundException{
		JSONObject wlspJSON = JSONObject.fromObject(JSONObject.fromObject(json).getString("OutputParameters"));
		String js=wlspJSON.getString("X_MSG_LINE_TB");
		JSONObject jss=JSONObject.fromObject(JSONObject.fromObject(js).getString("X_MSG_LINE_TB_ITEM"));
		String status="";
		String param="";
		int i=0;
		try{
			status=wlspJSON.getString("X_RETURN_STATUS");
			
		}catch(Exception e){
			i++;
			//PrintWriter pw = new PrintWriter(new File("D:/excep.txt"));
			//PrintStream s=new PrintStream("D:/excep1.txt");
			//pw.write("2017-09-08");
			           	//e.printStackTrace(pw);  
			           //	e.printStackTrace(s);
			           	System.out.println(e.toString());
			           // pw.flush();  
			           // pw.close();  
		}
		if(i>0){
			try{
				status=wlspJSON.getString("RETURN_STATUS");	
			}catch(Exception e){
				i++;
			}
		}
		if(status=="S"||status.equals("S")){
			param="";
		}else{
			param=jss.getString("PROCESS_MESSAGE");;
		}
		return param;
	}
		
}

