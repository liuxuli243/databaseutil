package com.laoniu.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laoniu.annotation.LaoNiuParam;
import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.utils.DbInfo;
import com.laoniu.utils.ExcelExport;
import com.laoniu.utils.TestConnectUtil;

public class DbController {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 执行SQL语句（新增、修改、删除）
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@LaoNiuRequestMapping("/excutesql")
	public String excutesql(HttpServletRequest request,@LaoNiuParam("sql")String sql) throws IOException {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		boolean result = TestConnectUtil.excutesql(connection, sql);
		TestConnectUtil.close(connection);
		if (result) {
			return "1";
		}else {
			return "0";
		}
		
	}

	/**
	 * 获取表的数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping("tabledata")
	public void tabledata(HttpServletRequest request, HttpServletResponse response,@LaoNiuParam("tablename")String tablename) throws ServletException, IOException {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<String> listAllFiled = TestConnectUtil.listAllFiled(connection, tablename);
		request.setAttribute("allfileds", listAllFiled);
		TestConnectUtil.close(connection);
		request.setAttribute("tablename", tablename);
		request.getRequestDispatcher("/WEB-INF/jsp/tabledata.jsp").forward(request,response);
	}
	/**
	 * 分页查询表的数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@LaoNiuRequestMapping("gettabledata")
	public Map<String, Object> gettabledata(HttpServletRequest request,@LaoNiuParam("tablename")String tablename,@LaoNiuParam("page")int page,@LaoNiuParam("limit")int limit) throws ServletException, IOException {
		
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
	@LaoNiuRequestMapping("tablefileds")
	public void tablefileds(HttpServletRequest request,HttpServletResponse response,@LaoNiuParam("tablename")String tablename) throws ServletException, IOException {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<Map<String, Object>> tableFileds = TestConnectUtil.tableFileds(connection, tablename);
		TestConnectUtil.close(connection);
		request.setAttribute("tableFileds", tableFileds);
		request.setAttribute("tablename", tablename);
		request.getRequestDispatcher("/WEB-INF/jsp/tablefileds.jsp").forward(request,response);
	}
	/**
	 * 获取查询SQL的数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@LaoNiuRequestMapping("querySql")
	public Map<String, Object> querySql(HttpServletRequest request,HttpServletResponse response,@LaoNiuParam("sql")String sql) throws ServletException, IOException {
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
	@SuppressWarnings("rawtypes")
	@LaoNiuRequestMapping("gettables")
	public void gettables(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		List<Map> alltable = TestConnectUtil.listAllTables(connection, info);
		TestConnectUtil.close(connection);
		request.setAttribute("alltable", alltable);
		request.getRequestDispatcher("/WEB-INF/jsp/listtable.jsp").forward(request,response);
	}

	/**
	 * 测试连接
	 */
	@LaoNiuRequestMapping("testdb")
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
	@LaoNiuRequestMapping("connect")
	public void connect(DbInfo info,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		boolean testConnect = TestConnectUtil.TestConnect(info);
		if (testConnect) {
			request.getSession().setAttribute("dbconnectinfo", info);
			request.getSession().setMaxInactiveInterval(360000);
			request.getRequestDispatcher("/WEB-INF/jsp/dbindex.jsp").forward(request,response);
		}else {
			request.setAttribute("message", "数据库连接失败");
			request.setAttribute("info", info);
			request.getRequestDispatcher("/testdb.jsp").forward(request,response);
		}
	}
	/**
	 * 跳转到执行查询页面
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping("excutequery")
	public void excutequery(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/WEB-INF/jsp/excutequery.jsp").forward(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 跳转到执行sql的页面
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping("excutesqlpage")
	public void excutesqlpage(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/WEB-INF/jsp/excutesql.jsp").forward(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 退出系统
	 * @param request
	 * @param response
	 */
	@LaoNiuRequestMapping("exit")
	public void exit(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.getSession().removeAttribute("dbconnectinfo");
			request.getRequestDispatcher("testdb.jsp").forward(request,response); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@LaoNiuRequestMapping("exporttabledata")
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
	@LaoNiuRequestMapping("exportsqldata")
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
}
