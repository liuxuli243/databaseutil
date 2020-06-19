package com.laoniu.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.laoniu.annotation.LaoNiuParam;
import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.annotation.LaoNiuResponseBody;
import com.laoniu.utils.DbInfo;
import com.laoniu.utils.ExcelExport;
import com.laoniu.utils.TestConnectUtil;
import com.laoniu.websocket.ExecutesqlWebSocket;

public class DbController {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 执行SQL语句（新增、修改、删除）
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@LaoNiuRequestMapping(value = "/excutesql",description = "执行sql语句（新增、删除、修改）")
	public void excutesql(HttpServletRequest request,@LaoNiuParam("sql")String sql) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		/**记录可执行的sql**/
		List<String> sqllist = new ArrayList<String>();
		String[] split = sql.split("\\$\\$");
		for (String sqlstring : split) {
			//此处校验sql是否为空
			String checksql = sqlstring.replace("\n", " ").trim();
			if (checksql != null && checksql.length() > 0) {
				sqllist.add(sqlstring);
			}
		}
		//记录执行SQL的条数，计算百分比
		int size = sqllist.size();
		DecimalFormat decimalFormat0 = new DecimalFormat("#");
		for (int i = 0; i < sqllist.size(); i++) {
			Map<String, String> result = TestConnectUtil.excutesql(connection, sqllist.get(i));
			//计算百分比
			result.put("percent", decimalFormat0.format((Double.valueOf(i+1)/size)*100));
			ExecutesqlWebSocket.sendMessage(request.getSession(),JSONObject.toJSONString(result));
			if ("500".equals(result.get("code"))) {
				//遇到错误终止
				break;
			}
		}
		TestConnectUtil.close(connection);
		
	}

	/**
	 * 获取表的数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping(value = "tabledata",description = "跳转到数据库表的数据页面，并查询表的所有字段")
	public String tabledata(HttpServletRequest request, @LaoNiuParam("tablename")String tablename) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<String> listAllFiled = TestConnectUtil.listAllFiled(connection, tablename);
		request.setAttribute("allfileds", listAllFiled);
		TestConnectUtil.close(connection);
		request.setAttribute("tablename", tablename);
		return "tabledata";
	}
	/**
	 * 分页查询表的数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@LaoNiuRequestMapping(value = "gettabledata",description = "分页查询表的数据")
	@LaoNiuResponseBody
	public Map<String, Object> gettabledata(HttpServletRequest request,@LaoNiuParam("tablename")String tablename,@LaoNiuParam("page")int page,@LaoNiuParam("limit")int limit) {
		
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		int total = TestConnectUtil.getTableDataCount(connection, tablename);
		//分页查询
		Map<String, Object> querySql = TestConnectUtil.querySql(connection, info.getPageSql("select * from " + tablename, page, limit));
		TestConnectUtil.close(connection);
		querySql.put("code", 0);
		querySql.put("count", total);
		querySql.put("msg", "操作成功");
		return querySql;
	}
	/**
	 * 查看表结构
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping(value = "tablefileds",description = "查看表结构")
	public String tablefileds(HttpServletRequest request,@LaoNiuParam("tablename")String tablename) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<Map<String, Object>> tableFileds = TestConnectUtil.tableFileds(connection, tablename);
		TestConnectUtil.close(connection);
		request.setAttribute("tableFileds", tableFileds);
		request.setAttribute("tablename", tablename);
		return "tablefileds";
	}
	/**
	 * 获取查询SQL的数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping(value = "querySql",description = "执行查询的SQL语句，并返回查询的结果")
	@LaoNiuResponseBody
	public Map<String, Object> querySql(HttpServletRequest request,@LaoNiuParam("sql")String sql){
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		long connectstart = System.currentTimeMillis();
		Connection connection = TestConnectUtil.getConnection(info);
		long connectend = System.currentTimeMillis();
		logger.info("数据库连接时间："+(connectend-connectstart)+"ms");
		long sqlstart = System.currentTimeMillis();
		Map<String, Object> tableData = TestConnectUtil.querySql(connection, sql);
		long sqlend = System.currentTimeMillis();
		logger.info("SQL语句执行时间："+(sqlend-sqlstart)+"ms");
		//添加数据库连接时间和sql执行时间
		tableData.put("connecttime", (connectend-connectstart));
		tableData.put("sqltime", (sqlend-sqlstart));
		TestConnectUtil.close(connection);
		return tableData;
	}
	/**
	 * 获取所有的表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping(value = "gettables",description = "获取数据库的所有的表")
	public String gettables(HttpServletRequest request) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<Map<String, Object>> alltable = TestConnectUtil.listAllTables(connection, info);
		TestConnectUtil.close(connection);
		request.setAttribute("alltable", alltable);
		return "listtable";
	}

	/**
	 * 测试连接
	 */
	@LaoNiuRequestMapping(value = "testdb",description = "测试是否连接成功")
	@LaoNiuResponseBody
	public String testdb(DbInfo info){
		boolean testConnect = TestConnectUtil.TestConnect(info);
		if (testConnect) {
			return "1";
		}else {
			return "2";
		}
	}
	/**
	 * 连接到数据库
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@LaoNiuRequestMapping(value = "connect",description = "连接到数据库（如果连接成功跳转到主页）")
	public String connect(DbInfo info,HttpServletRequest request) {
		boolean testConnect = TestConnectUtil.TestConnect(info);
		if (testConnect) {
			request.getSession().setAttribute("dbconnectinfo", info);
			request.getSession().setMaxInactiveInterval(360000);
			return "dbindex";
		}else {
			request.setAttribute("message", "数据库连接失败");
			request.setAttribute("info", info);
			return "testdb";
		}
	}
	/**
	 * 跳转到执行查询页面
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping(value = "excutequery",description = "跳转到执行sql查询的页面")
	public String excutequery() {
		return "excutequery";
	}
	/**
	 * 跳转到执行sql的页面
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping(value = "excutesqlpage", description = "跳转到执行sql的页面")
	public String excutesqlpage() {
		return "excutesql";
	}
	/**
	 * 退出系统
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping(value = "exit", description = "退出，返回到登录页面")
	public String exit(HttpServletRequest request) {
		request.getSession().removeAttribute("dbconnectinfo");
		return "testdb"; 
	}
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@LaoNiuRequestMapping(value = "exporttabledata",description = "将数据库表的数据导出到excel")
	public void exporttabledata(HttpServletRequest request,HttpServletResponse response) {
		String tablename = request.getParameter("tablename");
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		Map<String, Object> tableData = TestConnectUtil.getTableData(connection, tablename);
		TestConnectUtil.close(connection);
		ExcelExport excel = new ExcelExport(tablename+"表数据", (List<String>)tableData.get("fileds"), (List<List<String>>)tableData.get("data"), tablename+"表数据.xlsx", response);
		try {
			excel.export();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将查询的sql语句导出excel
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@LaoNiuRequestMapping(value = "exportsqldata", description = "将sql的查询结果导出excel")
	public void exportsqldata(HttpServletRequest request,HttpServletResponse response,@LaoNiuParam("sql")String sql) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		Map<String, Object> tableData = TestConnectUtil.querySql(connection, sql);
		TestConnectUtil.close(connection);
		ExcelExport excel = new ExcelExport("SQL语句执行后的数据", (List<String>)tableData.get("fileds"), (List<List<String>>)tableData.get("listdata"), "SQL语句执行后的数据.xlsx", response);
		try {
			excel.export();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@LaoNiuRequestMapping(value = "allfunction" ,description = "跳转到查询所有函数的页面")
	 public String allfunction(HttpServletRequest request) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		List<Map<String, Object>> allFunction = TestConnectUtil.allFunction(info);
		request.setAttribute("functions", allFunction);
		return "functionlist";
	}
	
	@LaoNiuRequestMapping(value = "functionscript" ,description="获取函数的脚本")
	public String functionscript(HttpServletRequest request,@LaoNiuParam("functionname")String functionname) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		String script = TestConnectUtil.functionscript(info, functionname);
		request.setAttribute("script", script);
		request.setAttribute("functionname", functionname);
		return "functionscript";
	}
	
	@LaoNiuRequestMapping(value = "allprocedure",description="跳转到存储过程列表页面")
	public String allprocedure(HttpServletRequest request) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		List<Map<String, Object>> allprocedure = TestConnectUtil.allprocedure(info);
		request.setAttribute("procedures", allprocedure);
		return "procedurelist";
	}
	
	@LaoNiuRequestMapping(value = "procedurescript",description = "获取存储过程的脚本")
	public String procedurescript(HttpServletRequest request,@LaoNiuParam("procedurename")String procedurename) {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		String script = TestConnectUtil.procedurescript(info, procedurename);
		request.setAttribute("script", script);
		request.setAttribute("procedurename", procedurename);
		return "procedurescript";
	}
}
