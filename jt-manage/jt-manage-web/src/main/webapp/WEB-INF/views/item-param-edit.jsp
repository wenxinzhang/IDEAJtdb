<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form id="itemParamEditForm" method="post">
		
<table cellpadding="5" style="margin-left: 30px" border="0" id="itemParamEditTable" class="itemParam">
	<input type="text" name="id" value="${id}"/>
	<tr>
		<td>商品类目:</td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">选择类目</a> 
		</td>
	</tr>
	<tr class="addGroupTr">
		<td>规格参数:</td>
		<td id="groupTd">
			<ul>
				<li><a href="javascript:void(0)" class="easyui-linkbutton addGroup">添加分组</a></li>
			</ul>
			${tds}
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<a href="javascript:void(0)" class="easyui-linkbutton submit">提交</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton close">关闭</a>
		</td>
	</tr>
</table>
<div  class="itemParamAddTemplate" style="display: none;">
	<li class="param">
		<ul>
			<li>
				<input class="easyui-textbox" style="width: 150px;" name="group"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton addParam"  title="添加参数" data-options="plain:true,iconCls:'icon-add'"></a>
			</li>
			<li>
				<span>|-------</span><input  style="width: 150px;" class="easyui-textbox" name="param"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton delParam" title="删除" data-options="plain:true,iconCls:'icon-cancel'"></a>						
			</li>
		</ul>
	</li>
</div>


<script style="text/javascript">

	$(function(){
		//给回显的元素绑定事件
		var tds = $(".param");
		tds.find(".addParam").click(function(){
			  var li = $(".itemParamAddTemplate li").eq(2).clone();
			  li.find(".delParam").click(function(){
				  $(this).parent().remove();
			  });
			  li.appendTo($(this).parentsUntil("ul").parent());
		});
		
		tds.find(".delParam").click(function(){
			  $(this).parent().remove();
		});
		
		
		KindEditorUtil.initItemCat({
			fun:function(node){
			$(".addGroupTr").hide().find(".param").remove();
				//  判断选择的目录是否已经添加过规格
			  $.getJSON("/item/param/query/itemcatid/" + node.id,function(data){
				  if(data.status == 200 && data.data){
					  $.messager.alert("提示", "该类目已经添加，请选择其他类目。", undefined, function(){
						 $("#itemParamAddTable .selectItemCat").click();
					  });
					  return ;
				  }
				  //当前选择的商品分类没有规格参数
				  $(".addGroupTr").show();
			  });
			}
		});
		
		$(".addGroup").click(function(){
			  var temple = $(".itemParamAddTemplate li").eq(0).clone();	//复制模板那段代码
			  $(this).parent().parent().append(temple);
			  temple.find(".addParam").click(function(){
				  var li = $(".itemParamAddTemplate li").eq(2).clone();
				  li.find(".delParam").click(function(){
					  $(this).parent().remove();
				  });
				  li.appendTo($(this).parentsUntil("ul").parent());
			  });
			  temple.find(".delParam").click(function(){
				  $(this).parent().remove();
			  });
		 });
		
		$("#itemParamEditTable .close").click(function(){
			$(".panel-tool-close").click();
		});
		
		$("#itemParamEditTable .submit").click(function(){
			var params = [];
			var groups = $("#itemParamEditTable [name=group]");
			groups.each(function(i,e){
				var p = $(e).parentsUntil("ul").parent().find("[name=param]");
				var _ps = [];
				p.each(function(_i,_e){
					var _val = $(_e).siblings("input").val();
					if($.trim(_val).length>0){
						_ps.push(_val);						
					}
				});
				var _val = $(e).siblings("input").val();
				if($.trim(_val).length>0 && _ps.length > 0){
					params.push({
						"group":_val,
						"params":_ps
					});					
				}
			});
			
			//以RESTFul形式提交，将当前选择的商品分类的id值传递到后台
			var url = "/item/param/edit/"+$("#itemParamEditForm [name=id]").val();
			//将javascript对象转换为一个字符串，json串[{},{}]
			$.post(url,{"paramData":JSON.stringify(params)},function(data){
				if(data.status == 200){
					$.messager.alert('提示','修改商品规格成功!',undefined,function(){
						$(".panel-tool-close").click();
    					$("#itemParamList").datagrid("reload");
    				});
				}
			});
		});
	});
</script>

</form>