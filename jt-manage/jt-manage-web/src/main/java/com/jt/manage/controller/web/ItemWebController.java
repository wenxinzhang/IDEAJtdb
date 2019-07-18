package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.Item;
import com.jt.manage.service.ItemService;

@Controller
public class ItemWebController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/web/item/{id}")
	@ResponseBody
	public Item get(@PathVariable Long id){
		return itemService.queryById(id);
	}
}
