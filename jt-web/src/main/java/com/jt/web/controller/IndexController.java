package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.util.CookieUtils;
import com.jt.web.service.IndexService;

@Controller
public class IndexController {
	@Autowired
	private IndexService indexService;
	@Autowired
	private RedisService redisService;
	private static ObjectMapper MAPPER = new ObjectMapper();
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, Model model) throws Exception{
		//准备大广告数据	indexAD1，访问后台系统获取大广告位数据，异构语言访问方式来在代码中直接发出http请求，并获得响应
		
		String adData = indexService.getIndexAD1();
		model.addAttribute("indexAD1", adData);
		
		//先从cookie中获取当前用户的ticket，然后通过ticket去redis中获取当前user的json
		String ticket = CookieUtils.getCookieValue(request, UserController.cookieName);
		
		if(StringUtils.isNotEmpty(ticket)){
			try{
				//获取当前用户，写入model中
				String jsonData = redisService.get(ticket);
				JsonNode jsonNode = MAPPER.readTree(jsonData);
				String username = jsonNode.get("username").asText();
				model.addAttribute("username", username);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return "index";
	}
}
