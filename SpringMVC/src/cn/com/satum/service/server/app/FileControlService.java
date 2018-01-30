package cn.com.satum.service.server.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.service.server.util.FileData;

import com.alibaba.fastjson.JSON;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileControlService implements AppService{
private final static String json="{"
		+ "\"flag\":\"add\","
		+ "\"type\":\"1\","//ͷ��1��ͼ��Ϊ2
		+ "\"userCode\":\"15738928228\","
		+ "\"fileName\":\"ͷ��.jpg\","
		+ "\"file\":\"byte\","
		+ "\"fileType\":\"image\""
		+ "}";
@Override
public String getInfo(String jsondata) {
	// TODO Auto-generated method stub
	String rtg="";
	Map rmap=new HashMap();	
	Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String mark=(String) reqMap.get("flag");
	switch(mark){
	case "add":
		rtg=addFile(jsondata);
		break;
	case "update":
		rtg=updateFile(jsondata);
		break;
	case "delete":
		rtg=deleteFile(jsondata);
		break;
	}
	return rtg;
	}
	public String addFile(String jsondata){
		Map rmap=new HashMap();
	
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String reqType = (String) reqMap.get("type");
	String user_id=(String) reqMap.get("userCode");
	String fileName= (String) reqMap.get("fileName");
	String fileType= (String) reqMap.get("fileType");
	String file= (String) reqMap.get("file");
	String filedir="";
	String sys_fileName="";
	Map maps=new HashMap();
	String rtg="";
	if(reqType.equals("1")){		
		maps=uploadFile(fileName,fileType,file);
		if(maps!=null){
			filedir=maps.get("filedir").toString();
			try {
				filedir=new BASE64Encoder().encode(filedir.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sys_fileName=maps.get("fileName").toString();
		}
		AppBo.runSQL("insert into sh_common_icon (id,names,url,name_sys,user_id,types,file_type) values"
				+ " ('"+new DataUtil().getUUID()+"','"+fileName+"','"+filedir+"','"+sys_fileName+"','"+user_id+"','"+reqType+"','"+fileType+"')");
		
		rmap.put("result", "S");
	rmap.put("msg", "�ϴ��ɹ�");
	}
	try{
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "���ϴ����ļ���������ԭ��ʧ�ܣ��������ϴ���");
	}
	return JSONObject.fromObject(rmap).toString();
}  
	public String updateFile(String jsondata){
		Map rmap=new HashMap();
		try{
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String reqType = (String) reqMap.get("type");
	String user_id=(String) reqMap.get("userCode");
	String fileName= (String) reqMap.get("fileName");
	String fileType= (String) reqMap.get("fileType");
	String file= (String) reqMap.get("file");
	String filedir="";
	String sys_fileName="";
	Map maps=new HashMap();
	String rtg="";
	if(reqType.equals("1")){		
		maps=uploadFile(fileName,fileType,file);
		if(maps!=null){
			filedir=maps.get("filedir").toString();
			try {
				filedir=new BASE64Encoder().encode(filedir.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sys_fileName=maps.get("fileName").toString();
		}
		AppBo.runSQL("update sh_common_icon set is_del='1' where user_id='"+user_id+"'");
		AppBo.runSQL("insert into sh_common_icon (id,names,url,name_sys,user_id,types,file_type) values"
				+ " ('"+new DataUtil().getUUID()+"','"+fileName+"','"+filedir+"','"+sys_fileName+"','"+user_id+"','"+reqType+"','"+fileType+"')");
		
	rmap.put("result", "S");
	rmap.put("msg", "����ͷ��ɹ�");
	
	}
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "���ϴ����ļ���������ԭ��ʧ�ܣ��������ϴ���");
	}
	return JSONObject.fromObject(rmap).toString();
} 
	public String deleteFile(String jsondata){
		Map rmap=new HashMap();
		try{
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
	String user_id=(String) reqMap.get("userCode");
	AppBo.runSQL("update sh_icon set is_del='1' where user_id='"+user_id+"'");
	rmap.put("result", "S");
	rmap.put("msg", "ɾ��ͷ��ɹ�");	
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "ɾ��ʧ�ܡ�");
	}
	return JSONObject.fromObject(rmap).toString();
}  
    /*
     * �ļ��ϴ�����
     */
    public static Map uploadFile(String fileName,String filetype, String file)//byte[] bytes)
    {
    	Map map=new HashMap();
        FileOutputStream fos = null;
        try
        {
            String filedir = new FileData().getdir(filetype);
            BASE64Decoder decoder= new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(file);

            if(filedir=="")
            {
                return map;
            }
            Integer rdm = new Random().nextInt(10000);
            //
            String savename = new FileData().getDataTimeString(true) +fileName.substring(fileName.indexOf('.'));
            fos = new FileOutputStream(filedir+savename);
            // ���ֽ�����bytes�е����ݣ�д���ļ������fos��
            fos.write(bytes);
            fos.flush();
            map.put("filedir", filedir);
            map.put("fileName", savename);
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return map;
        }
      
    }

   

	  public static void main(String[] args) throws IOException {
		 /**
		  FileInputStream is;  
		
              is = new FileInputStream("D:\\ͷ��.jpg");  
              int i = is.available(); // �õ��ļ���С  
              byte data[] = new byte[i]; 
              is.read(data);
              BASE64Encoder encoder = new BASE64Encoder();
              String file = encoder.encode(data); 
              System.out.println(file);
              Map p=uploadFile("ͷ��.jpg","image",file);
              System.out.println(p+"1111111");
              is.read(data); // ������  
              is.close();  
**/
		 // downloadFile("E:\\youme\\image\\2018-01-12\\201801121451287297.png");
}
}
