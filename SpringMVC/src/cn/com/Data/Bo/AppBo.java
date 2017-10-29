package cn.com.Data.Bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.com.moudle.user.mapper.UserMapper;

public class AppBo  {
	private static JdbcTemplate template;
	public JdbcTemplate getJdbcTemplate() {  
        return template;  
    }  
	public void setTemplate(JdbcTemplate template) {
	this.template = template;
}
	public static List query(String sql){	
		List list=template.queryForList(sql);
		//List list1=template.queryForList("select * from ou_user");
		
		return list;
	}
	public static void runSQL(String sql){	
		template.execute(sql);
		//return list;
	}
	public static void setDataSource(String dbName){
		template.setDatabaseProductName(dbName);
	}
	
}


