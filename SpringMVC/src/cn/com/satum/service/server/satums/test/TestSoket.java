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
sd.send("谭正彪-----------------");
/**
ServerSocket serverSocket=null;
      Socket socket=null;
      String msg="hello client,I am server..";
      try {
         
         //构造ServerSocket实例，指定端口监听客户端的连接请求
         serverSocket=new ServerSocket(8080);
         //建立跟客户端的连接
         socket=serverSocket.accept();
         
         //向客户端发送消息
         OutputStream os=socket.getOutputStream();
         os.write(msg.getBytes());
         InputStream is=socket.getInputStream();
         
         //接受客户端的响应
         byte[] b=new byte[1024];
         is.read(b);
         System.out.println(new String(b+"666666666666666666"));
         
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         //操作结束，关闭socket 
          try {
            serverSocket.close();
            socket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }  **/

	}

}
