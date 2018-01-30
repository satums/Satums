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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.service.server.util.FileData;

import com.sun.jmx.snmp.Timestamp;

@Controller

public class FileUpload {
	@RequestMapping("upload")
	     public String upload(HttpServletRequest request,
	            @RequestParam("description") String description,
	            @RequestParam("file") MultipartFile file) throws Exception {

	        System.out.println(description);
	        //如果文件不为空，写入上传路径
	        if(!file.isEmpty()) {
	            //上传文件路径
	           String path =new FileData().getdir("backImage");
	        	//String path="E:/Staums/images/2018/";
	            //上传文件名	        	
	            String filename = file.getOriginalFilename();
	            filename=new FileData().getDataTimeString(true) +filename.substring(filename.indexOf('.'));
	            File filepath = new File(path,filename);
	            //判断路径是否存在，如果不存在就创建一个
	            if (!filepath.getParentFile().exists()) { 
	                filepath.getParentFile().mkdirs();
	            }
	            //将上传文件保存到一个目标文件当中
	            file.transferTo(new File(path + File.separator + filename));
	            String filedir=new BASE64Encoder().encode(path.getBytes("utf-8"));
	          List list=AppBo.query("select * from sh_common_icon where file_type='"+description+"'");
	          if(list.size()>0){
	        	  AppBo.runSQL("update sh_common_icon set is_del='1' where types="+description+"' and user_id='system')");
	        	  AppBo.runSQL("insert into sh_common_icon (id,names,url,name_sys,user_id,types,file_type) values"
		    				+ " ('"+new DataUtil().getUUID()+"','"+filename+"','"+filedir+"','"+file.getOriginalFilename()+"','"+description+"','','backImage')"); 
	          }else{      
	            AppBo.runSQL("insert into sh_common_icon (id,names,url,name_sys,user_id,types,file_type) values"
	    				+ " ('"+new DataUtil().getUUID()+"','"+file.getOriginalFilename()+"','"+filedir+"','"+filename+"','system','"+description+"','backImage')");
	          }
	            return "success";
	        } else {
	            return "error";
	        }

	     }

	}


