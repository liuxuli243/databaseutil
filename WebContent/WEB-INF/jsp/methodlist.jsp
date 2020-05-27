<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>方法列表</title>
	<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>方法列表</legend>
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
	        <th width="40%" align="center">方法名</th>
	        <th width="60%" align="center">描述</th>
	      </tr> 
	    </thead>
	    <tbody id="interfacetbody">
	    	<c:forEach items="${methodlist }" var="url">
	    		<tr>
	    			<td align="center">${url.url }</td>
	    			<td align="center">${url.description }</td>
	    		</tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	</div>
</body>
</html>