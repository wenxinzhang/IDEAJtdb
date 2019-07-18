package com.jt.manage.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Content;
import com.jt.manage.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;

	//列表/content/query/list',queryParams:{categoryId:0}
	@RequestMapping("query/list")
	@ResponseBody
	public EasyUIResult list(Long categoryId, Integer page, Integer rows){
		return contentService.queryContentList( page, rows, categoryId);
	}
	
	//新增保存	/content/save
	@RequestMapping("save")
	@ResponseBody
	public SysResult save(Content content){
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		contentService.save(content);
		
		return SysResult.ok();
	}
	
	//修改保存	/content/edit
	@RequestMapping("edit")
	@ResponseBody
	public SysResult edit(Content content){
		content.setUpdated(new Date());
		contentService.updateSelective(content);
		
		return SysResult.ok();
	}
	
	//删除	/content/delete
	@RequestMapping("delete")
	@ResponseBody
	public SysResult delete(Long[] ids){
		contentService.deleteByIds(ids);
		
		return SysResult.ok();
	}
}
