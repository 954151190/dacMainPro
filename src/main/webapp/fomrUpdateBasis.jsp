<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="utf-8" />
<title>维护基础信息页面</title>
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
		    <li><a href="#">基本信息</a></li>
	    </ul>
    </div>
    <div class="formbody">
	    <div class="formtitle"><span>基本信息</span></div>
		    <ul class="forminfo">
		    	<table>
			    	<form id="basisUpdate" method="post" name="basisUpdate" action="basisUpdate"/>
					    <tr><td><li><label>联系方式:</label><input name="basis.phone" id="basis.phone" type="text" class="dfinput" value="${ basis.phone }"></input></li></td></tr>
					    <tr><td><li><label>办公地点:</label><input name="basis.address" id="basis.address" type="text" class="dfinput" value="${ basis.address }"></input></li></td></tr>
					    <tr><td><li><label>Q&nbsp;Q号码:</label><input name="basis.qq" id="basis.qq" type="text" class="dfinput" value="${ basis.qq }"></input></li></td></tr>
					    <tr><td><li><label>微信平台:</label><input name="basis.wx" id="basis.wx" type="text" class="dfinput" value="${ basis.wx }" /></li></td></tr>
					    <tr td align="center" ><td align="center"><li><input type="submit" class="btn" value="提交"></li></td></tr>
			    	</form>
		    	</table>
		    </ul>
	    </div>
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
			document.forms["toPhotoList"].submit();
		}else{
			alert("操作失败");
		}
	}catch(e) {
	}
</script>  
</html>