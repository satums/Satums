package cn.com.satum.service.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.HEX2And16;

public class DataUtil {
	private static AppBo appBo;
	//多组
	private final static String jsondata="{"
			+ "\"zjbh\":\"hhhhhhhhhhhsss\""		
			+ "data:["
			+ "{\"num\":\"1\","
			+ "\"name\":\"A电源一\","
			+ "\"status\":\"1\"},"
			+ "{\"num\":\"2\","
			+ "\"name\":\"A电源二\","
			+ "\"status\":\"2\"}"		
			+ "]}";
	// 解析二进制访问服务端时发送的数据
	public String dataParse(String data, String IPs, int ports) {
		String json=new HEX2And16().hex2To16(data);
		json=json.substring(0,(json.length()-1)).toString();		
		Map map=JSONObject.fromObject(json);		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(date);
		String zjbh=(String)map.get("zjbh");
		//zjbh = "hhhhhhhhhhhsss";
		System.out.println(zjbh);
		return new DataUtil().dataUpdate(zjbh, IPs, ports, dates);
	}
public static boolean checkMaster(String zjbh){
	boolean flag=true;
	List list=appBo.query("select * from sh_masterdata where zjbh='"+zjbh+"'");
	if(list.size()>0){
	}else{
		flag=false;
	}	
	return flag;
}

	// 对主机数据进行更新
	public String dataUpdate(String zjbhs, String iPs, int ports, String dates) {
		String message="S";
		if(checkMaster(zjbhs)){
			List list = appBo.query("select id from sh_onlinedata where zjbh='" + zjbhs + "'");			
			if (list.size() > 0) {
				String sql="update sh_onlinedata set updated_date='" + dates + "',IP='"+iPs+"',port='"+ports+ "' where zjbh='"+zjbhs+"'";
				System.out.println(sql);
				appBo.runSQL(sql);
			} else {
				
				appBo.runSQL("insert into sh_onlinedata (id,zjbh,ip,port,created_at,updated_date,b1) "
						+ "values ('"+getUUID()+"','"+zjbhs+"','"+iPs+"','"+ports+"','"+dates+"','"+dates+"','0')");			
			}
		}else{
			message="主机不是本厂家生产，没有相关主机记录。";
		}	
		return message;
	}

	// 通过主机编号查询获取主机的详细信息,并把用户关联主机信息插入表中
	public Map dataQuery(String zjbhs, String userid) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		String message="成功";
		String result="S";
		String ip="0.0.0.0";
		String port="8080";
		int mm = 60;
		Map map = new HashMap();
		if(checkMaster(zjbhs)){
			List listu1 = appBo.query("select * from sh_masterdata where user_code='" + userid + "' and zjbh='"+zjbhs+"'");
			if(listu1.size()>0){												
			}else{
			appBo.runSQL("update sh_masterdata set user_code='" + userid + "' where zjbh='" + zjbhs + "'");								
			}	
			List list1 = appBo.query("select * from sh_onlinedata where zjbh='" + zjbhs + "'");
			// 这里的mm是指主机心跳检测的时间，如果时间大于心跳检测的时间则说明该主机断开连接，云端将状态返回给APP进行状态改变
			if(list1.size()>0){
				Map maps = (Map) list1.get(0);
				String rq = maps.get("updated_date").toString();
				ip=maps.get("IP").toString();
				port=maps.get("port").toString();
				Date date = sdf.parse(rq);
				long a = new Date().getTime();
				long b = date.getTime();
				mm = (int) ((a - b) / 1000);
			}else{
				message="主机不是本厂家生产，没有相关主机记录。";
				result="E";
				mm=999;
			}
		}else{			
			message="主机不是本厂家生产，没有相关主机记录。";
			result="E";
			mm=999;			
		}
		map.put("second", mm+"");
		map.put("result", result);	
		map.put("message", message);
		map.put("ip", ip);	
		map.put("port", port);
		return map;
	}
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
