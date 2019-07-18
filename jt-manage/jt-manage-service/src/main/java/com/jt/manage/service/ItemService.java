package com.jt.manage.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.mapper.ItemParamItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item>{
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private ItemParamItemMapper itemParamItemMapper;
	
	@PropertyConfig
	private String MANAGE_ITEM_LIST;
	
	@Autowired
	private RedisService redisService;
	
	private ObjectMapper MAPPER = new ObjectMapper();
	
	//分页（当前页，每页记录数），排序
	public EasyUIResult queryItemList(Integer page, Integer rows){
		EasyUIResult result = null;
		
		//2.从缓存中获取信息，当翻页时，每个页进行缓存
		String jsonData = redisService.get(MANAGE_ITEM_LIST+page);
		if(StringUtils.isNoneEmpty(jsonData)){
			try {
				result = MAPPER.readValue(jsonData, EasyUIResult.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			PageHelper.startPage(page, rows);	//将分页参数传入
			List<Item> itemList = itemMapper.queryItemList();	
			PageInfo pageInfo = new PageInfo(itemList);		//内部拦截器拦截拼接分页条件
			
			result = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
			
			//1.写缓存，过期时间1天
			try {
				redisService.set(MANAGE_ITEM_LIST+page, MAPPER.writeValueAsString(result), 60*60*24);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return result;	//只返回当前页
	}
	
	//商品保存
	public void saveItem(Item item, String desc, String paramData){
		//item对象保存时，mysql提供SQL可以拿到这个新增id值，mybatis就调用这个方法，回传给保存的对象
		itemMapper.insertSelective(item);	
		
		//保存描述信息
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());	//可以拿
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		
		itemDescMapper.insertSelective(itemDesc);
		
		//保存规格信息
		ItemParamItem itemParamItem = new ItemParamItem();
		itemParamItem.setItemId(item.getId());
		itemParamItem.setParamData(paramData);
		itemParamItem.setCreated(itemDesc.getCreated());
		itemParamItem.setUpdated(itemParamItem.getCreated());
		
		itemParamItemMapper.insertSelective(itemParamItem);
		
		//更新缓存
		this.updateRedis();
	}
	
	//商品修改
	public void updateItem(Item item, String desc){
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		
		//更新缓存
		this.updateRedis();
	}
	
	//更新缓存，删除第一页数据
	private void updateRedis(){
		redisService.del(MANAGE_ITEM_LIST+"1");
	}
}
