<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>数据库的java客户端</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
  <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">数据库的 java客户端</div>
		    <!-- 头部区域（可配合layui已有的水平导航） -->
		    <ul class="layui-nav layui-layout-left">
		    	<li class="layui-nav-item">
		    	　　　　　　　　　　　　　　　数据库IP：${dbconnectinfo.host }　　数据库名：${dbconnectinfo.servicename }　　数据库类型：${dbconnectinfo.dbtype }　　用户名：${dbconnectinfo.username }
		    	</li>
		    </ul>
		    <ul class="layui-nav layui-layout-right">
		      <li class="layui-nav-item">
		        <a href="javascript:;">
		          <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
		         		我的
		        </a>
		        <!-- <dl class="layui-nav-child">
		          <dd><a href="">基本资料</a></dd>
		          <dd><a href="">安全设置</a></dd>
		        </dl> -->
		      </li>
		      <li class="layui-nav-item"><a href="${pageContext.request.contextPath }/exit.db">退出</a></li>
		    </ul>
		</div>
		<div class="layui-side layui-bg-black">
		    <div class="layui-side-scroll">
		      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
		      <ul class="layui-nav layui-nav-tree"  lay-filter="test">
		        <li class="layui-nav-item layui-nav-itemed">
		          <a class="" href="javascript:;">数据库功能</a>
		          <dl class="layui-nav-child">
		            <dd><a href="javascript:openMenu('gettables.db');">数据库表</a></dd>
		            <dd><a href="javascript:openMenu('excutequery.db');">执行查询</a></dd>
		            <dd><a href="javascript:openMenu('excutesqlpage.db');">执行修改</a></dd>
		          </dl>
		        </li>
		        <!-- <li class="layui-nav-item">
		          <a href="javascript:;">解决方案</a>
		          <dl class="layui-nav-child">
		            <dd><a href="javascript:;">列表一</a></dd>
		            <dd><a href="javascript:;">列表二</a></dd>
		            <dd><a href="">超链接</a></dd>
		          </dl>
		        </li>
		        <li class="layui-nav-item"><a href="">云市场</a></li>
		        <li class="layui-nav-item"><a href="">发布商品</a></li> -->
		      </ul>
		    </div>
		  </div>
		  <div class="layui-body">
		    <!-- <div style="padding: 15px;">内容主体区域</div> -->
		    <iframe src="" frameborder="0" id="mainwindow" style="width: 100%; height: 830px;"></iframe>           
		  </div>
		  
		  <div class="layui-footer">
		    <!-- 底部固定区域 -->
		    © 北京同方软件有限公司 - 底部固定区域
		  </div>
	</div>
 

               
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
<script src="${pageContext.request.contextPath }/static/layui/dist/layui.js" charset="utf-8"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	function openMenu(url){
		$('#mainwindow').attr('src','${pageContext.request.contextPath }/'+url);
	}
</script>

</body>
</html>