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
			String flag="S";
			Map map=new HashMap();
			//ͨ���������кŲ�ѯ��ȡIP�Ͷ˿�
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
	//second�Ǽ��������������ʱ����Դ����������ʱ��1-5�룬�����������ʱ��Ϊ60��
	if(!flag.equals("S")||second>65){
		flag="E";
	}
			return flag;
		}

}
