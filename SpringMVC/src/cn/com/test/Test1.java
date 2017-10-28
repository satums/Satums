package cn.com.test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.Data.Bo.AppBo;
import cn.com.moudle.user.dao.UserDAO;
import cn.com.moudle.user.entity.User;
import cn.com.moudle.user.mapper.UserMapper;

public class Test1 {
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {
	this.template = template;
}

	public static void main(String[] args) throws SQLException {
		
		AppBo bo=new AppBo();
		// TODO Auto-generated method stub
		/**
ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");

UserDAO ud=ac.getBean("UserDAO",UserDAO.class);


List<User> list=ud.queryAll();

for(User user:list){
	System.out.println(user.getCode()+"===="+user.getUsername());
}
**/
		String sql="select * from ou_department";
		List list=bo.query(sql);
		System.out.println("===="+list);
	
	}
}
