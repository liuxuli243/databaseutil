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
	  <legend>${tablename }表的数据(${fn:length(tableData.data)}条数据)</legend>
	</fieldset>
	<div class="layui-form">
		<a href="javascript:history.go(-1);"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="返回"/></a>
		<a href="${pageContext.request.contextPath }/exporttabledata.db?tablename=${tablename }"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="导出Excel"/></a>
	  <table class="layui-table">
	    <!-- <colgroup>
	      <col width="150">
	      <col width="150">
	      <col width="200">
	      <col>
	    </colgroup> -->
	    <thead>
	      <tr>
	      	<c:forEach var="filed" items="${tableData.fileds }">
		        <th>${filed}</th>
	      	</c:forEach>
	      </tr> 
	    </thead>
	    <tbody>
	    	<c:forEach items="${tableData.data }" var="row">
	    		<tr>
			       <c:forEach items="${row }" var="colum">
			       		<td>${colum }</td>
			       </c:forEach>
			    </tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	</div>
</body>
</html>