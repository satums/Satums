package cn.com.satum.service.server.master;

public class TestService implements MasterService{
	public String getInfo(String jsondata){
		System.out.println("========"+jsondata);
		return jsondata;
	}

}
