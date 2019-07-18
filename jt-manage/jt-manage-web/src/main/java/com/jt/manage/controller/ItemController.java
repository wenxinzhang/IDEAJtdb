package com.jt.manage.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemDescService;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemDescService itemDescService;
	
	//商品新增 	/item/save  返回值要求返回的对象中有一个status属性，属性值为200代表操作成功。
	@RequestMapping("/save")
	@ResponseBody
	public SysResult save(Item item, String desc, String itemParams){
		//补充默认值，时间
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		try{
			itemService.saveItem(item, desc, itemParams);
			return SysResult.build(200, "新增商品保存成功!");
		}catch(Exception e){
			System.out.println(e);
			return SysResult.build(201, "新增商品保存失败!"+e.getMessage());
		}
	}
	
	//列表	/item/query	
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult query(Integer page, Integer rows){
		//List<Item> itemList = itemService.queryAll();
		return itemService.queryItemList(page, rows);
	}
	
	//修改 	/item/update
	@RequestMapping("/update")
	@ResponseBody
	public SysResult update(Item item, String desc){
		item.setUpdated(new Date());
		itemService.updateItem(item, desc);
		return SysResult.ok();
	}
	
	//删除	/item/delete
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult delete(String[] ids){
		itemService.deleteByIds(ids);
		
		return SysResult.ok();
	}
	
	//单独查询商品描述信息	/item/query/item/desc/'+data.id
	@RequestMapping("/query/item/desc/{id}")
	@ResponseBody
	public SysResult desc(@PathVariable Long id){
		ItemDesc itemDesc = itemDescService.queryById(id);
		return SysResult.ok(itemDesc);
	}
}
