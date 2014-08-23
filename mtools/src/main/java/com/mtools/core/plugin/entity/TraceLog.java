/**
 * TraceLog.java
 * 2014-4-14
 */
package com.mtools.core.plugin.entity;

import java.sql.Timestamp;

/**
 * @author zhang
 * 
 *         2014-4-14
 */
public class TraceLog {

	public static final String TABLE_ALIAS = "tracelog";
	public static final String TABLE_NAME = "tracelog";
	public static final String[] TABLE_KEYS = { "optid" };

	private String optid;// 操作编号
	private String loginuser;// 操作用户
	private String optUrl;// 功能url
	private String optName;// 操作功能
	private String orgParams;// 原参数
	private Timestamp optTime;// 操作时间
	private String optResult;// 0操作失败 1操作成功
	private String fromIp;//访问者ip

	public String getOptid() {
		return optid;
	}

	public void setOptid(String optid) {
		this.optid = optid;
	}

	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public String getOptUrl() {
		return optUrl;
	}

	public void setOptUrl(String optUrl) {
		this.optUrl = optUrl;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getOrgParams() {
		return orgParams;
	}

	public void setOrgParams(String orgParams) {
		this.orgParams = orgParams;
	}

	public Timestamp getOptTime() {
		return optTime;
	}

	public void setOptTime(Timestamp optTime) {
		this.optTime = optTime;
	}

	public String getOptResult() {
		return optResult;
	}

	public void setOptResult(String optResult) {
		this.optResult = optResult;
	}

}
