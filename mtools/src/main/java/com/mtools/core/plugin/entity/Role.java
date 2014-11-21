package com.mtools.core.plugin.entity;


public class Role extends BaseDbStruct{

	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6328054563985222114L;
	public static final String TABLE_ALIAS = "rl";
	public static final String TABLE_NAME = "ROLE";
	public static final String[] TABLE_KEYS = { "roleid" };

	public String roleid;
	public String rolename;

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
