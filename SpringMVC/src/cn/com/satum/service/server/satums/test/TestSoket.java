package cn.com.satum.service.server.satums.test;

import cn.com.satum.util.Reciver;
import cn.com.satum.util.Sender;

public class TestSoket {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
Sender sd=new Sender();
Reciver rc=new Reciver();
sd.send();
rc.recive();
	}

}
