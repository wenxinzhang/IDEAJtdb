package com.jt.manage.controller.web;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.vo.ItemCatResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.service.ItemCatService;

@Controller
public class ItemCatWebController {
	@Autowired
	private ItemCatService itemCatService;
	@Autowired
	private RedisService redisService;
	private ObjectMapper MAPPER = new ObjectMapper();	//jackson
	
	//http://manage.jt.com/web/itemcat/all?callback=category.getDataService
	//查询系统分类，name+url+subItemCats ItemCatResult
	//查询状态为正常的分类
	@RequestMapping("/web/itemcat/all")
	@ResponseBody
	public ItemCatResult queryItemCatAll(){
		ItemCatResult result = null;
		String key = "ITEM_CAT_WEB_URL";
		//第二次访问从缓存中获取
		String jsonData = redisService.get(key);
		if(StringUtils.isNotEmpty(jsonData)){
			//将json串转换为java对象
			try {
				result = MAPPER.readValue(jsonData, ItemCatResult.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			result = itemCatService.queryWebAll();
			
			//第一次访问后，将数据写缓存，这个key在整个系统中唯一
			//将java对象转换成json串
			try {	//缓存的异常不能抛出，必须自己处理，一般记录日志
				redisService.set(key, MAPPER.writeValueAsString(result));
			} catch (JsonProcessingException e) {
				//记录日志
				e.printStackTrace();
			}
		}
		
		return result;	//自动拼接where条件
	}
}
