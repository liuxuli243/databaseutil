<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
</head>
<body>
	<div class="layui-form-item layui-form-text">
		<a href="javascript:history.go(-1);"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="返回"/></a>
	</div>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend>函数${procedurename }的脚本：</legend>
	</fieldset>
	<div class="layui-form-item layui-form-text">
      
      <textarea  class="layui-textarea" rows="30" cols="6" disabled="disabled">${script }</textarea>
	 </div>
</body>
</html>