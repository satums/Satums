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
// �û�ƽ̨API�˺�(�ǵ�¼�˺�,ʾ��:N1234567)
public static String account = "CN0735736";
// �û�ƽ̨API����(�ǵ�¼����)
public static String pswd = "cnrGRmL1Jq5be8";


private final AppBo bo=new AppBo();
	@Override
	public String getInfo(String jsondata) {
		// TODO Auto-generated method stub
		//�����ַ���¼253��ͨѶ����ͨƽ̨�鿴����ѯ�������������˻�ȡ
				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		Map mp=JSONObject.fromObject(jsondata);
		String flag=mp.get("flag").toString();
		String usercode=mp.get("usercode").toString();
		Map map=new HashMap();
		map.put("flag", flag);
		map.put("usercode", usercode);		
		int randNum = getRandNum(1,999999);
		map.put("num", randNum);
		// ��������
	    String msg = "��˹��ķ���ܿƼ�������,�����֤����"+randNum+",������ע��У�飬�����֪���ˡ�";
		//�ֻ�����
		String phone = usercode;
		//״̬����
		String report= "true";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);	
		String requestJson = JSON.toJSONString(smsSingleRequest);		
		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		String result="1";
		Map maps=JSONObject.fromObject(response);
		if(maps.get("errorMsg")==null)
			result="2";		
		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
		//������־���д洢����
		bo.runSQL("insert into sh_sms_log (id,type,code,content,valid,mobile,request) "
				+ "values('"+new DataUtil().getUUID()+"','"+flag+"','"+randNum+"','"+msg+"','120','"+usercode+"','"+response+"')");
		return JSONObject.fromObject(map).toString();
	}
	public static int getRandNum(int min, int max) {
	    int randNum = min + (int)(Math.random() * ((max - min) + 1));
	    return randNum;
	}

}
