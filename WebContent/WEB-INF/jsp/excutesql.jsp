<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>执行查询</title>
	<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
</head>
<body>
	<div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">增、删、改SQL语句：</label>
	    <br/>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入SQL语句" class="layui-textarea" id="sql" name="desc" rows="10"></textarea>
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <div class="layui-input-block">
	      <button class="layui-btn" lay-submit="" id="submitbtn" lay-filter="demo1">执行</button>
	    </div>
	  </div>
	  <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	  <script type="text/javascript">
	  	//执行sql
	  	$('#submitbtn').click(function(){
	  		var sql = $('#sql').val();
	  		$.post('${pageContext.request.contextPath }/excutesql.db',{sql:sql},function(data){
	  			if(data == "1"){
	  				alert('执行成功');
	  			}else{
	  				alert('执行失败');
	  			}
			});
	  	});
	  </script>
</body>
</html>