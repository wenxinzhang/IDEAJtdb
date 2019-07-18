package com.jt.manage.mapper;

import java.util.List;

import com.jt.manage.mapper.base.mapper.SysMapper;
import com.jt.manage.pojo.ItemCat;

public interface ItemCatMapper extends SysMapper<ItemCat>{
	//扩展方法
	public List<ItemCat> queryItemCatListByParentId(Integer pid);
}
