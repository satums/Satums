package cn.com.satum.service.server.app;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.com.Data.Bo.AppBo;
import cn.com.satum.service.server.util.DataUtil;

import com.alibaba.fastjson.JSON;
import com.chuanglan.sms.request.SmsSendRequest;
import com.chuanglan.sms.response.SmsSendResponse;
import com.chuanglan.sms.util.ChuangLanSmsUtil;

public class SmsCheckService  implements AppService{
private final static String json="{"
		+ "\"flag\":\"register\","
		+ "\"usercode\":\"15738928228\","
		+ "}";

public static final String charset = "utf-8";
// 用户平台API账号(非登录账号,示例:N1234567)
public static String account = "CN0735736";
// 用户平台API密码(非登录密码)
public static String pswd = "cnrGRmL1Jq5be8";


private final AppBo bo=new AppBo();
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		//请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		Map mp=JSONObject.fromObject(jsondata);
		String flag=mp.get("flag").toString();
		String usercode=mp.get("usercode").toString();
		Map map=new HashMap();
		map.put("flag", flag);
		map.put("usercode", usercode);		
		int randNum = getRandNum(1,999999);
		map.put("num", randNum);
		// 短信内容
	    String msg = "【斯塔姆智能科技】您好,你的验证码是"+randNum+",仅用于注册校验，请勿告知他人。";
		//手机号码
		String phone = usercode;
		//状态报告
		String report= "true";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);	
		String requestJson = JSON.toJSONString(smsSingleRequest);		
		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		String result="1";
		Map maps=JSONObject.fromObject(response);
		if(maps.get("errorMsg")==null)
			result="2";		
		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
		//短信日志表中存储数据
		bo.runSQL("insert into sh_sms_log (id,type,code,content,valid,mobile,request) "
				+ "values('"+new DataUtil().getUUID()+"','"+flag+"','"+randNum+"','"+msg+"','120','"+usercode+"','"+response+"')");
		return JSONObject.fromObject(map).toString();
	}
	public static int getRandNum(int min, int max) {
	    int randNum = min + (int)(Math.random() * ((max - min) + 1));
	    return randNum;
	}

}
