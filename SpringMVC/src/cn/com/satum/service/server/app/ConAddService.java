package cn.com.satum.service.server.app;

import java.net.SocketException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;
import cn.com.satum.util.PostStyle;
import cn.com.satum.util.Sender;
public class ConAddService implements AppService {

		public String getInfo(String jsonData) {
			AppBo appBo=new AppBo();
			String userid="";
			String conID="";
			String zjbh="hhhhhhhhhhhsss";
			/**
			 * table相当于存储情景、联动等配置的表
			 * 查询表中是否存在数据，存在即更新不存在就新增
			 * String sql="select * from table where userId=userid";
			 * List list=appBo.query(sql);
			 * if(list.size()>0){
			 * appBo.runSQL("update table set message='"+jsondata+"'");
			 * 调用向主机发送配置信心的接口
			 * }else{
			 * List lis=appBo.query("select max(id) id from table");
			 * Map maps=(Map)lis.get(0);
			 * int id=Integer.valueOf(maps.get("id").toString());
			 * appBo.runSQL("insert into table values('"+(id+1)+"','"+userid+"','"+jsondata+"')");
			 * 调用向主机发送配置信心的接口
			 * }
			 * 
			 * 接收传递过来的数据包关联用户ID放入表中存储，并传递
			 * 
			 */
			String flag="S";
			Map map=new HashMap();
			//通过主机序列号查询获取IP和端口
			try {
				map=new DataUtil().dataQuery(zjbh,jsonData);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String IP=map.get("ip").toString();
			int second=Integer.valueOf(map.get("second").toString());
			int port=Integer.valueOf(map.get("port").toString());
			System.out.println(IP+"=================");
	try {
		flag=new Sender().send(jsonData,IP,port);
		System.out.println(flag+"=================");
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//second是检测主机心跳检测的时间可以大于心跳检测时间1-5秒，假设心跳检测时间为60秒
	if(!flag.equals("S")||second>65){
		flag="E";
	}
			return flag;
		}

}
