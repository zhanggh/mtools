/*通联支付网络服务股份有限公司版权所有
 *未经授权严禁查看传抄
 *
 *作者:pomme
 *日期:Jan 27, 2010 10:05:10 AM
 */
package com.mtools.core.plugin.mail;

import java.util.ArrayList;
import java.util.List;

public class MailDetailConfig {
	private String mailname = null;
	private String smtphost = null;
	private String smtpport = "25";
	private String pop3host = null;
	private String pop3port = "110";
	private String from = null;
	private String username = null;
	private String password = null;
	private boolean usessl = false;
	private List<String> to = new ArrayList();
	private List<String> cc = new ArrayList();
	private List<String> bcc = new ArrayList();
	private String subject;
	private String content;
	private String url;//验证地址
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	//邮件接收后保存路径
	private String savedir = null;
	//发送完毕后删除附件源文件(一般为false)
	private boolean deletesrc = false;
	private boolean debug = false;
	private String frommail;
	
	public String getMailname() {
		return mailname;
	}
	public void setMailname(String mailname) {
		this.mailname = mailname;
	}
	public String getSmtphost() {
		return smtphost;
	}
	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}
	public String getSmtpport() {
		return smtpport;
	}
	public void setSmtpport(String smtpport) {
		this.smtpport = smtpport;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isUsessl() {
		return usessl;
	}
	public void setUsessl(boolean usessl) {
		this.usessl = usessl;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public List<String> getBcc() {
		return bcc;
	}
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public boolean isDeletesrc() {
		return deletesrc;
	}
	public void setDeletesrc(boolean deletesrc) {
		this.deletesrc = deletesrc;
	}
	public String getPop3host() {
		return pop3host;
	}
	public void setPop3host(String pop3host) {
		this.pop3host = pop3host;
	}
	public String getPop3port() {
		return pop3port;
	}
	public void setPop3port(String pop3port) {
		this.pop3port = pop3port;
	}
	public String getSavedir() {
		return savedir;
	}
	public void setSavedir(String savedir) {
		this.savedir = savedir;
	}
	public String getFrommail() {
		return frommail;
	}
	public void setFrommail(String frommail) {
		this.frommail = frommail;
	}
}
