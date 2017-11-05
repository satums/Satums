package cn.com.satum.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexCode {
	private static String hexStr =  "0123456789ABCDEF"; 
	private static String[] binaryArray =   
	        {"0000","0001","0010","0011",  
	        "0100","0101","0110","0111",  
	        "1000","1001","1010","1011",  
	        "1100","1101","1110","1111"};  

	 /** 
	     *  
	     * @param str 
	     * @return ����������ת��Ϊ�������ַ���   2-2
	     */  
	    public static String bytes2BinStr(byte[] bArray){  

	        String outStr = "";  
	        int pos = 0;  
	        for(byte b:bArray){  
	            //����λ  
	            pos = (b&0xF0)>>4;  
	            outStr+=binaryArray[pos];  
	            //����λ  
	            pos=b&0x0F;  
	            outStr+=binaryArray[pos];  
	        }  
	        return outStr;  
	    }  
	    /** 
	     *  
	     * @param hexString 
	     * @return ��ʮ������ת��Ϊ�������ֽ�����   16-2
	     */  
	    public static byte[] hexStr2BinArr(String hexString){  
	        //hexString�ĳ��ȶ�2ȡ������Ϊbytes�ĳ���  
	        int len = hexString.length()/2;  
	        byte[] bytes = new byte[len];  
	        byte high = 0;//�ֽڸ���λ  
	        byte low = 0;//�ֽڵ���λ  
	        for(int i=0;i<len;i++){  
	             //������λ�õ���λ  
	             high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);  
	             low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));  
	             bytes[i] = (byte) (high|low);//�ߵ�λ��������  
	        }  
	        
	        return bytes;  
	    }

	      /** 
	     *  
	     * @param bytes 
	     * @return ������������ת��Ϊʮ�������ַ���  2-16
	     */  
	    public static String bin2HexStr(String str){  
byte[] bytes=str.getBytes();
	        String result = "";  
	        String hex = "";  
	        for(int i=0;i<bytes.length;i++){  
	            //�ֽڸ�4λ  
	            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
	            //�ֽڵ�4λ  
	            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
	            result +=hex;  //+" "
	        }  
	        return result;  
	    }   

	    /** 
	     *  
	     * @param hexString 
	     * @return ��ʮ������ת��Ϊ�������ַ���   16-2 
	     */  
	    public static String hexStr2BinStr(String hexString){
	        return bytes2BinStr(hexStr2BinArr(hexString));
	    }
	    /**
	     * �ַ���ת����Ϊ16�����ַ���
	     * @param s
	     * @return
	     */
	    public static String strTo16(String s) {
	        String str = "";
	        for (int i = 0; i < s.length(); i++) {
	            int ch = (int) s.charAt(i);
	            String s4 = Integer.toHexString(ch);
	            str = str + s4;
	        }
	        return str;
	    }
	    public static String byte2hex(byte[] b) { // ������ת�ַ���  
	        String hs = "";  
	        String stmp = "";  
	        for (int n = 0; n < b.length; n++) {  
	         stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
	         if (stmp.length() == 1)  
	          hs = hs + "0" + stmp;  
	         else  
	          hs = hs + stmp;  
	        }  
	        return hs;  
	     }  
	    // ��Unicode�ַ���ת����bool������  
	    public boolean[] StrToBool(String input) {  
	        boolean[] output = Binstr16ToBool(BinstrToBinstr16(StrToBinstr(input)));  
	        return output;  
	    }  
	  
	    // ��bool������ת����Unicode�ַ���  
	    public String BoolToStr(boolean[] input) {  
	        String output = BinstrToStr(Binstr16ToBinstr(BoolToBinstr16(input)));  
	        return output;  
	    }  
	  
	    // ���ַ���ת���ɶ������ַ������Կո����  
	    private String StrToBinstr(String str) {  
	        char[] strChar = str.toCharArray();  
	        String result = "";  
	        for (int i = 0; i < strChar.length; i++) {  
	            result += Integer.toBinaryString(strChar[i]) + " ";  
	        }  
	        return result;  
	    }  
	  
	    // ���������ַ���ת����Unicode�ַ���  
	    private String BinstrToStr(String binStr) {  
	        String[] tempStr = StrToStrArray(binStr);  
	        char[] tempChar = new char[tempStr.length];  
	        for (int i = 0; i < tempStr.length; i++) {  
	            tempChar[i] = BinstrToChar(tempStr[i]);  
	        }  
	        return String.valueOf(tempChar);  
	    }  
	  
	    // ���������ַ�����ʽ����ȫ16λ���ո��Binstr  
	    private String BinstrToBinstr16(String input) {  
	        StringBuffer output = new StringBuffer();  
	        String[] tempStr = StrToStrArray(input);  
	        for (int i = 0; i < tempStr.length; i++) {  
	            for (int j = 16 - tempStr[i].length(); j > 0; j--)  
	                output.append('0');  
	            output.append(tempStr[i] + " ");  
	        }  
	        return output.toString();  
	    }  
	  
	    // ��ȫ16λ���ո��Binstrת����ȥ0ǰ׺�Ĵ��ո�Binstr  
	    private String Binstr16ToBinstr(String input) {  
	        StringBuffer output = new StringBuffer();  
	        String[] tempStr = StrToStrArray(input);  
	        for (int i = 0; i < tempStr.length; i++) {  
	            for (int j = 0; j < 16; j++) {  
	                if (tempStr[i].charAt(j) == '1') {  
	                    output.append(tempStr[i].substring(j) + " ");  
	                    break;  
	                }  
	                if (j == 15 && tempStr[i].charAt(j) == '0')  
	                    output.append("0" + " ");  
	            }  
	        }  
	        return output.toString();  
	    }  
	  
	    // �������ִ�ת��Ϊboolean������ ����16λ�пո��Binstr  
	    private boolean[] Binstr16ToBool(String input) {  
	        String[] tempStr = StrToStrArray(input);  
	        boolean[] output = new boolean[tempStr.length * 16];  
	        for (int i = 0, j = 0; i < input.length(); i++, j++)  
	            if (input.charAt(i) == '1')  
	                output[j] = true;  
	            else if (input.charAt(i) == '0')  
	                output[j] = false;  
	            else  
	                j--;  
	        return output;  
	    }  
	  
	    // boolean������ת��Ϊ�������ִ� ���ش�0ǰ׺16λ�пո��Binstr  
	    private String BoolToBinstr16(boolean[] input) {  
	        StringBuffer output = new StringBuffer();  
	        for (int i = 0; i < input.length; i++) {  
	            if (input[i])  
	                output.append('1');  
	            else  
	                output.append('0');  
	            if ((i + 1) % 16 == 0)  
	                output.append(' ');  
	        }  
	        output.append(' ');  
	        return output.toString();  
	    }  
	  
	    // ���������ַ���ת��Ϊchar  
	    private static char BinstrToChar(String binStr) {  
	        int[] temp = BinstrToIntArray(binStr);  
	        int sum = 0;  
	        for (int i = 0; i < temp.length; i++) {  
	            sum += temp[temp.length - 1 - i] << i;  
	        }  
	        return (char) sum;  
	    }  
	  
	    // ����ʼ�������ַ���ת�����ַ������飬�Կո����  
	    private static String[] StrToStrArray(String str) {  
	        return str.split(" ");  
	    }  
	  
	    // ���������ַ���ת����int����  
	    private static int[] BinstrToIntArray(String binStr) {  
	        char[] temp = binStr.toCharArray();  
	        int[] result = new int[temp.length];  
	        for (int i = 0; i < temp.length; i++) {  
	            result[i] = temp[i] - 48;  
	        }  
	        return result;  
	    }  
	   
	    public static void main(String[] args) throws UnsupportedEncodingException {
	    	//�ַ���ת������
	    	 String str = "{\"username\":\"̷����\"}";
	    	  char[] strChar=str.toCharArray();
	    	  String result="";
	    	  for(int i=0;i<strChar.length;i++){
	    	    result +=Integer.toBinaryString(strChar[i])+ " ";
	    	  }
	    	  System.out.println(result);
	    	//���������ַ���ת����Unicode�ַ���
	    	 String binSt=result;
	    	  String[] tempStr=StrToStrArray(result);
	    	  char[] tempChar=new char[tempStr.length];
	    	  for(int i=0;i<tempStr.length;i++) {
	    	  tempChar[i]=BinstrToChar(tempStr[i]);
	    	  }
	    	 System.out.println(new String(tempChar));
	    	  
		}
}
