package com.jt.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat>{
	//注入接口
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	@Autowired
	private RedisService redisService;
	private ObjectMapper MAPPER = new ObjectMapper();	//jackson
	
	@PropertyConfig
	private String ITEM_CAT_WEB_URL;
	
	//查询某个分类的列表
	public List<ItemCat> queryItemCatList(Integer parentId){
		return itemCatMapper.queryItemCatListByParentId(parentId);
	}
	
	//前台系统需要json串，分三级目录，每层目录都有自己的结构
	public ItemCatResult queryWebAll() {
		ItemCatResult result = new ItemCatResult();
		//第二次访问从缓存中获取
		String jsonData = redisService.get(ITEM_CAT_WEB_URL);
		if(StringUtils.isNotEmpty(jsonData)){
			//将json串转换为java对象
			try {
				result = MAPPER.readValue(jsonData, ItemCatResult.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			ItemCat param = new ItemCat();
			param.setStatus(1);		//状态为1正常
			List<ItemCat> cats =  itemCatMapper.select(param);
			
			 // 转为map存储，key为父节点ID，value为数据集合
	        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
	        for (ItemCat itemCat : cats) {
	            if (!itemCatMap.containsKey(itemCat.getParentId())) {
	            	//在map中创建一个记录，记录key，当前分类所属的父分类id，value空集合
	                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
	            }
	            itemCatMap.get(itemCat.getParentId()).add(itemCat);
	        }
	
	        // 封装一级对象
	        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
	        for (ItemCat itemCat : itemCatList1) {
	            ItemCatData itemCatData = new ItemCatData();
	            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
	            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
	            result.getItemCats().add(itemCatData);
	            if (!itemCat.getIsParent()) {
	                continue;
	            }
	
	            // 封装二级对象
	            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
	            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
	            itemCatData.setItems(itemCatData2);
	            for (ItemCat itemCat2 : itemCatList2) {
	                ItemCatData id2 = new ItemCatData();
	                id2.setName(itemCat2.getName());
	                id2.setUrl("/products/" + itemCat2.getId() + ".html");
	                itemCatData2.add(id2);		//构造子集合
	                if (itemCat2.getIsParent()) {
	                    // 封装三级对象
	                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
	                    List<String> itemCatData3 = new ArrayList<String>();
	                    id2.setItems(itemCatData3);
	                    for (ItemCat itemCat3 : itemCatList3) {
	                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
	                    }
	                }
	            }
	            //主分类太多，限制只展示前14个
	            if (result.getItemCats().size() >= 14) {
	                break;
	            }
	        }
	        
	        //第一次访问后，将数据写缓存，这个key在整个系统中唯一
			//将java对象转换成json串
			try {	//缓存的异常不能抛出，必须自己处理，一般记录日志
				//设置过期时间，60*60*24*7=7天
				redisService.set(ITEM_CAT_WEB_URL, MAPPER.writeValueAsString(result),60*60*24*7);
			} catch (JsonProcessingException e) {
				//记录日志
				e.printStackTrace();
			}
		}
		
        return result;
	}
}
