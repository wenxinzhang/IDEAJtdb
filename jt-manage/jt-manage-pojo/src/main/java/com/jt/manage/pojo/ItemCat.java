package com.jt.manage.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="tb_item_cat")	//声明当前的POJO对象对应的数据库表tb_item_cat
public class ItemCat extends BasePojo{
	@Id		//设置主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)	//设置自增，底层数据库支持
	private Long id;
	
	@Column(name="parent_id")	//实体的属性名和表的字段名不一致时，需要这个注解做映射
	private Long parentId;		//父节点
	private String name;		//名称
	private Integer status;		//状态，默认1，可选值：1正常，2删除
	
	@Column(name="sort_order")
	private Integer sortOrder;	//排序号
	
	@Column(name="is_parent")
	private Boolean isParent;	//是否是父节点
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
	// 扩展get方法，满足EasyUI的tree格式
    public String getText() {
        return getName();
    }

    public String getState() {
        return getIsParent() ? "closed" : "open";
    }

}
