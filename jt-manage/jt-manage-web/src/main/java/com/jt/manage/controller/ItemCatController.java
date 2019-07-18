package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	///item/cat/list
	@RequestMapping("/item/cat/list")
	@ResponseBody		//返回json格式串
	public List<ItemCat> list(@RequestParam(defaultValue="0")Integer id){
		return itemCatService.queryItemCatList(id);
	}
}
