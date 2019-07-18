package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	//通用转向	http://localhost:8081/page/item-add
	@RequestMapping("/page/{pageName}")
	public String goPage(@PathVariable()String pageName){
		return pageName;
	}
}
