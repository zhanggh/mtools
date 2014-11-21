/**
 * 通联支付-研发中心
 * Department.java
 * 2014-5-6
 */
package com.mtools.core.plugin.entity;

/**
 * @author zhang
 *  功能：部门
 * @date 2014-5-6
 */
public class Department extends BaseDbStruct{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1232585315893956712L;
	public static final String TABLE_ALIAS = "department";
	public static final String TABLE_NAME = "DEPARTMENT";
	public static final String[] TABLE_KEYS = { "depid" };
	
	public String depid;
	public String depname;
	public String parentid;
	public String principal;
	public String reserved;
	 
	/**
	 * @return the depid
	 */
	public String getDepid() {
		return depid;
	}
	/**
	 * @param depid the depid to set
	 */
	public void setDepid(String depid) {
		this.depid = depid;
	}
	/**
	 * @return the depname
	 */
	public String getDepname() {
		return depname;
	}
	/**
	 * @param depname the depname to set
	 */
	public void setDepname(String depname) {
		this.depname = depname;
	}
	/**
	 * @return the parentid
	 */
	public String getParentid() {
		return parentid;
	}
	/**
	 * @param parentid the parentid to set
	 */
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	/**
	 * @return the principal
	 */
	public String getPrincipal() {
		return principal;
	}
	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	/**
	 * @return the reserved
	 */
	public String getReserved() {
		return reserved;
	}
	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
}
