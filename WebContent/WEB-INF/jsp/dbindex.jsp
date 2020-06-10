<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>欢迎使用轻量级数据库小工具</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/layui/dist/css/layui.css"  media="all">
  <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">欢迎使用轻量级数据库工具</div>
		    <!-- 头部区域（可配合layui已有的水平导航） -->
		    <ul class="layui-nav layui-layout-left">
		    	<li class="layui-nav-item">
		    	　　　　　　　　　　　　连接信息：　　数据库IP：${dbconnectinfo.host }　　数据库名：${dbconnectinfo.servicename }　　数据库类型：${dbconnectinfo.dbtype }　　用户名：${dbconnectinfo.username }
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
		            <dd><a href="javascript:openMenu('gettables.db','表和视图');">表和视图</a></dd>
		            <dd><a href="javascript:openMenu('excutequery.db','执行查询',1);">执行查询</a></dd>
		            <dd><a href="javascript:openMenu('excutesqlpage.db','执行修改');">执行修改</a></dd>
		            <dd><a href="javascript:openMenu('allfunction.db','函数列表');">函数列表</a></dd>
		            <dd><a href="javascript:openMenu('allprocedure.db','存储过程');">存储过程</a></dd>
		            <dd><a href="javascript:openMenu('interface/methodlist.db','方法列表');">方法列表</a></dd>
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
		    <!-- <iframe src="" frameborder="0" id="mainwindow" style="width: 100%; height: 830px;"></iframe>-->
		    <div class="layui-tab" lay-filter="content" lay-allowclose="true">
			  <ul class="layui-tab-title">
			  </ul>
			  <div class="layui-tab-content">
			  </div>
			</div>            
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
var element;
layui.use('element', function(){
  var $ = layui.jquery;
  element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
});

function openMenu(url,name,type){
	//$('#mainwindow').attr('src','${pageContext.request.contextPath }/'+url);
	var tabs = $(".layui-tab-title").children();
	var layid = name;
	if(type == 1){
		var layid = name + new Date().getTime();
	}
	//判断是否存在该菜单是否已经存在tab页中
	var msg = true;
	 $.each(tabs, function (i, item) {
        var tabid = $(item).attr("lay-id");
        if (tabid === layid) {
            msg = false;
        }

    });
	 if(msg){
		element.tabAdd('content', {
	        title: name //用于演示
	        ,content: '<iframe frameborder="0" src="${pageContext.request.contextPath }/'+url+'" style="width: 100%; height: 750px;"></iframe>'
	        ,id: layid //实际使用一般是规定好的id，这里以时间戳模拟下
      	});
	 }
	 //在当前新增的位置
	 tabs = $(".layui-tab-title").children();
	 $.each(tabs, function (i, item) {
        var tabid = $(item).attr("lay-id");
        if (tabid === layid) {
        	$(item).addClass('layui-this');
        	$(item).click();
        }else{
        	$(item).removeClass('layui-this');
        }

    });
}
</script>

</body>
</html>