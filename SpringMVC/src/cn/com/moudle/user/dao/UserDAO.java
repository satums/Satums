package cn.com.moudle.user.dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.com.moudle.user.entity.User;
import cn.com.moudle.user.entity.UserData;
import cn.com.moudle.user.mapper.UserMapper;

public class UserDAO implements UserData{
private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {
	this.template = template;
}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> queryAll() {
		// TODO Auto-generated method stub
		RowMapper rmp=new UserMapper();
		List list=template.query("select * from ou_user", rmp);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User queryByCode(String code) {
		RowMapper rmp=new UserMapper();
		String sql="select * from ou_user where code=?";
		Object[] param={code};
		User user=template.queryForObject(sql, param,rmp);
		return user;
		// TODO Auto-generated method stub
		
	}
	
}
