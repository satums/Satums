package cn.com.satum.service.server.satums.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import cn.com.satum.util.Reciver;
import cn.com.satum.util.Sender;

public class TestSoket {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
Sender sd=new Sender();
Reciver rc=new Reciver();
sd.send("̷����-----------------");
/**
ServerSocket serverSocket=null;
      Socket socket=null;
      String msg="hello client,I am server..";
      try {
         
         //����ServerSocketʵ����ָ���˿ڼ����ͻ��˵���������
         serverSocket=new ServerSocket(8080);
         //�������ͻ��˵�����
         socket=serverSocket.accept();
         
         //��ͻ��˷�����Ϣ
         OutputStream os=socket.getOutputStream();
         os.write(msg.getBytes());
         InputStream is=socket.getInputStream();
         
         //���ܿͻ��˵���Ӧ
         byte[] b=new byte[1024];
         is.read(b);
         System.out.println(new String(b+"666666666666666666"));
         
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         //�����������ر�socket 
          try {
            serverSocket.close();
            socket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }  **/

	}

}
