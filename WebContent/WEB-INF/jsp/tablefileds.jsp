<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>表格数据</title>
	<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend>${tablename }表的结构</legend>
	</fieldset>
	<div class="layui-form">
		<a href="javascript:history.go(-1);"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="返回"/></a>
	  <table class="layui-table">
	    <!-- <colgroup>
	      <col width="150">
	      <col width="150">
	      <col width="200">
	      <col>
	    </colgroup> -->
	    <thead>
	      <tr>
		        <th>字段名</th>
		        <th>类型</th>
		        <th>长度</th>
		        <th>可以为空</th>
		        <th>主键</th>
		        <th>注释</th>
	      </tr> 
	    </thead>
	    <tbody>
	    	<c:forEach items="${tableFileds }" var="row">
	    		<tr>
		       		<td width="20%">${row.COLUMN_NAME }</td>
		       		<td width="20%">${row.TYPE_NAME }</td>
		       		<td width="15%">${row.COLUMN_SIZE }</td>
		       		<td width="10%">${row.NULLABLE eq 1?'是':'否'}</td>
		       		<td width="10%">${row.PRIMARYKEY}</td>
		       		<td width="25%">${row.REMARKS }</td>
			    </tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	</div>
</body>
</html>