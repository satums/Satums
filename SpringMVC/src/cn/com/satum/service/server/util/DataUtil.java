package cn.com.satum.service.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.com.Data.Bo.AppBo;

public class DataUtil {
	private AppBo appBo;

	// 解析二进制访问服务端时发送的数据
	public String dataParse(String data, String IPs, int ports) {
		/**
		 * 对Data进行解析，获取主机编号 String zjbhs=""; this.zjbh=zjbhs
		 * 
		 */
		System.out.println(IPs);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(date);
		String zjbh = "hhhhhhhhhhhsss";
		return new DataUtil().dataUpdate(zjbh, IPs, ports, dates);
	}

	// 对主机数据进行更新
	public String dataUpdate(String zjbhs, String iPs, int ports, String dates) {
		List list = appBo.query("select id from sh_onlinedata where zjbh='" + zjbhs + "'");
		String message = "S";
		if (list.size() > 0) {
			// System.out.println("-------------===================update"+"update
			// sh_onlinedata set
			// updated_date='"+dates+"',IP='"+iPs+"',port='"+ports+"' where
			// zjbh='"+zjbhs+"'");
			appBo.runSQL("update sh_onlinedata set updated_date='" + dates + "',IP='" + iPs + "',port='" + ports
					+ "' where zjbh='" + zjbhs + "'");
		} else {

			List list1 = appBo.query("select max(id) id from sh_onlinedata");
			Map map = (Map) list1.get(0);
			int id = 0;
			if (map.get("id") != null) {
				id = Integer.valueOf(map.get("id").toString());
			} else {
				id = 0;
			}

			try {
				String sql = "INSERT INTO sh_onlinedata VALUES ('" + (id + 1) + "','" + zjbhs + "','" + iPs + "','"
						+ ports + "','" + dates + "','" + dates + "','0','','')";
				System.out.println(sql);
				appBo.runSQL("INSERT INTO sh_onlinedata VALUES ('" + (id + 1) + "','" + zjbhs + "','" + iPs + "','"
						+ ports + "','" + dates + "','" + dates + "','0','','')");
			} catch (Exception e) {
				message = e.getMessage();
			}
		}
		return message;
	}

	// 通过主机编号查询获取主机的详细信息,并把用户关联主机信息插入表中
	public Map dataQuery(String zjbhs, String jsonData) throws ParseException {
		List listu = appBo.query("select id from sh_masterdata where zjbh='" + zjbhs + "'");
		if (listu.size() > 0) {
			appBo.runSQL("update sh_masterdata set content='" + jsonData + "' where zjbh='" + zjbhs + "'");
		} else {
			/**
			 * 解析jsondata获取用户ID
			 * 
			 */
			String userid = "15811137696";
			List list1 = appBo.query("select max(id) id from sh_onlinedata");
			Map map = (Map) list1.get(0);
			int id = 0;
			if (map.get("id") != null) {
				id = Integer.valueOf(map.get("id").toString());
			} else {
				id = 0;
			}
			appBo.runSQL("INSERT INTO sh_masterdata VALUES ('" + (id + 1) + "','" + userid + "','" + zjbhs + "','"
					+ jsonData + "','0','','')");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map map = null;
		List list1 = appBo.query("select * from sh_onlinedata where zjbh='" + zjbhs + "'");
		// 这里的mm是指主机心跳检测的时间，如果时间大于心跳检测的时间则说明该主机断开连接，云端将状态返回给APP进行状态改变
		int mm = 60;
		if (list1.size() > 0) {
			map = (Map) list1.get(0);
			String rq = map.get("updated_date").toString();
			Date date = sdf.parse(rq);
			long a = new Date().getTime();
			long b = date.getTime();
			mm = (int) ((a - b) / 1000);
		} else {
			mm = 9999;
		}
		map.put("second", mm);
		System.out.println(mm + "--------------------mm");
		return map;
	}

	public static String getUUID() {

		return UUID.randomUUID().toString();
	}

}
