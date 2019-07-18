package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.web.pojo.Item;

@Service
public class ItemWebService {
	@Autowired
	private HttpClientService httpClientService;
	private ObjectMapper MAPPER = new ObjectMapper();
	
	@PropertyConfig
	private String MANAGE_URL;
	
	public Item getItem(Long id) throws Exception{
		//利用httpclient去后台系统中查询某个商品
		String url = MANAGE_URL + "/web/item/"+id;
		String jsonStr = httpClientService.doGet(url);
		
		//转换失败
		Item item = MAPPER.readValue(jsonStr, Item.class);
		return item;
	}
}
