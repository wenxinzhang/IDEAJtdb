package com.jt.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.mapper.ItemParamMapper;
import com.jt.manage.pojo.ItemParam;

@Service
public class ItemParamService extends BaseService<ItemParam>{
	@Autowired
	private ItemParamMapper itemParamMapper;
	@Autowired
	private ItemCatMapper itemCatMapper;

	public EasyUIResult queryItemParamList(Integer page, Integer rows) {
		//分页
		PageHelper.startPage(page, rows);
		List<ItemParam> itemParamList = itemParamMapper.select(null);
		PageInfo pageInfo = new PageInfo(itemParamList);
		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());		
	}

	public SysResult queryItemCat(Long itemCatId) {
		ItemParam param = new ItemParam();
		param.setItemCatId(itemCatId);
		
		List<ItemParam> itemParamList = itemParamMapper.select(param);
		if(itemParamList!=null && itemParamList.size()>0){
			//业务约定，一个分类下只能设置一个规格参数
			return SysResult.ok(itemParamList.get(0));	
		}else{
			return SysResult.ok(null);
		}
	}

	public SysResult saveItemParam(Long cid, String paramData) {
		ItemParam itemParam = new ItemParam();
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(itemParam.getCreated());
		
		itemParamMapper.insert(itemParam);
		return SysResult.ok();
	}

	public SysResult editItemParam(Long id, String paramData) {
		ItemParam itemParam = new ItemParam();
		itemParam.setId(id);
		itemParam.setParamData(paramData);
		
		itemParamMapper.updateByPrimaryKeySelective(itemParam);
		return SysResult.ok();
	}
	
	public SysResult delete(Long[] ids){
		//加一个判断，在商品的规格表中有无对此几个id中的进行引用，如果引用提示用户已经有引用，不能删除，如果没有，直接删除
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ids", ids);
		
		Integer countNum = itemParamMapper.queryItemParamItemCount(map);
		if(countNum!=null && countNum>0){
			return SysResult.build(201, ids.toString()+"这些规格参数中至少有一个规格参数被引用!");
		}else{
			itemParamMapper.deleteByIDS(ids);
			return SysResult.ok();
		}
	}

}
