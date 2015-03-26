<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body bgcolor="#d9dfaa">
	<hr width="700" align="left">
	<h3>插入照片</h3>

	<s:form action="addPhoto" method="post" enctype="multipart/form-data">
		<table border="0" cellpadding="1" cellspacing="0">
			<tr>
				<td><s:textfield name="photo.id" label="照片ID" value=""></s:textfield>
				</td>
			</tr>
			<tr>
				<td><s:file name="zpfile" label="照片" value=""></s:file></td>
			</tr>
		</table>
		<p>
			<input type="submit" value="添加"> <input type="reset"
				value="重置">
	</s:form>

	<hr width="700" align="left">
</body>
</html>
