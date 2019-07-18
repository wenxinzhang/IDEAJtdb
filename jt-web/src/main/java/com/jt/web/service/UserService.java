package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserService {
	@Autowired
	private HttpClientService httpClientService;
	@Autowired
	private RedisService redisService;
	
	@PropertyConfig
	private String SSO_URL;
	private static ObjectMapper MAPPER = new ObjectMapper();
	
	//注册
	public SysResult doRegister(User user){
		//httpClient访问SSO
		String url = SSO_URL + "/user/register";
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		
		try {
			httpClientService.doPost(url, params, "utf-8");
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "httpClient访问后台系统失败!");
		}
	}

	//登录
	public SysResult doLogin(User user) {
		/*
		 * 验证用户输入的用户名和密码，
		 * 1、直接去单点来验证
		 * 2、如果成功，返回当前ticket
		 * 3、写cookie
		 * 4、如果不成功错误信息
		 */
		String url = "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<String,String>();
		params.put("u", user.getUsername());
		params.put("p", user.getPassword());
		
		
		try {
			String jsonData = httpClientService.doPost(url, params);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
            String ticket = jsonNode.get("data").asText();
            
            
			return SysResult.ok(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "登录失败!");
		}
	}

	public User queryUserByTicket(String ticket) {
		String jsonData = redisService.get(ticket);
		try {
			User _user = MAPPER.readValue(jsonData, User.class);
			return _user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
