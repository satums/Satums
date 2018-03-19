package cn.com.satum.util;

import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;

public class UdpUtil {

	private AppBo appBo=new AppBo();
	public String getNetInfo(String userid,String zjbh,String conID,String status,String IP,int port) {
		Map mapm=new HashMap();
		String msg="";
		String json="";
		String sql="select t.parent_id typeid,t1.num num from sh_common_device_type t,sh_device t1 where "
				+ "t1.user_code='"+userid+"' and t1.device_code='"+conID+"' and t1.zjbh='"+zjbh+"' and t1.device_type_id=t.id";
	 List list=appBo.query(sql);
	 if(list.size()>0){
		 Map map=(Map)list.get(0);
		 String typeid=map.get("typeid").toString();
		 String num=map.get("num").toString();
		 json="{"
	+"\"flag\":\"device\","
	+"\"data\":[{"
	+"\"device_type\":\""+typeid+"\","
	+"\"device_num\":\""+num+"\",	"
	+"\"status\":\""+status+"\""
	+"}]"
	+"}\n";
		 System.out.println(json);
		 msg="S";
		 try {
			new Sender().send(json,IP,port);
		} catch (SocketException e) {
			msg=e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }else{
		 msg="您没有添加该设备或该设备或已失效，请刷新后添加。";
	 }
	   
		return msg;
	}
	}

