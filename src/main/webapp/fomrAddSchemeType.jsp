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
	<form id="toSchemeTypeList" method="post" name="toSchemeTypeList" action="toSchemeTypeList" />
	<div class="place">
	    <span>位置：</span>
	    <ul class="placeul">
		    <li><a href="#">首页</a></li>
		    <li><a href="#">表单</a></li>
	    </ul>
    </div>
    <div class="formbody">
	    <div class="formtitle"><span>基本信息</span></div>
	    <ul class="forminfo">
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
		    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认保存" onclick="AddProduct()"/></li>
	    </ul>
    </div>
</body>
<script>
	var regS = new RegExp("&quot;","gi"); 
	var mess = '';
	mess="<s:property value='%{retJson}'/>";
	mess = mess.replace(regS,"\"");
	try{
		var obj = JSON.parse(mess);
		if( true == obj.MANAGER_RESULT ) {
			alert("操作成功");
			document.forms["toProductList"].submit();
		}else{
			alert("操作失败");
		}
	}catch(e) {
	}
</script> 
<script>
	/**
		执行添加业务类型信息方法
	*/
	function AddProduct() {
		var title = encodeURI(encodeURI(document.getElementById("title").value));
		var content = encodeURI(encodeURI(document.getElementById("content").value));
		var is_show = getRadioValue();
		$.ajax({  
            url :"schemeTypeAdd",//后台处理程序
            type:"post",    	//数据发送方式  
            async:false,  
            data:"schemeType.title="+title+
            		"&schemeType.content="+content+
            		"&schemeType.is_show="+is_show+"",
            error: function(){  
            	alert("服务器没有返回数据，可能服务器忙，请重试");  
           },  
            success: function(data){
           	 var retDate = eval("("+data+")");
           	 if( true == retDate.MANAGER_RESULT ) {
           		 //执行成功,跳转到UserList页面
           		 alert("添加业务类型成功");
           		 document.getElementById("toSchemeTypeList").submit();
           	 }else{
           		 //执行失败，alert错误信息
           		 alert("添加业务类型信息失败，失败原因：" + retDate.MANAGER_ERROR_MESSAGE );
           	 }
           }	
		});  
		
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
</html>
