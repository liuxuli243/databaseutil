<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="websocketserver" value="ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
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
	  <div class="layui-form-item">
	  	<div class="layui-input-block">
		  <div class="layui-progress layui-progress-big" lay-showPercent="yes" lay-filter="executeprogress" style="width: 80%;">
		  	<div id="percentdiv" class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>
		  </div>
	  	</div>
	  </div>
	   <div class="layui-form-item">
	   		<div class="layui-input-block">
		      <textarea  class="layui-textarea" id="result" name="result" rows="20" disabled="disabled"></textarea>
		    </div>
	   </div>
	  <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	  <script type="text/javascript">
	    var element;
	  	layui.use('element', function(){
		  element = layui.element;
		});
	  	//执行sql
	  	$('#submitbtn').click(function(){
	  		var sql = $('#sql').val();
	  		$('#result').html('');
	  		element.progress('executeprogress', '0%');
	  		connect();
	  		$.post('${pageContext.request.contextPath }/excutesql.db',{sql:sql},function(data){
	  			close();
			});
	  	});
	  	var ws;
	  	function connect(){
	  		if ("WebSocket" in window) {
               ws = new WebSocket("${websocketserver}/executesql");
                
               ws.onopen = function() {
            	   console.log("数据发送中...");
               };
                
               ws.onmessage = function (evt) { 
            	   var received_msg = JSON.parse(evt.data);
                   var result = $('#result').html();
                   $('#result').html(result + received_msg.message);
                   //滚动到最底
                   var height=$("#result")[0].scrollHeight;
                   $("#result").scrollTop(height);
                   element.progress('executeprogress', received_msg.percent+'%');
               };
               ws.onclose = function() { 
                  // 关闭 websocket
                  console.log("连接已关闭..."); 
               };
            }
            
            else{
               // 浏览器不支持 WebSocket
               alert("您的浏览器不支持 WebSocket!");
            }
	  	}
	  	function close(){
	  		ws.close();
	  	}
	  </script>
</body>
</html>