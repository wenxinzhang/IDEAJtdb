package com.jt.manage.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.mapper.ContentMapper;
import com.jt.manage.pojo.Content;

@Controller
public class ContentWebController {
	@Autowired
	private ContentMapper contentMapper;
	
	//获取大广告位数据
	@RequestMapping("/web/content/ad1/{categoryId}")
	@ResponseBody
	public String list(@PathVariable Long categoryId){
		Content param = new Content();
		param.setCategoryId(categoryId);
		
		List<Content> contentList = contentMapper.select(param);
		StringBuilder sBuf = new StringBuilder();
		//[{},{}]
		sBuf.append("[");
		int pos = 1;
		for(Content content : contentList){
			sBuf.append("{");
			sBuf.append("width: 730, height: 454,");
			sBuf.append("href:'").append(content.getUrl()).append("',");
			sBuf.append("alt: '',");
			sBuf.append("src: '").append(content.getPic()).append("',");
			sBuf.append("ext1: '',");
			sBuf.append("index: '").append(pos++).append("',");
			sBuf.append("widthB: 510, heightB: 454,");
			sBuf.append("srcB: '").append(content.getPic2()).append("'");
			
			sBuf.append("},");
		}
		if(sBuf!=null && sBuf.length()>0){
			sBuf.delete(sBuf.length()-1, sBuf.length());	//删除最后一个元素
		}
		sBuf.append("]");
		return sBuf.toString();
	}
}
