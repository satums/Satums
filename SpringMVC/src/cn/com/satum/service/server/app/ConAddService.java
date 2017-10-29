package cn.com.satum.service.server.app;

import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import cn.com.satum.util.PostStyle;
public class ConAddService implements AppService {

		public String getInfo(String jsondata){
			AppBo appBo=new AppBo();
			String userid="";
			String conID="";
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
	
			return "调用接口成功"+jsondata;
		}

}
