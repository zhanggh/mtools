package com.mtools.core.plugin.entity;

import java.io.Serializable;

public class Emailconfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6843614022502187947L;
	public static final String TABLE_ALIAS = "sys_emailconfig";
	public static final String TABLE_NAME = "sys_emailconfig";
	public static final String[] TABLE_KEYS = { "id" };
	
	private int id;// 流水号
	private String toemails;//  目标地址
	private String subject;// 主题
	private String context;// 内容
	private String apptype;// 内容
	private String savedir;// 接受邮件目录
	
	
	public String getSavedir() {
		return savedir;
	}
	public void setSavedir(String savedir) {
		this.savedir = savedir;
	}
	public String getApptype() {
		return apptype;
	}
	public void setApptype(String apptype) {
		this.apptype = apptype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToemails() {
		return toemails;
	}
	public void setToemails(String toemails) {
		this.toemails = toemails;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
}
