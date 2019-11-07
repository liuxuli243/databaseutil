package com.thtf.utils;

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
		if ("informix".contentEquals(dbtype)) {
			url += "jdbc:informix-sqli://";
			url += ip.trim();
			url += ":"+ port.trim();
			url += "/" + dbname;
			url+=":INFORMIXSERVER=";
		}
		return url;
	}
	
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
	 * @param dbtype
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
		if ("informix".contentEquals(dbtype)) {
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
	 * 查询所有的表
	 * @param conn
	 * @return
	 */
	public static List<Map> listAllTables(Connection conn,DbInfo info){
		List<Map> result = new ArrayList<Map>();
		ResultSet rs = null;
		try {
			conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, info.getUsername().toUpperCase(), null, new String[]{"TABLE","VIEW"});
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("table_name", rs.getString("TABLE_NAME"));
				map.put("remark", rs.getString("REMARKS"));
				map.put("table_type", rs.getString("TABLE_TYPE"));
				result.add(map);
			}
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
			Statement statement = conn.createStatement();
			ResultSet queryResult = statement.executeQuery("select * from "+tablename);
			ResultSetMetaData metaData = queryResult.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				result.add(metaData.getColumnName(i));
			}
			/*DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getColumns(null, "%", tablename, "%");
			while (rs.next()) {
				result.add(rs.getString("COLUMN_NAME"));
			}*/
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
		try {
			List<String> allFiled = new ArrayList<String>();
			Statement statement = conn.createStatement();
			ResultSet queryResult = statement.executeQuery(sql);
			ResultSetMetaData metaData = queryResult.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				allFiled.add(metaData.getColumnName(i));
			}
			result.put("fileds", allFiled);
			List<List<String>> data = new ArrayList<List<String>>();
			while (queryResult.next()) {
				List<String> row = new ArrayList<String>();
				for (String filed : allFiled) {
					row.add(queryResult.getString(filed));
				}
				data.add(row);
			}
			result.put("data", data);
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
				}
				fileds.add(filed);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileds;
	}
	
	
	public static void main(String[] args) {
		Connection connection = getConnection("oracle", "10.10.129.222", "1521", "thtf", "thtf", "thtf");
		//List<String> listAllFiled = listAllFiled(connection, "SYS_DICT");
		tableFileds(connection, "SYS_DICT");
		close(connection);
	}
}
