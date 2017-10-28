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
		    	  throw new ClassNotFoundException("查找类发生错误，请将类文件：" + className + " 放置J2EE容器的项目相对路径中。");
		      }catch (Exception ex){
		    	  throw new Exception("实例化对象" + className + "产生其他不可预知的错误！");
		      }
		}
		
		public static Object toBean(Class c) throws Exception{
			try{
		        return c.newInstance();
			}catch (IllegalAccessException ex) {
		    	  throw new IllegalAccessException("实例化对象" + c.getName() + "发生错误，非法参数.");
		      }catch (InstantiationException ex) {
		    	  throw new InstantiationException("对象" + c.getName() + "是一个抽象类或接口，无法进行实例化.");
		      }catch (Exception ex){
		    	  throw new Exception("实例化对象" + c.getName() + "产生其他不可预知的错误！");
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
				throw new IllegalAccessException("实例化带参数对象" + c.getName()
						+ "发生错误，非法参数.");
			} catch (InstantiationException ex) {
				throw new InstantiationException("带参对象" + c.getName()
						+ "是一个抽象类或接口，无法进行实例化.");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception("实例化带参数对象" + c.getName() + "产生其他不可预知的错误！");
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
		
		
		
	


