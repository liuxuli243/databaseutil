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
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>所有的数据库表和视图</legend>
</fieldset>
	<div class="layui-form">
	  <table class="layui-table">
	    <!-- <colgroup>
	      <col width="150">
	      <col width="150">
	      <col width="200">
	      <col>
	    </colgroup> -->
	    <thead>
	      <tr>
	        <th>表名</th>
	        <th>类型</th>
	        <th>备注</th>
	        <th align="center">操作</th>
	      </tr> 
	    </thead>
	    <tbody>
	    	<c:forEach items="${alltable }" var="table">
	    		<tr>
			       <td width="40%">${table.table_name }</td>
			       <td width="10%" align="center">${table.table_type eq 'TABLE'?'数据库表':'视图' }</td>
			       <td width="35%">${table.remark }</td>
			       <td align="center">
			       		<a href="${pageContext.request.contextPath }/tablefileds.db?tablename=${table.table_name}">表结构</a>&nbsp;&nbsp;&nbsp;&nbsp;
			       		<a href="${pageContext.request.contextPath }/tabledata.db?tablename=${table.table_name}">查看数据</a>
			       </td>
			    </tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	</div>
</body>
</html>