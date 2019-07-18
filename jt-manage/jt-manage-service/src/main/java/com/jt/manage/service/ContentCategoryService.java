package com.jt.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jt.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory>{
	/*
	 * 节点删除
	 * 1、要删除所有的子节点，多级请求
	 * 2、删除自身
	 * 3、当没有兄弟节点时，设置其父isParent为0，它的父亲节点就成为叶子节点
	 */
	
	public void deleteCase(Long parentId, Long id){
		//1.要删除所有的子节点，多级请求。
		/*
		 * 递归
		 * 条件：必须有同样参数，必须有判断条件退出
		 */
		List<Long> idList = new ArrayList<Long>();
		selectNode(idList, id);
		
		//2.删除
		if(idList!=null && idList.size()>0){
			this.deleteByIds(idList.toArray());
		}
		
		//3.查询是否有兄弟节点
		ContentCategory param2 =  new ContentCategory();
		param2.setParent_id(parentId);
		List<ContentCategory> brotherList = super.queryListByWhere(param2);
		if(brotherList!=null && brotherList.size()>0){
			//有兄弟节点存在
		}else{
			//没有兄弟，父节点下已经没有节点
			ContentCategory parentCC = super.queryById(parentId);
			parentCC.setIsParent(false);
			super.save(parentCC);
		}
		
	}
	
	private void selectNode(List<Long> idList, Long pid){
		idList.add(pid);
		
		//查询当前节点下的子节点
		ContentCategory param = new ContentCategory();
		param.setParent_id(pid);
		
		List<ContentCategory> ccList = super.queryListByWhere(param);
		
		if(ccList!=null && ccList.size()>0){
			for(ContentCategory cc : ccList){
				selectNode(idList, cc.getId());
			}
		}
	}
}
