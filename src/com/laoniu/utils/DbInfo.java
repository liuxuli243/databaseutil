package com.laoniu.utils;

import java.io.Serializable;
import java.sql.ResultSet;

public class DbInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2764445785376077748L;

	private String dbtype;
	private String host;
	private String port;
	private String username;
	private String password;
	private String servicename;
	
	
	public DbInfo(String dbtype, String host, String port, String username,
			String password, String servicename) {
		super();
		this.dbtype = dbtype;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.servicename = servicename;
	}
	public DbInfo() {
	}
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	/**
	 * 获取分页查询的sql
	 * @param sqlstr
	 * @param page
	 * @param limit
	 * @return
	 */
	public String getPageSql(String sqlstr,int page,int limit) {
		String result = "";
		if ("oracle".equals(dbtype)) {
			int start = (page -1 ) * limit + 1;
			int end = page * limit;
			result = "SELECT * FROM ( "
					+ "SELECT pagesql.*, ROWNUM RN "
					+ "from ("
						+sqlstr 
					+ ") pagesql "
					+ "WHERE ROWNUM <= "+end
					+") WHERE RN >= "+start;
		}
		if ("mysql".equals(dbtype)) {
			int start = (page -1 ) * limit;
			result = sqlstr + " limit "+start+","+limit;
		}
		if("db2".equals(dbtype)){
			int start = (page -1 ) * limit + 1;
			int end = page * limit;
			result = "SELECT * FROM ( "
					+ "SELECT pagesql.*, rownumber() over() as rowid "
					+ "from ("
						+sqlstr 
					+ ") pagesql "

					+") tmp WHERE tmp.rowid <= "+end + " and tmp.rowid>="+start;
		}
		if ("sqlserver".equals(dbtype)) {
			int start = (page -1 ) * limit;
			
			result = sqlstr + "offset "+start
					+"fetch next "+limit+" rows only";
		}
		if ("sybase".equals(dbtype)) {
			int start = (page -1 ) * limit+1;
			result = "select top "+(start+limit)+" rowid=identity(12), * "
					+"into #tmp from ( "
					+sqlstr
					+" ) "
					+"select * from #tmp where rowid >="+start;
		}
		if ("postgresql".equals(dbtype)) {
			int start = (page -1 ) * limit;
			result = sqlstr + "limit "+limit+", offset "+start;
		}
		if ("informix".equals(dbtype)) {
			int start = (page -1 ) * limit;
			sqlstr = sqlstr.substring(sqlstr.indexOf("select"));
			result = "select skip "+start + " first "+limit + " "+ sqlstr;
		}
		return result;
	}
	/**
	 * 查询所有函数脚本的sql语句
	*Title: getFunctionScriptSql
	*author:liuxuli
	*Description: 
	　 * @param functionname
	　 * @return
	 */
	public String getFunctionScriptSql(String functionname) {
		String result = "";
		if ("oracle".equals(dbtype)) {
			result = "SELECT text " + 
					"    FROM user_source " + 
					"   WHERE NAME = '"+functionname.toUpperCase()+"' " + 
					"ORDER BY line";
		}
		if ("mysql".equals(dbtype)) {
			result = "SHOW CREATE FUNCTION "+functionname;
		}
		return result;
	}
	/**
	 * 获取函数的脚本
	*Title: getScript
	*author:liuxuli
	*Description: 
	　 * @param rs
	　 * @return
	 */
	public String getFunctionScript(ResultSet rs) {
		StringBuffer result = new StringBuffer("");
		try {
			if ("oracle".equals(dbtype)) {
				while (rs.next()) {
					result.append(rs.getString("TEXT"));
				}
			}else if ("mysql".equals(dbtype)) {
				while (rs.next()) {
					result.append(rs.getString("Create Function"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
	/**
	 * 查询存储过程脚本的sql语句
	*Title: getFunctionScriptSql
	*author:liuxuli
	*Description: 
	　 * @param functionname
	　 * @return
	 */
	public String getProcedureScriptSql(String procedurename) {
		String result = "";
		if ("oracle".equals(dbtype)) {
			result = "SELECT text " + 
					"    FROM user_source " + 
					"   WHERE NAME = '"+procedurename.toUpperCase()+"' " + 
					"ORDER BY line";
		}
		if ("mysql".equals(dbtype)) {
			result = "SHOW CREATE PROCEDURE "+procedurename;
		}
		return result;
	}
	/**
	 * 获取存储过程的脚本
	*Title: getProcedureScript
	*author:liuxuli
	*Description: 
	　 * @param rs
	　 * @return
	 */
	public String getProcedureScript(ResultSet rs) {
		StringBuffer result = new StringBuffer("");
		try {
			if ("oracle".equals(dbtype)) {
				while (rs.next()) {
					result.append(rs.getString("TEXT"));
				}
			}else if ("mysql".equals(dbtype)) {
				while (rs.next()) {
					result.append(rs.getString("Create Procedure"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
}
