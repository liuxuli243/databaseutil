package com.laoniu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoniu.utils.DbInfo;
import com.laoniu.utils.ExcelExport;
import com.laoniu.utils.TestConnectUtil;

public class TestDb extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestURI.substring(contextPath.length()+1,requestURI.length()-3);
		try {
			//通过反射将请求的url转换为执行方法
			this.getClass().getDeclaredMethod(url, HttpServletRequest.class,HttpServletResponse.class).invoke(this, request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	/**
	 * 执行SQL语句（新增、修改、删除）
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void excutesql(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String sql = request.getParameter("sql");
		logger.info("执行sql语句："+sql);
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		boolean result = TestConnectUtil.excutesql(connection, sql);
		TestConnectUtil.close(connection);
		if (result) {
			logger.info("执行成功");
			response.getWriter().print("1");
		}else {
			logger.info("执行失败");
			response.getWriter().print("0");
		}
		
	}

	/**
	 * 获取表的数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void tabledata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String tablename = request.getParameter("tablename");
		logger.info("查询"+tablename+"表的数据");
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		Map<String, Object> tableData = TestConnectUtil.getTableData(connection, tablename);
		TestConnectUtil.close(connection);
		request.setAttribute("tableData", tableData);
		request.setAttribute("tablename", tablename);
		request.getRequestDispatcher("/WEB-INF/jsp/tabledata.jsp").forward(request,response);
	}
	/**
	 * 查看表结构
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void tablefileds(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String tablename = request.getParameter("tablename");
		logger.info("查看"+tablename+"表的表结构");
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
	public void querySql(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sql = request.getParameter("sql");
		logger.info("执行SQL："+sql);
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
		ObjectMapper objectMapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(objectMapper.writeValueAsString(tableData));;
	}
	/**
	 * 获取所有的表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@SuppressWarnings("rawtypes")
	public void gettables(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("获取所有的表");
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
	public void testdb(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        logger.info("测试是否连接成功");
		DbInfo info = getDbInfo(request);
		boolean testConnect = TestConnectUtil.TestConnect(info);
		if (testConnect) {
			response.getWriter().print("1");
		}else {
			response.getWriter().print("2");
		}
	}
	/**
	 * 连接到数据库
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void connect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		DbInfo info = getDbInfo(request);
		logger.info("连接到数据库");
		boolean testConnect = TestConnectUtil.TestConnect(info);
		if (testConnect) {
			logger.info("连接成功跳转到首页");
			request.getSession().setAttribute("dbconnectinfo", info);
			request.getSession().setMaxInactiveInterval(360000);
			request.getRequestDispatcher("/WEB-INF/jsp/dbindex.jsp").forward(request,response);
		}else {
			logger.info("连接失败跳转到连接页面");
			request.setAttribute("message", "数据库连接失败");
			request.setAttribute("info", info);
			request.getRequestDispatcher("/testdb.jsp").forward(request,response);
		}
	}
	/*
	 * 获取数据库连接对象
	 */
	public DbInfo getDbInfo(HttpServletRequest request) {
		String dbtype = request.getParameter("dbtype");
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String servicename = request.getParameter("servicename");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		DbInfo info = new DbInfo(dbtype, host, port, username, password, servicename);
		return info;
	}
	/**
	 * 跳转到执行查询页面
	 * @param request
	 * @param response
	 */
	public void excutequery(HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("跳转到执行查询的页面");
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
	public void excutesqlpage(HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("跳转到执行SQL的页面");
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
	public void exit(HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("退出系统");
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
	public void exporttabledata(HttpServletRequest request,HttpServletResponse response) {
		String tablename = request.getParameter("tablename");
		logger.info("导出表"+tablename+"的数据到Excel中");
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
	public void exportsqldata(HttpServletRequest request,HttpServletResponse response) {
		String sql = request.getParameter("sql");
		logger.info("导出SQL语句："+sql+"的查询结果到Excel中");
		DbInfo info = (DbInfo) request.getSession().getAttribute("dbconnectinfo");
		Connection connection = TestConnectUtil.getConnection(info);
		Map<String, Object> tableData = TestConnectUtil.querySql(connection, sql);
		TestConnectUtil.close(connection);
		ExcelExport excel = new ExcelExport("SQL语句执行后的数据", (List<String>)tableData.get("fileds"), (List<List<String>>)tableData.get("data"), "SQL语句执行后的数据.xlsx", response);
		try {
			excel.export();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
