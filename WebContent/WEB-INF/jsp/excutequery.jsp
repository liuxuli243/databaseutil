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
	    <label class="layui-form-label">查询的SQL语句：</label>
	    <br/>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入SQL语句" class="layui-textarea" id="sql" name="desc" rows="10" cols="10"></textarea>
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <div class="layui-input-block">
	      <button class="layui-btn" lay-submit="" id="submitbtn" lay-filter="demo1">执行</button>
	      <button class="layui-btn" lay-submit="" id="exportexcel" lay-filter="demo1">导出Excel</button>
	    </div>
	  </div>
	  <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
		  <legend id="queryresult"></legend>
		</fieldset>
	  <table class="layui-table" id="LAY_table_user" lay-filter="user">
	  		<thead id="thead">
		    </thead>
		    <tbody id="tbody">
		    </tbody>
	  </table> 
	  <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath }/static/layui/dist/layui.js" charset="utf-8"></script>
	  <script type="text/javascript">
	  	layui.use('layer', function(){
		  var layer = layui.layer;
		  
		});
	  	//执行sql
	  	$('#submitbtn').click(function(){
	  		var sql = $('#sql').val();
	  		$.post('${pageContext.request.contextPath }/querySql.db',{sql:sql},function(data){
	  			//console.log(data);
	  			var resultdata = JSON.parse(data);
				var thead = '<tr>';
				$.each(resultdata.fileds,function(index,obj){
					thead += '<th>'+obj+'</th>';
				});
				thead += '</tr>';
				$('#thead').html(thead);
				var tbody = '';
				$.each(resultdata.listdata,function(index,obj){
					tbody +='<tr>';
					$.each(obj,function(inx,object){
						tbody += '<th>'+object+'</th>';
					});
					tbody +='</tr>';
				});
				$('#tbody').html(tbody);
				$('#queryresult').html('共查出'+resultdata.listdata.length+'条数据，数据库连接时间：'+resultdata.connecttime+"ms，SQL执行时间："+resultdata.sqltime+"ms");
			});
	  	});
	  	
	  	//导出excel
	  	$('#exportexcel').click(function(){
	  		var sql = $('#sql').val();
	  		if(sql){
	  			//$.get('${pageContext.request.contextPath }/exportsqldata.db',{sql:sql});
	  			sql = sql.replace(/\n/g,' ');
	  			window.location.href = '${pageContext.request.contextPath }/exportsqldata.db?sql='+sql;
	  		}else{
	  			layer.msg('sql语句不能为空');
	  		}
	  	});
	  </script>
</body>
</html>