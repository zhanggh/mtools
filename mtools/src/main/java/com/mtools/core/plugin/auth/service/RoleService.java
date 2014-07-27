/**
 * UserService.java
 * 2014-4-16
 */
package com.mtools.core.plugin.auth.service;

import java.util.List;

import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.Role;
import com.mtools.core.plugin.entiy.vo.UserVo;

/**
 * @author zhang
 *	角色相关业务逻辑
 * 2014-4-16
 */
public interface RoleService {

	 /**
	 * 功能：获取角色列表
	 * 2014-4-29
	 */
	public List<Role> getRoles(Role role);
	 
	 /**
	 * 功能：获取用户角色
	 * 2014-4-29
	 */
	public void setUserRole(UserVo user);
	
	/**
	 * 功能：新增角色
	 * 2014-4-29
	 */
	
	public int addRole(Role role,Permission perm);
	/**
	 * 功能：修改角色
	 * 2014-4-29
	 */
	
	public int modRole(Role role,Permission perm);
	/**
	 * 功能：删除角色
	 * 2014-4-29
	 */
	
	public int deleteRole(Role role,Permission perm);
}
