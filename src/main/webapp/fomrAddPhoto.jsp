<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="utf-8" />
<title>添加图片</title>
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
<script>
	/**
		执行添加图片信息方法
	*/
	function AddPhoto() {
		document.getElementById("photoAdd").submit();
	}
</script>
</head>

<body>
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
		    	<form id="photoAdd" method="post" name="photoAdd" action="photoAdd" enctype="multipart/form-data"/>
				    <li><label>图片标题</label><input name="photo.title" id="photo.title" type="text" class="dfinput" value="图片标题" ></input> <i>标题不能超过30个字符</i></li>
				    <li>
				    	<label>文章内容</label>
				    	<input type="file" id="photoFile" name="photoFile" style="width:450">
				    	<br/>
				    	<font color="red">支持JPG,JPEG,GIF,BMP,SWF,RMVB,RM,AVI文件的上传</font>
				    	<br/>
				    	<input type="submit" class="btn" value="上传文件"><span id="msg"></span>
				    	<br/>
				    </li>
		    	</form>
		    </ul>
	    </div>
	</div>
	<div>
    	<form id="toPhotoList" method="post" name="toPhotoList" action="toPhotoList?page.number=1&page.count=10" />
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