package cn.com.satum.util;

	import java.io.*;
	import java.lang.*;
	import java.net.*;
	public class UDPRecive
	{
	    private DatagramSocket server_sock;
	    private DatagramPacket pac;
	    private byte recv_buffer[];
	    private String recv_string;
	    public UDPRecive(){
	        Init();
	    }
	    public void Init()
	    {
	        try
	        { 
	            //监听端口设为1234
	            server_sock=new DatagramSocket(8088);
	            recv_buffer=new byte[1024];//接收缓冲区，byte型
	            pac=new DatagramPacket(recv_buffer,recv_buffer.length);//构造一个packet
	            recv_string="";
	          
	            while(true)//循环接受数据
	            {
	                server_sock.receive(pac);//阻塞式接收数据
	                //将byte[]转化成string
	               recv_string=new String(recv_buffer,0,pac.getLength());
	               System.out.println(recv_string);
	               
	              }

	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }

	    }

	    public static void main(String args[])
	    {
	        new UDPRecive();
	    }

	} 

