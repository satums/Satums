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
			 * table�൱�ڴ洢�龰�����������õı�
			 * ��ѯ�����Ƿ�������ݣ����ڼ����²����ھ�����
			 * String sql="select * from table where userId=userid";
			 * List list=appBo.query(sql);
			 * if(list.size()>0){
			 * appBo.runSQL("update table set message='"+jsondata+"'");
			 * ���������������������ĵĽӿ�
			 * }else{
			 * List lis=appBo.query("select max(id) id from table");
			 * Map maps=(Map)lis.get(0);
			 * int id=Integer.valueOf(maps.get("id").toString());
			 * appBo.runSQL("insert into table values('"+(id+1)+"','"+userid+"','"+jsondata+"')");
			 * ���������������������ĵĽӿ�
			 * }
			 * 
			 * ���մ��ݹ��������ݰ������û�ID������д洢��������
			 * 
			 */
	
			return "���ýӿڳɹ�"+jsondata;
		}

}
