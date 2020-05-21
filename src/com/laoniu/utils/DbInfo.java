package com.laoniu.utils;

import java.io.Serializable;

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
	
	
}
