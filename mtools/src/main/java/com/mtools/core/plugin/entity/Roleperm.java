package com.mtools.core.plugin.entity;

public class Roleperm  extends BaseDbStruct{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459103401803834256L;
	public static final String TABLE_ALIAS = "rp";
	public static final String TABLE_NAME = "ROLEPERM";
	public static final String[] TABLE_KEYS = { "ROLEID", "PERMID" };
	private String roleid;
	private String permid;

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getPermid() {
		return permid;
	}

	public void setPermid(String permid) {
		this.permid = permid;
	}

}
