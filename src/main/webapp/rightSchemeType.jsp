<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务类型管理</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>

<script type="text/javascript">
$(document).ready(function(){
  $(".click").click(function(){
  $(".tip").fadeIn(200);
  });
  
  $(".tiptop a").click(function(){
  $(".tip").fadeOut(200);
});

  $(".sure").click(function(){
  $(".tip").fadeOut(100);
});

  $(".cancel").click(function(){
  $(".tip").fadeOut(100);
});

});

/**
 * 添加业务类型信息方法
 */
function addSchemesType() {
	document.getElementById("formSchemeTypeAdd").submit();
}

function updateSchemesType(data) {
	var user_id = document.getElementById("form_update_scheme_type_id");
	user_id.value = data;
	document.getElementById("formSchemeTypeUpdate").submit();
}

function deleteSchemesType( data , state) {
	$.ajax({  
        url :"schemeTypeDelete?"+
        	 "schemeType.state="+state+        	 
       		 "&schemeType.id="+data+"",//后台处理程序
        type:"post",    	//数据发送方式  
        async:false,  
        data:"",
        error: function(){  
        	alert("服务器没有返回数据，可能服务器忙，请重试");  
       },  
        success: function(data){
       	 var retDate = eval("("+data+")");
       	 if( true == retDate.MANAGER_RESULT ) {
       		 //执行成功,跳转到UserList页面
       		 alert("删除业务成功");
       		 //当前页面刷新
       		location.reload();  
       	 }else{
       		 //执行失败，alert错误信息
       		 alert("删除业务信息失败，失败原因：" + retDate.MANAGER_ERROR_MESSAGE );
       	 }
       }	
	}); 
}
</script>


</head>


<body>
	<form id="formSchemeTypeUpdate" name="formSchemeTypeUpdate" action="toSchemeTypeUpdate" >
		<input type="hidden" id="form_update_scheme_type_id" name="schemeType.id"/>
	</form>
	<form id="formSchemeTypeAdd" name="formSchemeTypeAdd" action="toSchemeTypeAdd" />
	
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">管理信息</a></li>
    <li><a href="#">业务类型管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    
    <div class="tools">
    
    	<ul class="toolbar">
        <li ><span><a href="javascript:addSchemesType();"><img src="images/t01.png" /></span>添加</a></li>
        </ul>
    </div>
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th><input name="" type="checkbox" value="" checked="checked"/></th>
        <th>编号<i class="sort"><img src="images/px.gif" /></i></th>
        <th>标题</th>
        <th>是否首页展示</th>
        <th>作者</th>
        <th>发布时间</th>
        <th>状态</th>
        <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="schemeTypeList" var="schemeTypes">
            <tr>
	        <td><input name="" type="checkbox" value="" /></td>
	        <td>${ schemeTypes.id }</td>
	        <td>${ schemeTypes.title }</td>
	        <td>
	        	<s:if test="#schemeTypes.is_show==0">
	        		不展示
	        	</s:if>
	        	<s:else>
	        		展示
	        	</s:else>
	        </td>
	        <td>${ schemeTypes.author_name }</td>
	        <td>${ schemeTypes.create_time }</td>
	        <td>
	        	<s:if test="#schemeTypes.state==0">
	        		未审核
	        	</s:if>
	        	<s:else>
	        		已审核
	        	</s:else>
	        </td>
	        <td><a href="javascript:updateSchemesType('${ schemeTypes.id }')" class="tablelink" >更新</a>
	        	<s:if test="#schemeTypes.state==0">
	        		<a href="javascript:deleteSchemesType('${ schemeTypes.id }','${ schemeTypes.state }')" class="tablelink"> 审核</a></td>
	        	</s:if>
	        	<s:else>
	        		<a href="javascript:deleteSchemesType('${ schemeTypes.id }','${ schemeTypes.state }')" class="tablelink"> 作废</a></td>
	        	</s:else>
	        </tr> 
    	</s:iterator> 
        </tbody>
    </table>
   
    <div class="pagin">
    	<div class="message">共<i class="blue">${ page.allCount }</i>条记录，当前显示第&nbsp;${ page.number }&nbsp;页，总&nbsp;${ page.allPage }&nbsp;页</div>
        <ul class="paginList">
        <li class="paginItem"><a href="javascript:goLastPage()" style="width: 41px">首页</a></li>
        <li class="paginItem"><a href="javascript:pageLast();" style="width: 41px">上一页</a></li>
        <li class="paginItem current"><a href="javascript:;"  style="width: 41px">${page.number }</a></li>
        <li class="paginItem"><a href="javascript:pageNext();" style="width: 41px">下一页</a></li>
        <li class="paginItem"><a href="javascript:goNextPage();" style="width: 41px">末页</a></li>
        <li class="paginItem">
        	<input type="text" id="pageNumber" value="${ page.number }" style="width: 31px;height: 25px;border: 2px solid #000000; margin-left: 2px;  margin-right: 2px;"/>
        </li>
        <li class="paginItem"><a href="javascript:pageGo();">跳转</a></li>
        </ul>
    </div>
    
    
    <div class="tip">
    	<div class="tiptop"><span>提示信息</span><a></a></div>
        
      <div class="tipinfo">
        <span><img src="images/ticon.png" /></span>
        <div class="tipright">
        <p>是否确认对信息的修改 ？</p>
        <cite>如果是请点击确定按钮 ，否则请点取消。</cite>
        </div>
        </div>
        
        <div class="tipbtn">
        <input name="" type="button"  class="sure" value="确定" />&nbsp;
        <input name="" type="button"  class="cancel" value="取消" />
        </div>
    
    </div>
    
    
    
    
    </div>
    
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>
<script>
	var number = "<s:property value='%{page.number}'/>";
	var count = "<s:property value='%{page.count}'/>";
	var allPage = "<s:property value='%{page.allPage}'/>";
	
	function pageGo() {
		var goNumber = document.getElementById("pageNumber").value;
		goNumber = parseInt(goNumber);
		if( isNaN(goNumber) ||  goNumber <= 0 || goNumber > allPage ) {
			alert("请输入有效页码")
		}else{
			window.location.href="toSchemeTypeList?page.number="+goNumber+"&page.count="+count+"";			
		}
	}

	function pageLast() {
		var goNumber = parseInt( number ) - parseInt(1);
		if( goNumber > 0 ) {
			window.location.href="toSchemeTypeList?page.number="+ goNumber +"&page.count="+count+"";
		}else{
			alert("没有了，已经是第一页了。");
		}
	}
	
	function pageNext() {
		var goNumber = parseInt(number)+parseInt(1);
		if( goNumber <= allPage) {
			window.location.href="toSchemeTypeList?page.number="+ goNumber +"&page.count="+count+"";
		}else{
			alert("没有了，已经是最有一页了。");
		}
	}
	
	function goLastPage() {
		window.location.href="toSchemeTypeList?page.number=1&page.count="+count+"";
	}
	
	function goNextPage() {
		window.location.href="toSchemeTypeList?page.number="+allPage+"&page.count="+count+"";
	}
</script>
</html>
