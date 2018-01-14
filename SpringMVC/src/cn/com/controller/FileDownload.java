package cn.com.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.com.Data.Bo.AppBo;

import com.sun.jmx.snmp.Timestamp;

@Controller
@RequestMapping("download")
public class FileDownload {
	
	@RequestMapping("picture")
		public String downloadFile(@RequestParam("flag") String flag,@RequestParam("id")String id,
		             HttpServletRequest request, HttpServletResponse response) {
			String fileName="";
			String path="";
				List list=new AppBo().query("select * from sh_common_icon where file_type='"+flag+"' and id='"+id+"'");
		if(list.size()>0){
			Map maps=(Map)list.get(0);
			fileName=(String) maps.get("name_sys");
			path=(String) maps.get("url");			
			try {
				byte[] b=new BASE64Decoder().decodeBuffer(path);
				path=new String(b,"utf-8");
				System.out.println(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		         if (fileName != null) {
		             String realPath = path+"\\"+fileName;
		             File file = new File(realPath);		      		            
		                 response.setContentType("application/force-download");// 设置强制下载不打开
		                 response.addHeader("Content-Disposition","attachment;fileName=" + fileName);// 设置文件名		               
		                 byte[] buffer = new byte[1024];
		                 FileInputStream fis = null;
		                 BufferedInputStream bis = null;
		                 try {
		                     fis = new FileInputStream(file);
		                     bis = new BufferedInputStream(fis);
		                     OutputStream os = response.getOutputStream();
		                     int i = bis.read(buffer);
		                     while (i != -1) {
		                         os.write(buffer, 0, i);
		                         i = bis.read(buffer);
		                     }
		                 } catch (Exception e) {
		                     // TODO: handle exception
		                     e.printStackTrace();
		                 } finally {
		                     if (bis != null) {
		                         try {
		                             bis.close();
		                         } catch (IOException e) {
		                             // TODO Auto-generated catch block
		                             e.printStackTrace();
		                         }
		                     }
		                     if (fis != null) {
		                         try {
		                             fis.close();
		                         } catch (IOException e) {
		                             // TODO Auto-generated catch block
		                             e.printStackTrace();
		                         }
		                     }
		                 }
		            
		         }
		         return null;
		     }

	}


