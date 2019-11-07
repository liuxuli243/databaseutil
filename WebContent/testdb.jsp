<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试连接</title>
<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
  <style type="text/css">
  	body{overflow:hidden;}

	.video-player{background-color: transparent;display: block;position: absolute;z-index: 1;top:0;}
	.video_mask{ width:100%; height:100%; position:absolute; left:0; top:0; z-index:90; background-color:rgba(0,0,0,0.5); }
	.login{ height:400px;width:260px;padding: 20px;background-color:rgba(0,0,0,0.5);border-radius: 4px;position:absolute;left: 50%;top: 35%; margin:-150px 0 0 -150px;z-index:99;}
	.login h1{ text-align:center; color:#fff; font-size:24px; margin-bottom:20px; }
	.form_code{ position:relative; }
	.form_code .code{ position:absolute; right:0; top:1px; cursor:pointer; }
	.login_btn{ width:100%; }
  </style>
</head>
<body bgcolor="#99FFFF">
	<div class="video_mask"></div>
	<div class="login">
	    <h1>数据库连接</h1>
	    <form class="layui-form" id="dbinfo" action="${pageContext.request.contextPath }/connect.db" method="post">
	    	<div class="layui-form-item" >
	    		<div class="layui-input-inline" style="width:260px;">
					<select id="dbtype" name="dbtype" lay-filter="dbtype" style="width:260px;" value="${info.dbtype }">
						<option value="mysql">mysql</option>
						<option value="oracle">oracle</option>
						<option value="db2">DB2</option>
						<option value="sqlserver">SQL Server</option>
						<option value="sybase">Sybase</option>
						<option value="postgresql">PostgreSql</option>
						<option value="informix">Informix</option>
					</select>
	    		</div>
		    </div>
		    <div class="layui-form-item">
				<input class="layui-input" name="host" placeholder="数据库IP：" lay-verify="required" type="text" autocomplete="off" value="${info.host }">
		    </div>
	    	<div class="layui-form-item">
				<input class="layui-input" name="port" placeholder="数据库端口：" lay-verify="required" type="text" autocomplete="off" value="${info.port }">
		    </div>
		    <div class="layui-form-item">
				<input class="layui-input" name="servicename" placeholder="数据库名(服务名 )：" lay-verify="required" type="text" autocomplete="off" value="${info.servicename }">
		    </div>
	    	<div class="layui-form-item">
				<input class="layui-input" name="username" placeholder="数据库用户名：" lay-verify="required" type="text" autocomplete="off" value="${info.username }">
		    </div>
		    <div class="layui-form-item">
				<input class="layui-input" name="password" placeholder="数据库密码：" lay-verify="required" type="text" autocomplete="off" value="${info.password }">
		    </div>
		    <div class="layui-form-item">
    			<div class="layui-inline">
					<button type="button" class="layui-btn" onclick="submitform()">测试连接</button>
					<button type="submit" class="layui-btn" >连接</button>
					<button type="reset" class="layui-btn layui-btn-primary">重置</button>
    			</div>
    		</div>
    		<font color="#FF0000">${message}</font>
		</form>
	</div>
	<script src="${pageContext.request.contextPath }/static/layui/dist/layui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		layui.use('form', function() {
	        var form = layui.form();
	   });
		function submitform(){
			$.post('${pageContext.request.contextPath }/testdb.db',$('#dbinfo').serialize(),function(data){
				if(data == 1){
					layer.alert("连接成功");
				}else{
					layer.alert('连接失败');
				}
			});
		}
	</script>
</body>
</html>