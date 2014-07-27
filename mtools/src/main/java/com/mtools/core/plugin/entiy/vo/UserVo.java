/**
 * 通联支付-研发中心
 * UserVo.java
 * 2014-4-23
 */
package com.mtools.core.plugin.entiy.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mtools.core.plugin.entity.Role;
import com.mtools.core.plugin.entity.UserInfo;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-23
 */
public class UserVo extends UserInfo {

	
	public String rolename;
	public String fromIp;
	public String loginTime;
	public String depname;//部门名称
	public List<Role> roles;
	Map<String, String> roleM;
	
	
	/**
	 * @return the roleM
	 */
	public Map<String, String> getRoleM() {
		if(roleM==null)
			roleM=Maps.newConcurrentMap();
		return roleM;
	}
	/**
	 * @param roleM the roleM to set
	 */
	public void setRoleM(Map<String, String> roleM) {
		this.roleM = roleM;
	}
	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		if(roles==null){
			roles=Lists.newArrayList();
		}
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	/**
	 * @return the rolename
	 */
	public String getRolename() {
		return rolename;
	}
	/**
	 * @param rolename the rolename to set
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
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
	
}
