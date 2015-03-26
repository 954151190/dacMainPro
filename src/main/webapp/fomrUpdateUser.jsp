<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="utf-8" />
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script>
	/**
		执行添加用户信息方法
	*/
	function AddUser() {
		var name = encodeURI(encodeURI(document.getElementById("name").value));
		var user_name = document.getElementById("user_name").value;
		var user_password = document.getElementById("user_password").value;
		var repeat_user_password = document.getElementById("repeat_user_password").value;
		var age = document.getElementById("age").value;
		var user_role =	 encodeURI(encodeURI(document.getElementById("user_role").value));
		var remark = encodeURI(encodeURI(document.getElementById("remark").value));
		var state =document.getElementById("state").value;
		
		if( null == user_password || user_password.length == 0 ) {
			alert( "登录密码为空，请重新填写。" );
			return;
		}
		if( user_password != repeat_user_password ) {
			alert( "重复登录密码与登录密码不一致，请重新填写。" );
			return;
		}
		$.ajax({  
             url :"userUpdate?"+
            		 "user.name="+name+""+
            		 "&user.user_name="+user_name+""+
            		 "&user.user_password="+user_password+""+
            		 "&user.age="+age+""+
            		 "&user.user_role="+user_role+""+
            		 "&user.remark="+remark+""+
            		 "&user.state="+state+"",  		//后台处理程序  
             type:"post",    	//数据发送方式  
             async:false,  
             data:"user.name:"+name+"",
             error: function(){  
             	alert("服务器没有返回数据，可能服务器忙，请重试");  
            },  
             success: function(data){
            	 var retDate = eval("("+data+")");
            	 if( true == retDate.MANAGER_RESULT ) {
            		 //执行成功,跳转到UserList页面
            		 alert("更新用户成功");
            		 document.getElementById("toUserList").submit();
            	 }else{
            		 //执行失败，alert错误信息
            		 alert("更新用户信息失败，失败原因：" + retDate.MANAGER_ERROR_MESSAGE );
            	 }
            }	
		});  
	}
</script>
</head>

<body>
	<form id="toUserList" name="toUserList" action="toUserList" />
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
    <li><label>登录名</label><input name="name" id="name" type="text" class="dfinput" value="${ user.name }" ></input> <i>标题不能超过30个字符</i></li>
    <li><label>登录账户</label><input name="user_name" id="user_name" type="text" class="dfinput" value="${ user.user_name }" ></input> <i>标题不能超过30个字符</i></li>
    <li><label>登录密码</label><input name="user_password" id="user_password" type="text" class="dfinput" value="${ user.user_password }" ></input> <i>多个关键字用,隔开</i></li>
    <li><label>重复输入密码</label><input name="repeat_user_password" id="repeat_user_password" type="text" class="dfinput" value="" ></input> <i>多个关键字用,隔开</i></li>
    <li><label>年龄</label><input name="age" id="age" type="text" class="dfinput" value="${ user.age }" ></input> <i>标题不能超过30个字符</i></li>
    <li><label>角色</label><input name="user_role" id="user_role" type="text" class="dfinput" value="${ user.user_role }" ></input> <i>标题不能超过30个字符</i></li>
    <li><label>备注</label><input name="remark" id="remark" type="text" class="dfinput" value="${ user.remark }" ></input> <i>标题不能超过30个字符</i></li>
    <li><label>状态</label><cite><input name="state" id="state" type="radio" value="" checked="checked" />开启&nbsp;&nbsp;&nbsp;&nbsp;<input name="state" type="radio" value="" />关闭</cite></li>
    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认更新" onclick="AddUser()"/></li>
    </ul>
    </div>
</body>

</html>
