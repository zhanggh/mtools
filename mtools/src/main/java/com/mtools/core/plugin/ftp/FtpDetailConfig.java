package com.mtools.core.plugin.ftp;

/**
 * FTP明细参数配置
 *
 */
public class FtpDetailConfig {
	private String cusid=null;
	private String ftpname = null;
	private String ftphost = null;
	private int ftpport = 21;
	//FTP模式 被动和主动
	private String ftpmode = "PASV";
	private String ftpuser = null;
	private String ftppwd = null;
	private String upremotepath = null;
	private String downremotepath = null;
	private String uplocalpath = null;
	private String downlocalpath = null;
	private String fileNametpl = null ;
	private boolean debug = true;
	private String protocol = null;
	
	private String rarpwd;
	private String rartype;
	private String signfilenametpl;
	private String signtype;
	private String signpwd;
	
	public String getCusid() {
		return cusid;
	}
	public void setCusid(String cusid) {
		this.cusid = cusid;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getFtpname() {
		return ftpname;
	}
	public void setFtpname(String ftpname) {
		this.ftpname = ftpname;
	}
	public String getFtphost() {
		return ftphost;
	}
	public void setFtphost(String ftphost) {
		this.ftphost = ftphost;
	}
	public int getFtpport() {
		return ftpport;
	}
	public void setFtpport(int ftpport) {
		this.ftpport = ftpport;
	}
	public String getFtpmode() {
		if(ftpmode!=null)
			ftpmode = ftpmode.toLowerCase();
		return ftpmode;
	}
	public void setFtpmode(String ftpmode) {
		this.ftpmode = ftpmode;
	}
	public String getFtpuser() {
		return ftpuser;
	}
	public void setFtpuser(String ftpuser) {
		this.ftpuser = ftpuser;
	}
	public String getFtppwd() {
		return ftppwd;
	}
	public void setFtppwd(String ftppwd) {
		this.ftppwd = ftppwd;
	}
	public String getUpremotepath() {
		return upremotepath;
	}
	public void setUpremotepath(String upremotepath) {
		this.upremotepath = upremotepath;
	}
	public String getDownremotepath() {
		return downremotepath;
	}
	public void setDownremotepath(String downremotepath) {
		this.downremotepath = downremotepath;
	}
	public String getUplocalpath() {
		return uplocalpath;
	}
	public void setUplocalpath(String uplocalpath) {
		this.uplocalpath = uplocalpath;
	}
	public String getDownlocalpath() {
		return downlocalpath;
	}
	public void setDownlocalpath(String downlocalpath) {
		this.downlocalpath = downlocalpath;
	}
	
	public String getFileNametpl() {
		return fileNametpl;
	}
	public void setFileNametpl(String fileNametpl) {
		this.fileNametpl = fileNametpl;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getRarpwd() {
		return rarpwd;
	}
	public void setRarpwd(String rarpwd) {
		this.rarpwd = rarpwd;
	}
	public String getRartype() {
		return rartype;
	}
	public void setRartype(String rartype) {
		this.rartype = rartype;
	}
	public String getSignfilenametpl() {
		return signfilenametpl;
	}
	public void setSignfilenametpl(String signfilenametpl) {
		this.signfilenametpl = signfilenametpl;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
	public String getSignpwd() {
		return signpwd;
	}
	public void setSignpwd(String signpwd) {
		this.signpwd = signpwd;
	}	
}
