package com.jt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.common.util.CookieUtils;
import com.jt.web.controller.UserController;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;
import com.jt.web.threadlocal.UserThreadLocal;

public class CartInterceptor implements HandlerInterceptor{
	@Autowired
	private UserService userService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//判断用户是否登录
		//先获取cookie中值
		String ticket = CookieUtils.getCookieValue(request, UserController.cookieName);
		if(ticket==null){
			UserThreadLocal.set(null);
			response.sendRedirect("/user/login.html");
            return false;
		}
		
		User _user = userService.queryUserByTicket(ticket);
		if(_user==null){
			UserThreadLocal.set(null);
            response.sendRedirect("/user/login.html");
            return false;
		}
		
		//设置登录信息
		if(UserThreadLocal.get()==null){
			UserThreadLocal.set(_user);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
