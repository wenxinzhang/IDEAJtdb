package com.jt.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ContentMapper;
import com.jt.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content>{
	@Autowired 
	private ContentMapper contentMapper;

	//查询某个分类下的信息
	public EasyUIResult queryContentList(Integer page, Integer rows, Long categoryId) {
		PageHelper.startPage(page, rows);
		Content param = new Content();
		param.setCategoryId(categoryId);
		
		PageInfo<Content> pageInfo = new PageInfo<Content>(contentMapper.select(param));
		
		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	}

}
