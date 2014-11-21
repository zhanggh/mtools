package com.mtools.core.plugin.entity;

import java.sql.Timestamp;
import java.util.List;

import com.mtools.core.plugin.entiy.vo.UserVo;


/**
 * @author zhang 用户信息
 */
public class UserInfo  extends BaseDbStruct implements Cloneable{

	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8128093648505704813L;
	
	public static final String TABLE_ALIAS = "us";
	public static final String TABLE_NAME = "STMUSER";
	public static final String[] TABLE_KEYS = { "USERID" };
	
	private String userid;// 登陆名
	private String username;// 真实姓名（收件人名)
	private String password;// 登陆密码
	private String workphone;// 工作电话
	private String mobilephone;// 手机号码
	private String status;// 用户状态
	private String usertype;// 用户类型
	private String addr;// 收件地址
	private String email;// 邮件
	private String verifycode;// 验证码
	public String depid;//部门编号
	private List<UserInfo> childUser;//下属
	private Timestamp createTime;//创建时间
	public String fromIp;
	public String loginTime;
	
	
	/**
	 * @return the fromIp
	 */
	public String getFromIp() {
		return fromIp;
	}

	/**
	 * @param fromIp the fromIp to set
	 */
	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	/**
	 * @return the loginTime
	 */
	public String getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

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
	 * @return the childUser
	 */
	public List<UserInfo> getChildUser() {
		return childUser;
	}

	/**
	 * @param childUser the childUser to set
	 */
	public void setChildUser(List<UserInfo> childUser) {
		this.childUser = childUser;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private List<Permission> perms;

	public List<Permission> getPerms() {
		return perms;
	}

	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	 

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	/**  
	 * 功能：
	 */
	@Override
	public  UserVo clone() throws CloneNotSupportedException {
		UserVo us=new UserVo();
		us.setUserid(this.userid);
		us.setUsername(this.username);
		us.setCreateTime(this.createTime);
		us.setDepid(this.depid);
		us.setEmail(this.email);
		us.setMobilephone(this.mobilephone);
		us.setPassword(this.password);
		us.setStatus(this.status);
		us.setUsertype(this.usertype);
		us.setWorkphone(this.workphone);
		return us;
	}
	
}
