package com.laoniu.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestConnectUtil {

	public static String getConnUrl(String dbtype,String ip,String port,String dbname){
		
		String url = "";
		if ("oracle".equals(dbtype)) {
			url += "jdbc:oracle:thin:@";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
		}
		if ("mysql".equals(dbtype)) {
			url += "jdbc:mysql://";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
		}
		if ("db2".equals(dbtype)) {
			url += "jdbc:db2://";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
		}
		if ("sqlserver".equals(dbtype)) {
			url += "jdbc:sqlserver://";
			url += ip.trim();
			url += ":"+ port.trim();
			url += ";DatabaseName=" + dbname;
		}
		if ("sybase".equals(dbtype)) {
			url += "jdbc:jtds:sybase:";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
		}
		if ("postgresql".equals(dbtype)) {
			url += "jdbc:postgresql://";
			url += ip.trim();
			url += "/" + dbname;
		}
		if ("informix".equals(dbtype)) {
			url += "jdbc:informix-sqli://";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
			url+=":INFORMIXSERVER=";
		}
		return url;
	}
	/**
	 * 测试连接
	*Title: TestConnect
	*author:刘旭利
	*Description: 
	　 * @param info
	　 * @return
	 */
	public static boolean TestConnect(DbInfo info){
		String url = getConnUrl(info.getDbtype(), info.getHost(), info.getPort(), info.getServicename());
		Connection conn = null;
		try {
			String driverstr = getDriverstr(info.getDbtype());
			conn = getConnection(driverstr,url, info.getUsername(), info.getPassword());
			if(null == conn){
				return false;
			}
			DatabaseMetaData meta =  conn.getMetaData();
			//
			if(null == meta){
				return false;
			} else {
				// 只有这里返回true
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(conn);
		}
		
		return false;
	}
	
	/**
	 * 获取JDBC连接
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String driverstr,String url, String username, String password) {
		Connection conn = null;
		try {
			// 不需要加载Driver. Servlet 2.4规范�?��容器会自动载�?
			// conn = DriverManager.getConnection(url, username, password);
			//
			Properties info =new Properties();
			//
			info.put("user", username);
			info.put("password", password);
			// !!! Oracle 如果想要获取元数�?REMARKS 信息,�?��加此参数
			info.put("remarksReporting","true");
			// !!! MySQL 标志�? 获取TABLE元数�?REMARKS 信息
			info.put("useInformationSchema","true");
			// 不知道SQLServer�?���?��设置...
			//
			Class.forName(driverstr);
			conn = DriverManager.getConnection(url, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 获取连接
	 * @param dbtype
	 * @param host
	 * @param port
	 * @param servicename
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String dbtype,String host,String port,String servicename, String username, String password) {
		
		String url = getConnUrl(dbtype, host, port, servicename);
		Connection conn = null;
		try {
			String driverstr = getDriverstr(dbtype);
			conn = getConnection(driverstr,url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return conn;
	}
	/**
	 * 获取连接
	 * @param info
	 * @return
	 */
	public static Connection getConnection(DbInfo info){
		String url = getConnUrl(info.getDbtype(), info.getHost(), info.getPort(), info.getServicename());
		Connection conn = null;
		try {
			String driverstr = getDriverstr(info.getDbtype());
			conn = getConnection(driverstr,url, info.getUsername(), info.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return conn;
	}
	/**
	 * 获取驱动
	 * @param dbtype 数据库类型
	 * @return
	 */
	public static String getDriverstr(String dbtype){
		String dbstr = "";
		if ("oracle".equals(dbtype)) {
			dbstr = "oracle.jdbc.driver.OracleDriver";
		}
		if ("mysql".equals(dbtype)) {
			dbstr = "com.mysql.jdbc.Driver";
		}
		if("db2".equals(dbtype)){
			dbstr = "com.ibm.db2.jdbc.jcc.DB2Driver";
		}
		if ("sqlserver".equals(dbtype)) {
			dbstr = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		if ("sybase".equals(dbtype)) {
			dbstr = "net.sourceforge.jtds.jdbc.Driver";
		}
		if ("postgresql".equals(dbtype)) {
			dbstr = "org.postgresql.Driver";
		}
		if ("informix".equals(dbtype)) {
			dbstr = "com.informix.jdbc.IfxDriver";
		}
		/*
		 * if ("access".contentEquals(dbtype)) { dbstr = "sun.jdbc.odbc.JdbcOdbcDriver";
		 * }
		 */
		return dbstr;
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	public static void close(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 关闭结果集
	*Title: closeResultSet
	*Description: 
	　 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 查询所有的表
	 * @param conn
	 * @return
	 */
	public static List<Map<String, Object>> listAllTables(Connection conn,DbInfo info){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ResultSet rs = null;
		try {
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, info.getUsername().toUpperCase(), null, new String[]{"TABLE","VIEW"});
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("table_name", rs.getString("TABLE_NAME"));
				map.put("remark", rs.getString("REMARKS"));
				map.put("table_type", rs.getString("TABLE_TYPE"));
				result.add(map);
			}
			closeResultSet(rs);
			closeStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * 根据表名，查询表的所有字段
	 * @param tablename
	 * @return
	 * @throws SQLException 
	 */
	public static List<String> listAllFiled(Connection conn,String tablename){
		List<String> result = new ArrayList<String>();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet resultset = metaData.getColumns(null, conn.getSchema(), tablename, null);
			while (resultset.next()) {
				result.add(resultset.getString("COLUMN_NAME"));
				
			}
			closeResultSet(resultset);
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 获取表的数据
	 * @param conn
	 * @param tablename
	 * @return
	 */
	public static Map<String, Object> getTableData(Connection conn,String tablename){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> allFiled = listAllFiled(conn, tablename);
			result.put("fileds", allFiled);
			Statement statement = conn.createStatement();
			ResultSet queryResult = statement.executeQuery("select * from "+tablename);
			List<List<String>> data = new ArrayList<List<String>>();
			while (queryResult.next()) {
				List<String> row = new ArrayList<String>();
				for (String filed : allFiled) {
					row.add(queryResult.getString(filed));
				}
				data.add(row);
			}
			result.put("data", data);
			result.put("total", data.size());
			closeResultSet(queryResult);
			closeStatement(statement);
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 获取表的数据量
	 * @param conn
	 * @param tablename
	 * @return
	 */
	public static int getTableDataCount(Connection conn,String tablename){
		int result = 0;
		try {
			
			Statement statement = conn.createStatement();
			String countsql = "select count(1) count from "+tablename;
			
			ResultSet queryResult = statement.executeQuery(countsql);
			ResultSetMetaData metaData = queryResult.getMetaData();
			String columnName = metaData.getColumnName(1);
			while (queryResult.next()) {
				result = queryResult.getInt(columnName);
			}
			closeResultSet(queryResult);
			closeStatement(statement);
		} catch (Exception e) {
			
		}
		return result;
	}
	/**
	 * 执行sql查询
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> querySql(Connection conn,String sql){
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("查询的sql语句："+sql);
		try {
			List<String> allFiled = new ArrayList<String>();
			Statement statement = conn.createStatement();
			ResultSet queryResult = statement.executeQuery(sql);
			ResultSetMetaData metaData = queryResult.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				allFiled.add(metaData.getColumnName(i));
			}
			result.put("fileds", allFiled);
			List<List<String>> listdata = new ArrayList<List<String>>();
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			while (queryResult.next()) {
				//查询用
				Map<String, Object> row = new HashMap<String, Object>();
				//导出excel用
				List<String> listrow = new ArrayList<String>();
				for (String filed : allFiled) {
					row.put(filed,queryResult.getString(filed));
					listrow.add(queryResult.getString(filed));
				}
				data.add(row);
				listdata.add(listrow);
			}
			result.put("data", data);
			result.put("listdata", listdata);
			closeResultSet(queryResult);
			closeStatement(statement);
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 执行SQL
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static boolean excutesql(Connection conn,String sql){
		boolean execute = false;
		try {
			Statement statement = conn.createStatement();
			execute = statement.execute(sql);
			execute = true;
			closeStatement(statement);
		} catch (SQLException e) {
		}
		return execute;
	}
	/**
	 * 查询所有的字段
	 * @param conn
	 * @param tablename
	 * @return
	 */
	public static List<Map<String, Object>> tableFileds(Connection conn,String tablename){
		List<Map<String, Object>> fileds = new  ArrayList<Map<String,Object>>();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet resultset = metaData.getColumns(null, conn.getSchema(), tablename, null);
			List<String> colums = new ArrayList<String>(); 
			ResultSetMetaData resultmeta = resultset.getMetaData();
			for (int i = 1; i < resultmeta.getColumnCount(); i++) {
				colums.add(resultmeta.getColumnName(i));
			}
			while (resultset.next()) {
				Map<String, Object> filed = new HashMap<String, Object>();
				for (String colum : colums) {
					filed.put(colum, resultset.getString(colum));
					filed.put("PRIMARYKEY", "");
				}
				fileds.add(filed);
			}
			//获取主键
			ResultSet primaryKeys = metaData.getPrimaryKeys(null, conn.getSchema(), tablename);
			
			while (primaryKeys.next()) {
				for (Map<String, Object> field : fileds) {
					if (primaryKeys.getString("COLUMN_NAME").equals(field.get("COLUMN_NAME"))) {
						field.put("PRIMARYKEY", "√");
					}
				}
			}
			closeResultSet(primaryKeys);
			closeResultSet(resultset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileds;
	}
	/**
	 * 获取所有的方法列表
	*Title: allFunction
	*author:liuxuli
	*Description: 
	　 * @param info
	　 * @return
	 */
	public static List<Map<String, Object>> allFunction(DbInfo info) {
		Connection conn = getConnection(info);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet functionrs = metaData.getFunctions(null, info.getUsername().toUpperCase(), null);
			ResultSetMetaData metaData2 = functionrs.getMetaData();
			List<String> colums = new ArrayList<String>();
			for (int i = 1; i < metaData2.getColumnCount(); i++) {
				colums.add( metaData2.getColumnName(i));
			}
			while (functionrs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String string : colums) {
					map.put(string,functionrs.getString(string));
				}
				result.add(map);
			}
			functionrs.close();
			close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取函数的脚本
	*Title: functionscript
	*author:liuxuli
	*Description: 
	　 * @param info
	　 * @param functionname
	　 * @return
	 */
	public static String functionscript(DbInfo info,String functionname) {
		String result = "";
		Connection connection = getConnection(info);
		try {
			Statement createStatement = connection.createStatement();
			ResultSet functionrs = createStatement.executeQuery(info.getFunctionScriptSql(functionname));
			result = info.getFunctionScript(functionrs);
			functionrs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(connection);
		return result;
	}
	/**
	 * 查询所有的存储过程
	*Title: allprocedure
	*author:liuxuli
	*Description: 
	　 * @param info
	　 * @return
	 */
	public static List<Map<String, Object>> allprocedure(DbInfo info){
		Connection conn = getConnection(info);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet functionrs = metaData.getProcedures(null, info.getUsername().toUpperCase(), null);
			ResultSetMetaData metaData2 = functionrs.getMetaData();
			List<String> colums = new ArrayList<String>();
			for (int i = 1; i < metaData2.getColumnCount(); i++) {
				colums.add( metaData2.getColumnName(i));
			}
			while (functionrs.next()) {
				String procedure_type = functionrs.getString("PROCEDURE_TYPE");
				if ("1".equals(procedure_type)) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (String string : colums) {
						map.put(string,functionrs.getString(string));
					}
					result.add(map);
				}
			}
			functionrs.close();
			close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 查询存储过程的脚本
	*Title: procedurescript
	*author:liuxuli
	*Description: 
	　 * @param info
	　 * @param procedurename
	　 * @return
	 */
	public static String procedurescript(DbInfo info, String procedurename) {
		String result = "";
		Connection connection = getConnection(info);
		try {
			Statement createStatement = connection.createStatement();
			ResultSet functionrs = createStatement.executeQuery(info.getProcedureScriptSql(procedurename));
			result = info.getProcedureScript(functionrs);
			functionrs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(connection);
		return result;
	}
	
	public static void main(String[] args) {
		Connection connection = getConnection("oracle", "10.10.129.222", "1521", "thtf", "thtf", "thtf");
		//List<String> listAllFiled = listAllFiled(connection, "SYS_DICT");
		tableFileds(connection, "SEQURENCE4ALARMID");
		close(connection);
	}
}
