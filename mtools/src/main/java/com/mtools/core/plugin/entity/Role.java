package com.mtools.core.plugin.entity;

import java.io.Serializable;

public class Role implements Serializable{

	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6328054563985222114L;
	public static final String TABLE_ALIAS = "ROLE";
	public static final String TABLE_NAME = "ROLE";
	public static final String[] TABLE_KEYS = { "ROLEID" };

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
