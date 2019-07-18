package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	public static final String cookieName = "JT_TICKET";
	private static final Integer maxage = 60*60*24;			//一天内有效
	
	//转向注册页面	http://www.jt.com/user/register.html
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	
	//转向登录页面	http://www.jt.com/user/login.html
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	//注册	/service/user/doRegister
	//冲突：前台页面为了让搜索的机器人抓取，给访问URL后缀加了.html。它和json请求发生冲突
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user){
		return userService.doRegister(user);
	}
	
	//登录	service/user/doLogin
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user, HttpServletRequest request, HttpServletResponse response){
		SysResult result = userService.doLogin(user);
		String ticket = (String)result.getData();

		//写cookie
        CookieUtils.setCookie(request, response, cookieName, ticket, maxage);
        
        
		return userService.doLogin(user);
	}
}
