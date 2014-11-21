/**
 * UserServiceImpl.java
 * 2014-4-16
 */
package com.mtools.core.plugin.auth.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.auth.service.RoleService;
import com.mtools.core.plugin.auth.service.UserService;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.entity.UserRole;
import com.mtools.core.plugin.entiy.vo.UserVo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.security.Crypto;

/**
 * @author zhang
 * 
 *         2014-4-16
 */
@Service("userService")
public class UserServiceImpl extends BasePlugin implements UserService {

	@Resource(name = "roleService")
	RoleService roleService;

	/**
	 * 功能： 登录
	 * 
	 * @throws Exception
	 */
	
	public UserInfo getUserInfo(UserInfo user) throws Exception {
		String pwd = Crypto.encode(user.getPassword());
		String sql = "select * from StmUser t where t.userid=? and t.password=? ";
		UserInfo suser = (UserInfo) this.dao.getObj(sql, UserInfo.class,
				user.getUserid(), pwd);
		return suser;
	}

	/**
	 * 功能：修改用户资料
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"getUserVo","getUserVos","userCache"},  allEntries=true)
	public void upateStmUser(UserInfo user, UserRole urole, ModelMap model) throws AIPGException {
		try { 
			int ret = this.dao.update(user);
			if(urole!=null){
				String[] ids = urole.getRoleid().split(",");
				this.dao.delete("delete from userrole u where u.userid=?",
						user.getUserid());
				for (String rid : ids) {
					urole.setRoleid(rid);
					this.dao.add(urole);
				}	
			}
			if(ret<1){
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "更新用户资料失败");
			}
			model.addAttribute(CoreConstans.OPTRESULT, "更新用户资料成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "更新用户资料成功!");
		} catch (Exception ex) {
			log.info("更新用户资料失败:" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "更新用户资料失败");
			model.put(CoreConstans.ERROR_MESSAGE, "更新用户资料失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, errorMsg);
		}
	}

	/**
	 * 功能：删除用户
	 * 
	 * @throws AIPGException
	 */
	
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"getUserVo","getUserVos","userCache"},  allEntries=true)
	public void deletStmUser(UserInfo user, ModelMap model)
			throws AIPGException {
		try {
			String[] ids = user.getUserid().split(",");
			for (String id : ids) {
				user.setUserid(id);
				this.dao.delete(user);
				this.dao.delete("delete from userrole u where u.userid=?", id);
			}
			model.addAttribute(CoreConstans.OPTRESULT, "删除用户资料成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除用户资料成功!");
		} catch (Exception ex) {
			log.info("删除用户资料失败:" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "删除用户资料失败");
			model.put(CoreConstans.ERROR_MESSAGE, "删除用户资料失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "删除用户失败!");
		}

	}

	/**
	 * 功能：
	 * @throws Exception 
	 */
	
	@Cacheable(value="getUserVo",key="#user.userid")
	public UserVo getUserVo(UserInfo user) throws Exception {
		String sql = "select u.*,d.depname from stmuser u,department d where u.userid=? and u.depid=d.depid";
		UserVo vo = (UserVo) this.dao.getObj(sql, UserVo.class,
				user.getUserid());
		roleService.setUserRole(vo);
		return vo;
	}

	/**
	 * 功能：查询用户列表
	 * @throws Exception 
	 */
	
	@Cacheable(value="getUserVos",key="#user.userid+''+#user.username+''+#page.pageIndex+''+#page.pageSize")
	public List<UserVo> getUserVos(UserVo user, PageInfo page) throws Exception {
		String sql = "select u.*,d.depname from stmuser u,department d where u.depid=d.depid";
		if (!FuncUtil.isEmpty(user.getUserid())) {
			sql += " and u.userid='" + user.getUserid() + "'";
		}
		if (!FuncUtil.isEmpty(user.getUsername())) {
			sql += " and u.username='" + user.getUsername() + "'";
		}
		// 总笔数
		int count = this.dao.count(sql);
		if (!FuncUtil.isEmpty(page.getSort().getId())) {
			sql += " order by u.userid " + page.getSort().getId();
		}
		if (!FuncUtil.isEmpty(page.getSort().getName())) {
			sql += " order by u.username " + page.getSort().getName();
		}
		page.setItemCount(count);
		List<UserVo> users = this.dao.searchPage(sql, UserVo.class,
				Integer.parseInt(page.getPageIndex()),
				Integer.parseInt(page.getPageSize()), null);
		for (UserVo vo : users) {
			roleService.setUserRole(vo);
		}
		return users;
	}

	/**
	 * 功能：新增用户
	 * 
	 * @throws AIPGException
	 */
	
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"getUserVo","getUserVos","userCache"},  allEntries=true)
	public void addUser(UserInfo user, UserRole urole, ModelMap model)
			throws AIPGException {
		try {
			this.errorMsg = "新增用户成功";
			user.setCreateTime(FuncUtil.getCurrTimestamp());
			String pwd = Crypto.encode(user.getPassword());
			user.setPassword(pwd);
			this.dao.add(user);
			if(urole==null||urole.getRoleid()==null)
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "请选择用户相应的职务!");
			String[] roleid = urole.getRoleid().split(",");
			for (String id : roleid) {
				urole.setRoleid(id);
				this.dao.add(urole);
			}
			model.addAttribute(CoreConstans.OPTRESULT, errorMsg);
			model.put(CoreConstans.SUCCESSMESSAGE, errorMsg);
		} catch (DuplicateKeyException ex) {
			this.errorMsg = "登录号已经存在!";
			log.error(errorMsg + "\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, errorMsg);
			model.put(CoreConstans.ERROR_MESSAGE, errorMsg);
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, errorMsg);
		} catch (AIPGException ex) {
			this.errorMsg = ex.getMessage();
			log.error(errorMsg + "\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, errorMsg);
			model.put(CoreConstans.SUCCESSMESSAGE, errorMsg);
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, errorMsg);
		} catch (Exception ex) {
			this.errorMsg ="新增用户失败!";
			log.error(errorMsg + "\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, errorMsg);
			model.put(CoreConstans.SUCCESSMESSAGE, errorMsg);
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, errorMsg);
		}
	}
}
