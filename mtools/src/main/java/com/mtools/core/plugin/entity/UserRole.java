package com.mtools.core.plugin.entity;

public class UserRole {

	public static final String TABLE_ALIAS = "userrole";
	public static final String TABLE_NAME = "USERROLE";
	public static final String[] TABLE_KEYS = { "USERID", "ROLEID" };

	private String userid;// 登陆名
	private String roleid;// 角色编号

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
}
