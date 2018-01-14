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

import com.alibaba.fastjson.JSON;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileControlService implements AppService{
private final static String json="{"
		+ "\"flag\":\"add\","//头像1，图标为2
		+ "\"type\":\"1\","//头像1，图标为2
		+ "\"userCode\":\"15738928228\","
		+ "\"fileName\":\"头像.jpg\","
		+ "\"file\":\"byte\","
		+ "\"fileType\":\"image\""//图像后缀	
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
	rmap.put("msg", "上传成功");
	}
	try{
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "您上传的文件由于网络原因失败，请重新上传。");
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
	rmap.put("msg", "更改头像成功");
	
	}
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "您上传的文件由于网络原因失败，请重新上传。");
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
	rmap.put("msg", "删除头像成功");	
	}catch(Exception e){
		rmap.put("result", "E");
		rmap.put("msg", "删除失败。");
	}
	return JSONObject.fromObject(rmap).toString();
}  
    /*
     * 文件上传服务
     */
    public static Map uploadFile(String fileName,String filetype, String file)//byte[] bytes)
    {
    	Map map=new HashMap();
        FileOutputStream fos = null;
        try
        {
            String filedir = getdir(filetype);
            BASE64Decoder decoder= new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(file);

            if(filedir=="")
            {
                return map;
            }
            Integer rdm = new Random().nextInt(10000);
            //
            String savename = getDataTimeString(true) +fileName.substring(fileName.indexOf('.'));
            fos = new FileOutputStream(filedir+savename);
            // 将字节数组bytes中的数据，写入文件输出流fos中
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

    /**
     * @param filepath
     * E:\Staums\immage\2017-01-01
     */
    private static String getdir(String filetype)
    {
        String path = "\\usr\\local\\Staums\\{0}\\" + getDataString()
                + "\\";
        switch (filetype)
        {
            case "image":
                path = path.replace("{0}", "image");
                break;
            case "vedio":
                path = path.replace("{0}", "vedio");
                break;                    
            default:
                return "";
        }
        try
        {
            java.io .File file = new java.io .File(path);
            if(!file.exists())
            {
                if(!file.mkdirs())
                {
                    return "";
                }
            }
            return path;
        }
        catch(Exception ex)
        {
            return "";
        }
        finally
        {
            
        }
    }

    /*
     * 文件下载服务
     */
    public static String downloadFile(String filepath)
    {
//        filepath = "F:\\youme\\vedio\\2013-09-03\\201309031700143294.amr";
        FileInputStream in = null;
        byte bytes[] = null;
        String file = null;
        try
        {
            in = new FileInputStream(filepath);
            bytes = new byte[in.available()];

            // 从输入流in中,将 bytes.length 个字节的数据读入字节数组bytes中
            in.read(bytes);
            BASE64Encoder encoder = new BASE64Encoder();
            file = encoder.encode(bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println(file);
//        return bytes;
        return file;
    }
    /*
     * 获取当前时间
     */
    private static String getDataTimeString(Boolean isfilename)
    {
        try
        {
            SimpleDateFormat formatter = null;
            if(!isfilename)
            {
                formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
            }
            else
            {
                formatter= new SimpleDateFormat("yyyyMMddHHmmss"); 
            }
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
            return formatter.format(curDate); 
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return "";
        }
    }

    /*
     * 获取当前日期
     */
    private static String getDataString()
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");     
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
            return formatter.format(curDate); 
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return "";
        }
    }

	  public static void main(String[] args) throws IOException {
		 /**
		  FileInputStream is;  
		
              is = new FileInputStream("D:\\头像.jpg");  
              int i = is.available(); // 得到文件大小  
              byte data[] = new byte[i]; 
              is.read(data);
              BASE64Encoder encoder = new BASE64Encoder();
              String file = encoder.encode(data); 
              System.out.println(file);
              Map p=uploadFile("头像.jpg","image",file);
              System.out.println(p+"1111111");
              is.read(data); // 读数据  
              is.close();  
**/
		 // downloadFile("E:\\youme\\image\\2018-01-12\\201801121451287297.png");
}
}
