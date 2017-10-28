package cn.com.satum.util;
import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Register {

		public static Class toClass(String className) throws Exception{
			Class c;
			try {
				c = Class.forName(className); 
				return c;
		      }catch (ClassNotFoundException ex) {
		    	  throw new ClassNotFoundException("�����෢�������뽫���ļ���" + className + " ����J2EE��������Ŀ���·���С�");
		      }catch (Exception ex){
		    	  throw new Exception("ʵ��������" + className + "������������Ԥ֪�Ĵ���");
		      }
		}
		
		public static Object toBean(Class c) throws Exception{
			try{
		        return c.newInstance();
			}catch (IllegalAccessException ex) {
		    	  throw new IllegalAccessException("ʵ��������" + c.getName() + "�������󣬷Ƿ�����.");
		      }catch (InstantiationException ex) {
		    	  throw new InstantiationException("����" + c.getName() + "��һ���������ӿڣ��޷�����ʵ����.");
		      }catch (Exception ex){
		    	  throw new Exception("ʵ��������" + c.getName() + "������������Ԥ֪�Ĵ���");
		      }
		}
		
		public static Object toBean(String className) throws Exception {
			try {
				Class c = toClass(className);
				return toBean(c);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
		}
		
		public static Object toBean(Class c, Class[] argCls, Object[] args)
				throws Exception {
			try {
				Constructor c1 = c.getDeclaredConstructor(argCls);
				c1.setAccessible(true);
				return c1.newInstance(args);
			} catch (IllegalAccessException ex) {
				throw new IllegalAccessException("ʵ��������������" + c.getName()
						+ "�������󣬷Ƿ�����.");
			} catch (InstantiationException ex) {
				throw new InstantiationException("���ζ���" + c.getName()
						+ "��һ���������ӿڣ��޷�����ʵ����.");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception("ʵ��������������" + c.getName() + "������������Ԥ֪�Ĵ���");
			}
		}
		
		public static Object toBean(String className, Class[] argCls, Object[] args) throws Exception {
			try {
				Class c = toClass(className);
				return toBean(c, argCls, args);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
		}
		
		
	
		}
		
		
		
	


