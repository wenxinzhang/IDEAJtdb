package com.jt.manage.mapper;

import java.util.Map;

import com.jt.manage.mapper.base.mapper.SysMapper;
import com.jt.manage.pojo.ItemParam;

public interface ItemParamMapper extends SysMapper<ItemParam>{
	public Integer queryItemParamItemCount(Map map);
}
