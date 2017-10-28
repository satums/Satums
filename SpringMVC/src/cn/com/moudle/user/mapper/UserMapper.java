package cn.com.moudle.user.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import cn.com.moudle.user.entity.User;



public class UserMapper implements RowMapper<User>{
	public User mapRow(ResultSet rs,int index) throws SQLException{
	User user = new User();
	user.setCode(rs.getString("code"));
	user.setDepartcode(rs.getString("departcode"));
	user.setId(rs.getInt("id"));
	user.setPersonpk(rs.getString("personpk"));
	user.setPwd(rs.getString("pwd"));
	user.setSeqno(rs.getString("seqno"));
	user.setStatus(rs.getString("status"));
	user.setUsername(rs.getString("username"));
	return user;
}

	
}