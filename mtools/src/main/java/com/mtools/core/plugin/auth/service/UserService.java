/**
 * UserService.java
 * 2014-4-16
 */
package com.mtools.core.plugin.auth.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.entity.UserRole;
import com.mtools.core.plugin.entiy.vo.UserVo;
import com.mtools.core.plugin.helper.AIPGException;

/**
 * @author zhang
 *	用户相关业务逻辑
 * 2014-4-16
 */
public interface UserService {

	/**
	 * 功能：获取用户信息
	 * 2014-4-16
	 * @param model 
	 * @throws Exception 
	 */
	public UserInfo getUserInfo(UserInfo user) throws Exception;
	
	/**
	 * 功能：更新用户
	 * 2014-4-16
	 * @param urole 
	 * @throws AIPGException 
	 */
	
	public void upateStmUser(UserInfo user,UserRole urole, ModelMap model) throws AIPGException;
	
	/**
	 * 功能：删除用户
	 * 2014-4-16
	 * @throws AIPGException 
	 */
	
	public void deletStmUser(UserInfo user,ModelMap model) throws AIPGException;
	
	
	/**
	 * 功能：获取用户视图
	 * 2014-4-23
	 */
	public UserVo getUserVo(UserInfo user);
	/**
	 * 功能：获取用户视图
	 * 2014-4-23
	 */
	public List<UserVo> getUserVos(UserVo user,PageInfo page);

	/**
	 * 功能：
	 * 2014-5-7
	 * @throws AIPGException 
	 */
	public void addUser(UserInfo user, UserRole urole, ModelMap model) throws AIPGException;
}
