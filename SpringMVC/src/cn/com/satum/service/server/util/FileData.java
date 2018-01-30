package cn.com.satum.service.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Encoder;

public class FileData {
	 /**
     * @param filepath
     * E:\Staums\immage\2017-01-01
     */
    public static String getdir(String filetype)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String path = "/usr/local/Staums/{0}/";
        switch (filetype)
        {
            case "image":
                path = path.replace("{0}", "image")+ getDataString()+"/";
                break;
            case "backImage":
                path = path.replace("{0}", "backImage")+sdf.format(date)+"/";
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
     * �ļ����ط���
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

            // ��������in��,�� bytes.length ���ֽڵ����ݶ����ֽ�����bytes��
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
     * ��ȡ��ǰʱ��
     */
    public static String getDataTimeString(Boolean isfilename)
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
            Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
            return formatter.format(curDate); 
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return "";
        }
    }

    /*
     * ��ȡ��ǰ����
     */
    public static String getDataString()
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");     
            Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
            return formatter.format(curDate); 
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return "";
        }
    }
}
