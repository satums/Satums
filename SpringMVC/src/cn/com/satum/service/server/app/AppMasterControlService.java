package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.Data.Bo.AppBo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AppMasterControlService implements AppService{
private final String json="{"
		+ "\"flag\":\"query\","
		+ "\"userCode\":\"15738928228\""
		+ "}";
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		Map map=JSONObject.fromObject(jsondata);
		Map retMap=new HashMap();
		if(map.get("flag")==null){
			retMap.put("result", "E");
			retMap.put("msg", "操作类型不能为空");
			String str=JSONObject.fromObject(retMap).toString();
			return str;
		}else if(map.get("userCode")==null){
			retMap.put("result", "E");
			retMap.put("msg", "用户号不能为空");
			String str=JSONObject.fromObject(retMap).toString();
			return str;
		}
		String flag=map.get("flag").toString();
		String userCode=map.get("userCode").toString();
		String ret="";
		if(flag.equals("query")){
			ret=query(userCode);
		}else if(flag.equals("update")){
			ret=update(jsondata,userCode);
		}else if(flag.equals("delete")){
			ret=delete(jsondata,userCode);
		}else if(flag.equals("add")){
			ret=add(jsondata,userCode);
		}			
		return ret;
	}
public String query(String userCode){
	String sql="select user_code,zjbh,content,b1 as status from sh_masterdata where user_code='"+userCode+"' and b2='2'";
	Map map=new HashMap();
	try {
		List list=AppBo.query(sql);
		if(list.size()>0){
			map.put("result", "S");
			map.put("data", list);
			map.put("msg","获取主机列表成功");
		}else{
			map.put("result", "S");
			map.put("data", "");
			map.put("msg","您还没有添加主机，请先添加。");
		}
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		map.put("result", "E");
		map.put("msg",e.getMessage());
		e.printStackTrace();
	}
	String ret=JSONObject.fromObject(map).toString();
	return ret;
}
public String update(String jsondata,String userCode){
	Map map=new HashMap();
	String ret="";
		Map maps=JSONObject.fromObject(jsondata);
		if(maps.get("content")==null){
			map.put("result", "E");
			map.put("msg", "主机描述不能为空");
			String str=JSONObject.fromObject(map).toString();
			return str;
		}else{
			String content=maps.get("content").toString();
			String zjbh=maps.get("zjbh").toString();
			String status=maps.get("status").toString();
		String sql="select user_code,zjbh,content from sh_masterdata where user_code='"+userCode+"'and zjbh='"+zjbh+"' and b2='2'";
		
		try {
			List list=AppBo.query(sql);
			if(list.size()>0){
				AppBo.runSQL("update sh_masterdata set content='"+content+"',b1='"+status+"' where zjbh='"+zjbh+"'");
				
			}else{
				map.put("result", "E");
				map.put("msg","主机不存在，请先进行添加");
				ret=JSONObject.fromObject(map).toString();
				return ret;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put("result", "E");
			map.put("msg",e.getMessage());
			e.printStackTrace();
			return ret;
		}
	}
	
	map.put("result", "S");
	map.put("msg","主机修改成功");
	ret=JSONObject.fromObject(map).toString();
	return ret;
}
public String delete(String jsondata,String userCode){
	Map map=new HashMap();
	String ret="";
		Map maps=JSONObject.fromObject(jsondata);		
		String zjbh=maps.get("zjbh").toString();
		try {
			String sql="select * from sh_masterdata where zjbh='"+zjbh+"'";
			List list=AppBo.query(sql);
			if(list.size()>0){
				AppBo.runSQL("update sh_masterdata set user_code='',content='' where zjbh='"+zjbh+"'");
				
			}else{
				map.put("result", "E");
				map.put("msg","该用户下没有此主机");
				ret=JSONObject.fromObject(map).toString();
				return ret;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put("result", "E");
			map.put("msg",e.getMessage());
			e.printStackTrace();
			ret=JSONObject.fromObject(map).toString();
			return ret;
		}
	
	
	map.put("result", "S");
	map.put("msg","主机解绑成功");
	ret=JSONObject.fromObject(map).toString();
	return ret;
}
public String add(String jsondata,String userCode){
	Map map=new HashMap();
	String ret="";
		Map maps=JSONObject.fromObject(jsondata);		
		String zjbh=maps.get("zjbh").toString();
		String content=maps.get("content").toString();
		String sqls="select max(id+1) id from sh_masterdata";
		List li=AppBo.query(sqls);
		String id=((Map)li.get(0)).get("id").toString();
		try {
			String sql="select * from sh_masterdata where zjbh='"+zjbh+"'";
			List list=AppBo.query(sql);
			if(list.size()>0){
				List lis=AppBo.query("select * from sh_masterdata where zjbh='"+zjbh+"' and user_code<>''");
				if(lis.size()>0){
					map.put("result", "E");
					map.put("msg","该主机已经被添加，请联系厂家进行配置。");
					ret=JSONObject.fromObject(map).toString();
					return ret;		
					
				}else{
					List lis1=AppBo.query("select * from sh_masterdata where zjbh='"+zjbh+"' and user_code=''");	
					String ids=((Map)lis1.get(0)).get("id").toString();					
					AppBo.runSQL("update sh_masterdata set user_code='"+userCode+"',content='"+content+"',b2='2' where id ="+ids);
					AppBo.runSQL("update sh_masterdata set b2='1' where user_code='"+userCode+"' and id <>"+ids);	
				}
						
			}else{	
				AppBo.runSQL("insert into sh_masterdata (id,user_code,zjbh,content,b1,b2,b3)values"
						+ "("+id+",'"+userCode+"','"+zjbh+"','"+content+"','1','2','1')");
				AppBo.runSQL("update sh_masterdata set b2='1' where user_code='"+userCode+"' and id <"+id);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put("result", "E");
			map.put("msg",e.getMessage());
			e.printStackTrace();
			ret=JSONObject.fromObject(map).toString();
			return ret;
		}
	
	map.put("result", "S");
	map.put("msg","主机绑定成功");
	ret=JSONObject.fromObject(map).toString();
	return ret;
}
}
