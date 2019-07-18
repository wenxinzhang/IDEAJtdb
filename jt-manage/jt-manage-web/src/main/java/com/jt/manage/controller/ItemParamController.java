package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.ItemParam;
import com.jt.manage.pojo.ItemParamJson;
import com.jt.manage.service.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
	private ObjectMapper MAPPER = new ObjectMapper();	//jackson
	
	@Autowired
	private ItemParamService itemParamService;
	
	//列表	/item/param/list
	@RequestMapping("list")
	@ResponseBody
	public EasyUIResult list(Integer page, Integer rows){
		return itemParamService.queryItemParamList(page, rows);
	}
	
	//是否当前选择的商品分类已经有规格参数	/item/param/query/itemcatid/" + node.id
	@RequestMapping("query/itemcatid/{itemCatId}")
	@ResponseBody
	public SysResult query(@PathVariable Long itemCatId){
		return itemParamService.queryItemCat(itemCatId);
	}
	
	//新增保存	/item/param/save/"+$("#itemParamAddTable [name=cid]
	@RequestMapping("save/{cid}")
	@ResponseBody
	public SysResult save(@PathVariable Long cid, String paramData){
		return itemParamService.saveItemParam(cid, paramData);
	}
	
	//修改页面
	@RequestMapping("toEdit")
	public String toEdit(Long itemParamId, Model model){
		//准备数据
		ItemParam itemParam = itemParamService.queryById(itemParamId);
		String tds = this.getItemParamTD(itemParam.getParamData());
		model.addAttribute("tds", tds);
		model.addAttribute("id", itemParamId);
		
		return "item-param-edit";
	}
	
	//修改保存
	@RequestMapping("edit/{id}")
	@ResponseBody
	public SysResult edit(@PathVariable Long id, String paramData){
		return itemParamService.editItemParam(id, paramData);
	}
	
	private String getItemParamTD(String paramData){
		JsonNode jsonNode = null;
		try {
			//将json串转换为java对象
			jsonNode = MAPPER.readTree(paramData);
			List<ItemParamJson> list = null;
			if (jsonNode.isArray() && jsonNode.size() > 0) {
				list = MAPPER.readValue(jsonNode.traverse(),
						MAPPER.getTypeFactory().constructCollectionType(List.class, ItemParamJson.class));
				
			}
			
			StringBuilder sb = new StringBuilder();
			for(ItemParamJson ipj : list){
				String groupName = ipj.getGroup();
				List<String> params = ipj.getParams();
				
				sb.append("	<ul>");
				sb.append("		<li>");
				sb.append("		<input class=\"easyui-textbox\" style=\"width: 150px;\" name=\"group\" value=\"").append(groupName).append("\"/>&nbsp;<a href=\"javascript:void(0)\" class=\"easyui-linkbutton addParam\"  title=\"添加参数\" data-options=\"plain:true,iconCls:'icon-add'\"></a>");
				sb.append("		</li>");
				
				for(String paramValue : params){
					sb.append("		<li>");
					sb.append("		<span>|-------</span><input  style=\"width: 150px;\" class=\"easyui-textbox\" name=\"param\" value=\"").append(paramValue).append("\"/>&nbsp;<a href=\"javascript:void(0)\" class=\"easyui-linkbutton delParam\" title=\"删除\" data-options=\"plain:true,iconCls:'icon-cancel'\"></a>");
					sb.append("		</li>");
				}
				
				sb.append("		</ul>");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//删除	/item/param/delete
	@RequestMapping("delete")
	@ResponseBody
	public SysResult delete(Long[] ids){
		return itemParamService.delete(ids);
	}
}
