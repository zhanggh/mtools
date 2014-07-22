package com.mtools.core.plugin.entity;

import java.io.Serializable;

public class Permission implements Serializable{

	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7896312659244014536L;
	public static final String TABLE_ALIAS = "Permission";
	public static final String TABLE_NAME = "PERMISSION";
	public static final String[] TABLE_KEYS = { "PERMID" };
	private String permid;
	private String menuid;
	private String permuri;
	private String permname;
	private String permdesc;
	private int permlevel;//0查询类型 1更新 2插入 3 修改 4删除
	private String permtype;// mo weix
	/**
	 * @return the permid
	 */
	public String getPermid() {
		return permid;
	}
	/**
	 * @param permid the permid to set
	 */
	public void setPermid(String permid) {
		this.permid = permid;
	}
	/**
	 * @return the menuid
	 */
	public String getMenuid() {
		return menuid;
	}
	/**
	 * @param menuid the menuid to set
	 */
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	/**
	 * @return the permuri
	 */
	public String getPermuri() {
		return permuri;
	}
	/**
	 * @param permuri the permuri to set
	 */
	public void setPermuri(String permuri) {
		this.permuri = permuri;
	}
	/**
	 * @return the permname
	 */
	public String getPermname() {
		return permname;
	}
	/**
	 * @param permname the permname to set
	 */
	public void setPermname(String permname) {
		this.permname = permname;
	}
	/**
	 * @return the permdesc
	 */
	public String getPermdesc() {
		return permdesc;
	}
	/**
	 * @param permdesc the permdesc to set
	 */
	public void setPermdesc(String permdesc) {
		this.permdesc = permdesc;
	}
	/**
	 * @return the permlevel
	 */
	public int getPermlevel() {
		return permlevel;
	}
	/**
	 * @param permlevel the permlevel to set
	 */
	public void setPermlevel(int permlevel) {
		this.permlevel = permlevel;
	}
	/**
	 * @return the permtype
	 */
	public String getPermtype() {
		return permtype;
	}
	/**
	 * @param permtype the permtype to set
	 */
	public void setPermtype(String permtype) {
		this.permtype = permtype;
	}
}
