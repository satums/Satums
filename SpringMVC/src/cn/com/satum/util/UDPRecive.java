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
	            //�����˿���Ϊ1234
	            server_sock=new DatagramSocket(8088);
	            recv_buffer=new byte[1024];//���ջ�������byte��
	            pac=new DatagramPacket(recv_buffer,recv_buffer.length);//����һ��packet
	            recv_string="";
	          
	            while(true)//ѭ����������
	            {
	                server_sock.receive(pac);//����ʽ��������
	                //��byte[]ת����string
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

