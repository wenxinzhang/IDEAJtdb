package com.jt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.service.ItemWebService;

@Controller
public class ItemWebUIController {
	@Autowired
	private ItemWebService itemWebService;
	
	//查询商品信息，转向商品页面 	http://www.jt.com/item/id.html
	@RequestMapping("/item/{id}")
	public String item(@PathVariable Long id, Model model) throws Exception{
		//调用后台系统中获取某个商品的信息
		
		model.addAttribute("item", itemWebService.getItem(id));
		
		return "item";
	}
	
}
