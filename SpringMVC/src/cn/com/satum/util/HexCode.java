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
	     * @return 二进制数组转换为二进制字符串   2-2
	     */  
	    public static String bytes2BinStr(byte[] bArray){  

	        String outStr = "";  
	        int pos = 0;  
	        for(byte b:bArray){  
	            //高四位  
	            pos = (b&0xF0)>>4;  
	            outStr+=binaryArray[pos];  
	            //低四位  
	            pos=b&0x0F;  
	            outStr+=binaryArray[pos];  
	        }  
	        return outStr;  
	    }  
	    /** 
	     *  
	     * @param hexString 
	     * @return 将十六进制转换为二进制字节数组   16-2
	     */  
	    public static byte[] hexStr2BinArr(String hexString){  
	        //hexString的长度对2取整，作为bytes的长度  
	        int len = hexString.length()/2;  
	        byte[] bytes = new byte[len];  
	        byte high = 0;//字节高四位  
	        byte low = 0;//字节低四位  
	        for(int i=0;i<len;i++){  
	             //右移四位得到高位  
	             high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);  
	             low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));  
	             bytes[i] = (byte) (high|low);//高地位做或运算  
	        }  
	        
	        return bytes;  
	    }

	      /** 
	     *  
	     * @param bytes 
	     * @return 将二进制数组转换为十六进制字符串  2-16
	     */  
	    public static String bin2HexStr(String str){  
byte[] bytes=str.getBytes();
	        String result = "";  
	        String hex = "";  
	        for(int i=0;i<bytes.length;i++){  
	            //字节高4位  
	            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
	            //字节低4位  
	            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
	            result +=hex;  //+" "
	        }  
	        return result;  
	    }   

	    /** 
	     *  
	     * @param hexString 
	     * @return 将十六进制转换为二进制字符串   16-2 
	     */  
	    public static String hexStr2BinStr(String hexString){
	        return bytes2BinStr(hexStr2BinArr(hexString));
	    }
	    /**
	     * 字符串转化成为16进制字符串
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
	    public static String byte2hex(byte[] b) { // 二进制转字符串  
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
	    // 将Unicode字符串转换成bool型数组  
	    public boolean[] StrToBool(String input) {  
	        boolean[] output = Binstr16ToBool(BinstrToBinstr16(StrToBinstr(input)));  
	        return output;  
	    }  
	  
	    // 将bool型数组转换成Unicode字符串  
	    public String BoolToStr(boolean[] input) {  
	        String output = BinstrToStr(Binstr16ToBinstr(BoolToBinstr16(input)));  
	        return output;  
	    }  
	  
	    // 将字符串转换成二进制字符串，以空格相隔  
	    private String StrToBinstr(String str) {  
	        char[] strChar = str.toCharArray();  
	        String result = "";  
	        for (int i = 0; i < strChar.length; i++) {  
	            result += Integer.toBinaryString(strChar[i]) + " ";  
	        }  
	        return result;  
	    }  
	  
	    // 将二进制字符串转换成Unicode字符串  
	    private String BinstrToStr(String binStr) {  
	        String[] tempStr = StrToStrArray(binStr);  
	        char[] tempChar = new char[tempStr.length];  
	        for (int i = 0; i < tempStr.length; i++) {  
	            tempChar[i] = BinstrToChar(tempStr[i]);  
	        }  
	        return String.valueOf(tempChar);  
	    }  
	  
	    // 将二进制字符串格式化成全16位带空格的Binstr  
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
	  
	    // 将全16位带空格的Binstr转化成去0前缀的带空格Binstr  
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
	  
	    // 二进制字串转化为boolean型数组 输入16位有空格的Binstr  
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
	  
	    // boolean型数组转化为二进制字串 返回带0前缀16位有空格的Binstr  
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
	  
	    // 将二进制字符串转换为char  
	    private static char BinstrToChar(String binStr) {  
	        int[] temp = BinstrToIntArray(binStr);  
	        int sum = 0;  
	        for (int i = 0; i < temp.length; i++) {  
	            sum += temp[temp.length - 1 - i] << i;  
	        }  
	        return (char) sum;  
	    }  
	  
	    // 将初始二进制字符串转换成字符串数组，以空格相隔  
	    private static String[] StrToStrArray(String str) {  
	        return str.split(" ");  
	    }  
	  
	    // 将二进制字符串转换成int数组  
	    private static int[] BinstrToIntArray(String binStr) {  
	        char[] temp = binStr.toCharArray();  
	        int[] result = new int[temp.length];  
	        for (int i = 0; i < temp.length; i++) {  
	            result[i] = temp[i] - 48;  
	        }  
	        return result;  
	    }  
	   
	    public static void main(String[] args) throws UnsupportedEncodingException {
	    	//字符串转二进制
	    	 String str = "{\"username\":\"谭正彪\"}";
	    	  char[] strChar=str.toCharArray();
	    	  String result="";
	    	  for(int i=0;i<strChar.length;i++){
	    	    result +=Integer.toBinaryString(strChar[i])+ " ";
	    	  }
	    	  System.out.println(result);
	    	//将二进制字符串转换成Unicode字符串
	    	 String binSt=result;
	    	  String[] tempStr=StrToStrArray(result);
	    	  char[] tempChar=new char[tempStr.length];
	    	  for(int i=0;i<tempStr.length;i++) {
	    	  tempChar[i]=BinstrToChar(tempStr[i]);
	    	  }
	    	 System.out.println(new String(tempChar));
	    	  
		}
}
