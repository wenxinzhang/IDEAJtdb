package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jt.common.service.HttpClientService;

@Service
public class IndexService {
	@Autowired
	private HttpClientService httpClientService;
	
	//获取大广告位数据
	
	public String getIndexAD1() throws Exception{
		//准备大广告数据	indexAD1，访问后台系统获取大广告位数据，异构语言访问方式来在代码中直接发出http请求，并获得响应
		
		String url = "http://manage.jt.com/web/content/ad1/9";
		return httpClientService.doGet(url, "utf-8");
	}
}
