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
	
	
}
