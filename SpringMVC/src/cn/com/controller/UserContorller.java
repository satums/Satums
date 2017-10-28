package cn.com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.com.moudle.user.dao.UserDAO;
import cn.com.moudle.user.entity.User;


@Controller
public class UserContorller {
private UserDAO ud;
@Autowired
public void setUd(UserDAO ud) {
	this.ud = ud;
}

@RequestMapping("user/list")
public  String excute(Model model){
	List<User> list=ud.queryAll();
	model.addAttribute("users", list);
	return "login";
	
}

}
