package cn.com.satum.util;

public class HEX2And16 {
	 // 将二进制字符串转换成int数组  
    private static int[] BinstrToIntArray(String binStr) {  
        char[] temp = binStr.toCharArray();  
        int[] result = new int[temp.length];  
        for (int i = 0; i < temp.length; i++) {  
            result[i] = temp[i] - 48;  
        }  
        return result;  
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
public static String hex16tTo2(String str){
	
	  char[] strChar=str.toCharArray();
	  String result="";
	  for(int i=0;i<strChar.length;i++){
	    result +=Integer.toBinaryString(strChar[i])+ " ";
	  }
	  return result;
}
public static String hex2To16(String result){
	  String[] tempStr=result.split(" ");
	  char[] tempChar=new char[tempStr.length];
	  for(int i=0;i<tempStr.length;i++) {
	  tempChar[i]=BinstrToChar(tempStr[i]);
	  }
	return new String(tempChar);
}
public static void main(String[] args) {
	System.out.println(hex16tTo2("uuu123456"));

	System.out.println(hex2To16("1110101 1110101 1110101 110001 110010 110011 110100 110101 110110"));
}
}
