
package cn.com.encode;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESCUtils {

 // ��Կ
 private static Key key;
 // KEY����
 private static String KEY_STR = "encrypt@cncounter.com";
 // ����
 public static final String UTF_8 = "UTF-8";
 public static final String DES = "DES";

 // ��̬��ʼ��
 static{
  try {
   // KEY ������
   KeyGenerator generator = KeyGenerator.getInstance(DES);
   // ��ʼ��,��ȫ�������
   generator.init(new SecureRandom( KEY_STR.getBytes(UTF_8) ));
   // ������Կ
   key = generator.generateKey();
   generator = null;
  } catch (Exception e) {
   throw new RuntimeException(e);
  }
 }

 /**
  * ��Դ�ַ�������,���� BASE64�����ļ����ַ���
  * @param source Դ�ַ���,����
  * @return �����ַ���
  */
 public static String encode(String source){
  try {
   // ���ݱ����ʽ��ȡ�ֽ�����
   byte[] sourceBytes = source.getBytes(UTF_8);
   // DES ����ģʽ
   Cipher cipher = Cipher.getInstance(DES);
   cipher.init(Cipher.ENCRYPT_MODE, key);
   // ���ܺ���ֽ�����
   byte[] encryptSourceBytes = cipher.doFinal(sourceBytes);
   // Base64������
   BASE64Encoder base64Encoder = new BASE64Encoder();
   return base64Encoder.encode(encryptSourceBytes);
  } catch (Exception e) {
   // throw Ҳ����һ�� return ·��
   throw new RuntimeException(e);
  }
 }

 /**
  * �Ա������� encode() �������ܺ���ַ������н���/����
  * @param encrypted �����ܹ����ַ���,������
  * @return �����ַ���
  */
 public static String decode(String encrypted){
  // Base64������
  BASE64Decoder base64Decoder = new BASE64Decoder();
  try {
   // �Ƚ���base64����
   byte[] cryptedBytes = base64Decoder.decodeBuffer(encrypted);
   // DES ����ģʽ
   Cipher cipher = Cipher.getInstance(DES);
   cipher.init(Cipher.DECRYPT_MODE, key);
   // �������ֽ�����
   byte[] decryptStrBytes = cipher.doFinal(cryptedBytes);
   // ���ø��������ʽ���ֽ��������ַ���
   return new String(decryptStrBytes, UTF_8);
  } catch (Exception e) {
   // ������ʽȷʵ�ʺϴ�������
   throw new RuntimeException(e);
  }
 }

 // �� s ���� BASE64 ����  

 public String getBASE64(String s){  

 if (s == null){ return null;}  
 else{
 return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );  
 }
 }  

 // �� BASE64 ������ַ��� s ���н���  

 public String getFromBASE64(String s){  

 if (s == null) return null;  

 BASE64Decoder decoder = new BASE64Decoder();  

 try {  

 byte[] b = decoder.decodeBuffer(s);  

 return new String(b);  

 } catch (Exception e) {  

 return null;  

 }  

 }
 // ��Ԫ����
 public static void main(String[] args) {
	 DESCUtils d=new DESCUtils();
	String a= d.getBASE64("1");
	String b=d.getFromBASE64("MQ==");
	  System.out.println("b=== " + b);
	  /**
  // ��Ҫ���ܵ��ַ���
  String email = "1";
  // ����
  String encrypted = d.encode(email);
  // ����
  String decrypted = d.decode(encrypted);

  // ������;
  System.out.println("email: " + email);
  System.out.println("encrypted: " + encrypted);
 // System.out.println("decrypted: " + decrypted);
  //System.out.println("email.equals(decrypted): " + email.equals(decrypted));
   * **/
   
 }
}
