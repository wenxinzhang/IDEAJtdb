package com.jt.manage.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.ContentCategory;
import com.jt.manage.service.ContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	//查询	/content/category/list
	@RequestMapping("list")
	@ResponseBody
	public List<ContentCategory> list(@RequestParam(defaultValue="0")Long id){
		ContentCategory param = new ContentCategory();
		param.setParent_id(id);
		
		return contentCategoryService.queryListByWhere(param);
	}
	
	//新增	/content/category/create	parentId+name
	@RequestMapping("create")
	@ResponseBody
	public SysResult create(Long parentId, String name){
		ContentCategory contentCategory = new ContentCategory();
		contentCategory.setParent_id(parentId);
		contentCategory.setName(name);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(contentCategory.getCreated());
		contentCategory.setIsParent(false);
		contentCategory.setStatus(1);		//1正常2删除
		
		//如果父节点是叶子节点，isParent状态修改为true；
		ContentCategory ccParent = contentCategoryService.queryById(parentId);
		if(!ccParent.getIsParent()){
			ccParent.setIsParent(true);
			contentCategoryService.updateSelective(ccParent);
		}
		
		contentCategoryService.save(contentCategory);
		
		return SysResult.ok(contentCategory);
	}
	
	//修改	/content/category/update
	@RequestMapping("update")
	@ResponseBody
	public void update(Long id, String name){
		ContentCategory contentCategory = new ContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		
		contentCategoryService.updateSelective(contentCategory);
	}
	
	//删除	/content/category/delete/
	@RequestMapping("delete")
	@ResponseBody
	public void delete(Long parentId, Long id){
		contentCategoryService.deleteCase(parentId, id);
	}
}
