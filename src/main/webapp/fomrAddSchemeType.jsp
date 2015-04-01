<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="utf-8" />
<title>添加业务类型页</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<style> 
.textbox {
	 background: #BFCEDC;
	 border-top: #7F9DB9 1px solid;
	 border-left: #7F9DB9 1px solid; 
	border-right: #7F9DB9 1px solid; 
	border-bottom: #7F9DB9 1px solid; 
	font-family: "宋体", "Verdana", "Arial", "Helvetica"; 
	font-sizE: 220px; 
	text-align: LIFT;
	} 
</style>
</head>
<body>
	<div class="place">
	    <span>位置：</span>
	    <ul class="placeul">
		    <li><a href="#">首页</a></li>
		    <li><a href="#">新增业务类型</a></li>
	    </ul>
    </div>
    <div class="formbody">
	    <div class="formtitle"><span>基本信息</span></div>
	    <ul class="forminfo">
		    <form action="schemeTypeAdd" method="post" id="schemeTypeAdd"  name="schemeTypeAdd">
			    <li><label>业务类型名称</label><input name="title" id="title" type="text" class="dfinput" value="业务类型名称" ></input> <i>标题不能超过30个字符</i></li>
			    <li><label>是否在首页展示</label>
			    	<cite>
				    	<input name="is_show" id="is_show" type="radio" value="1" checked="checked" />
				    	&nbsp;展示&nbsp;&nbsp;&nbsp;&nbsp;
				    	<input name="is_show" id="is_show" type="radio" value="0" />
				    	不展示
			    	</cite>
			    </li>
			    <li><label>业务类型描述</label><input name="content" id="content" type="text" class="dfinput" value="业务类型描述" ></li>
			    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认保存" onclick="AddProduct()"/></li>
			    <li>
			    	<input type="hidden" id="schemeType.title" name="schemeType.title"/>
			    	<input type="hidden" id="schemeType.content" name="schemeType.content"/>
			    	<input type="hidden" id="schemeType.is_show" name="schemeType.is_show" />
			    </li>
		    </form>
	    </ul>
    </div>
    <div>
    	<form id="toSchemeTypeList" method="post" name="toSchemeTypeList" action="toSchemeTypeList?page.number=1&page.count=10" />
    </div>
</body>
<script>
	/**
		执行添加业务类型信息方法
	*/
	function AddProduct() {
		var title = encodeURI(document.getElementById("title").value);
		var content = encodeURI(document.getElementById("content").value);
		var is_show = getRadioValue();
		document.getElementById("schemeType.title").value = title;
		document.getElementById("schemeType.content").value = content;
		document.getElementById("schemeType.is_show").value = is_show;
		return true;
	}
	
	function getRadioValue() {
		var chkObjs = document.getElementsByName("is_show");
		for(var i=0;i<chkObjs.length;i++){
			if(chkObjs[i].checked){
				return chkObjs[i].value;
			}
		}
	}
	
</script>
<script>
	var regS = new RegExp("&quot;","gi"); 
	var mess = '';
	mess="<s:property value='%{retJson}'/>";
	mess = mess.replace(regS,"\"");
	try{
		var obj = JSON.parse(mess);
		if( true == obj.MANAGER_RESULT ) {
			alert("操作成功");
			document.forms["toSchemeTypeList"].submit();
		}else{
			alert("操作失败");
		}
	}catch(e) {
	}
</script> 
</html>
