package cn.com.satum.service.server.app;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;

import com.alibaba.fastjson.JSON;

public class DeviceTypeControlService implements AppService {
	@Override
	public String getInfo(String jsondata) {
		Map<String, Object> reqMap = JSON.parseObject(jsondata);
		String reqType = (String) reqMap.get("type");
		String flag=(String) reqMap.get("flag");
		String resStr = "";
		switch(reqType){
		case "0":
			resStr=queryType(flag);
			break;
		}
		return resStr;
	}
	public static String queryType(String flag){
		String resStr="";
		Map mp=new HashMap();
		String sqls="select flag from sh_common_device_type where id<10";
		List lis=AppBo.query(sqls);
		if(lis.size()>0){
		String flags=((Map)lis.get(0)).get("flag").toString();
		if(flags.equals(flag)){
			mp.put("result", "E");
			mp.put("msg", "暂无更新");
		}else{
			String sql="select id,parent_id,level,name,flag from sh_common_device_type where is_del='2' order by id desc";
			Map map=new HashMap();
			List list=AppBo.query(sql);	
			mp.put("result", "S");
			mp.put("data", list);
			mp.put("msg","获取列表成功" );
		}
		}else{
			String sql="select id,parent_id,level,name,flag from sh_common_device_type where is_del='2' order by id desc";
			Map map=new HashMap();
			List list=AppBo.query(sql);	
			mp.put("result", "S");
			mp.put("data", list);
			mp.put("msg","获取列表成功" );
		}
		
		return JSONObject.fromObject(mp).toString();
	}
}
