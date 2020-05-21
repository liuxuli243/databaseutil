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
	<input id="tablecount" type="hidden">
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
	  <legend id="datainfo">${tablename }表的数据</legend>
	</fieldset>
	<div class="layui-form">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:history.go(-1);"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="返回"/></a>
		<a  onclick="exportexcel('${tablename}')"><input type="button" class="layui-btn" lay-submit="" lay-filter="demo1" value="导出Excel"/></a>
	  <table id="test" class="layui-table" lay-filter="test">
			
	  </table>
	</div>
	<script src="${pageContext.request.contextPath }/static/layui/dist/layui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		layui.use('table', function(){
		  var table = layui.table;
		  
		  table.render({
			elem: '#test'
			,url:'${pageContext.request.contextPath }/gettabledata.db?tablename=${tablename}'
			,method:'post'
				,cols: [[
			  		<c:forEach var="filed" items="${allfileds }" varStatus="status">
				  		<c:choose>
							<c:when test="${status.index eq 0}">
								{field:'${filed}',  title: '${filed}', sort: false ,align:'center',minWidth:150}
							</c:when>
							<c:otherwise>
								,{field:'${filed}',  title: '${filed}', sort: false ,align:'center',minWidth:150}
							</c:otherwise>
						</c:choose>
		      		</c:forEach>
					]]
			,loading:true
			,even: true
			,page: true
			,limit: 100
			,limits:[100,200,300,400,500],
			done: function(res, curr, count){
				$('#tablecount').val(count);
				var datainfo = $('#datainfo').html();
				console.log(datainfo);
				$('#datainfo').html('${tablename}表的数据('+count+'条)')
			}
		  });
		});
		
		//导出数据
		function exportexcel(tablename){
			var url = '${pageContext.request.contextPath }/exporttabledata.db?tablename=${tablename }';
			var count = $('#tablecount').val();
			if(count >= 1000000){
				layer.alert("数据超过100万不允许导出");
				return;
			}else{
				window.location.href = url;
			}
		}
	</script>
</body>
</html>