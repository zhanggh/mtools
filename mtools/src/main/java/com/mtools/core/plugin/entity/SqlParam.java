package com.mtools.core.plugin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhang
 * sql 与 参数 数据结构
 */
public class SqlParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6155450908947507635L;
	
	public String sql;
	public List params;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List getParams() {
		return params;
	}
	public void setParams(List params) {
		this.params = params;
	}
}
