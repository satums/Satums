package cn.com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.com.encode.DESCUtils;
import cn.com.moudle.user.dao.UserDAO;
import cn.com.moudle.user.entity.User;
@Controller
public class UserQuery {
	DESCUtils des=new DESCUtils();
UserDAO ud;
@Autowired
public void setUd(UserDAO ud) {
	this.ud = ud;
}
@RequestMapping("queryAction.do")
public ModelAndView excute(String codes,String pwd,ModelMap model,HttpServletRequest request){
	Map map=new HashMap();
	pwd=request.getParameter("pwd");
	User user=ud.queryByCode(codes);
	String pwds=user.getPwd();
	pwds=des.getFromBASE64(pwds);
	System.out.println("password===="+pwd);
	//System.out.println("pwd======"+pwds);
	if(pwds.equals(pwd)){
	//System.out.println("0000000000000000");
	map.put("user", user);
	return new ModelAndView("index",map);
}else{
	//System.out.println("1111111===="+pwds);
	List<User> list=ud.queryAll();
	map.put("users", list);
	return new ModelAndView("login",map);
}
}
}
