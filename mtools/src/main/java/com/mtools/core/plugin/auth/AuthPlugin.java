/**
 * AuthPlugin.java
 * 2014-4-14
 */
package com.mtools.core.plugin.auth;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.Role;
import com.mtools.core.plugin.entity.RoleVo;
import com.mtools.core.plugin.entity.Roleperm;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.entiy.vo.AuthVo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.FuncUtil;

/**
 * @author zhang 权限管理插件 2014-4-14
 */
@Component("auth")
public class AuthPlugin extends BasePlugin {

	@Cacheable(value="permOfUser",key="#userId + 'AuthPlugin.getPermOfUser'+#permType")
	public List<Permission> getPermOfUser(String userId, String permType) {
		String sql = "select p.* from Permission p, roleperm r, USERROLE U where p.permid = r.permid and u.roleid = r.roleid and p.permtype=? and u.userid = ?";
		List<Permission> perms = this.dao.search(sql, Permission.class,
				permType, userId);
		return perms;
	}

	@Cacheable(value="allPerms",key="AuthPlugin.getPerms")
	public List<Permission> getPerms() {

		String sql = "select p.* from Permission p";
		List<Permission> perms = this.dao.search(sql, Permission.class, null);
		return perms;
	}

	/**
	 * 功能：判断权限 2014-4-18
	 */
	public boolean checkUri(List<Permission> perms, String uri) {
		if (perms == null) {
			return false;
		} else {
			for (Permission perm : perms) {
				if (uri.equals(perm.getPermuri())) {
					return true;
				}
			}
		}
		return false;
	}

	@Cacheable(value="permByUri",key="#Uri + 'AuthPlugin.getPermByUri'")
	public Permission getPermByUri(String Uri) {
		String sql = "select p.* from Permission p where p.permuri like '%"
				+ Uri.substring(1) + "%'";
		List<Permission> perms = this.dao.search(sql, Permission.class, null);
		if (perms.size() > 0) {
			return perms.get(0);
		}
		return null;
	}

	/**
	 * 功能：根据url或者访问名称 2014-4-14
	 */
	@Cacheable(value="permNameByUri",key="#Uri + 'AuthPlugin.getPermName'")
	public String getPermName(String Uri) {
		Permission perm = getPermByUri(Uri);
		if (perm != null) {
			return perm.getPermname();
		} else {
			return "未知功能";
		}
	}

	/**
	 * 功能：检查是否具备访问权限 2014-4-14
	 */
	public void checkAccess(HttpServletRequest request) throws AIPGException {
		String path = request.getServletPath();
		log.info("访问URI:" + path);
		UserInfo user = (UserInfo) request.getSession().getAttribute(
				CoreConstans.LOGINGUSER);
		if (user != null) {
			List<Permission> perms = user.getPerms();
			if (perms == null || !checkUri(perms, path)) {
				throw new AIPGException(CoreConstans.EXCEPTON_03, "没有权限进行此操作");
			}
		}
	}

	/**
	 * 功能：查询权限列表 2014-4-23
	 * 
	 * @param page
	 */
	@Cacheable(value="searchPerms",key="#perm.permid+''+#perm.permname+'AuthPlugin.searchPerm'+#perm.permuri+''+#page.pageIndex+''+#page.pageSize")
	public List<AuthVo> searchPerm(Permission perm, String permtype,
			PageInfo page) {
		String sql = "select p.* ,nvl(m.name,'其他') menuname from permission p left join menuinfo m on m.menuid=p.menuid where p.permtype=? ";
		if (!FuncUtil.isEmpty(perm.getPermname())) {
			sql += " and permname like '%" + perm.getPermname() + "%'";
		}
		if (!FuncUtil.isEmpty(perm.getMenuid())) {
			sql += " and p.menuid =" + perm.getMenuid();
		}
		if (!FuncUtil.isEmpty(perm.getPermid())) {
			sql += " and p.permid =" + perm.getPermid();
		}
		// 总笔数
		int count = this.dao.count(sql, permtype);
		if (!FuncUtil.isEmpty(page.getSort().getId())) {
			sql += " order by permid " + page.getSort().getId();
		}
		if (!FuncUtil.isEmpty(page.getSort().getName())) {
			sql += " order by permname " + page.getSort().getName();
		}
		page.setItemCount(count);
		List<AuthVo> perms = this.dao.searchPage(sql, AuthVo.class,
				Integer.parseInt(page.getPageIndex()),
				Integer.parseInt(page.getPageSize()), permtype);
		return perms;
	}

	/**
	 * 功能：新增权限 2014-4-23
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"permOfUser","searchPerms","permNameByUri","allPerms","UnselPermsByRole","selectPermRole","authCache"},  allEntries=true)
	public void addAuth(Permission perm, String permtype, ModelMap model) {

		try {
			perm.setPermtype(permtype);
			if (this.dao.add(perm) > 0) {
				model.addAttribute(CoreConstans.OPTRESULT, "新增权限成功");
				model.put(CoreConstans.SUCCESSMESSAGE, "新增权限成功!");
			} else {
				model.addAttribute(CoreConstans.OPTRESULT, "新增权限失败");
				model.put(CoreConstans.SUCCESSMESSAGE, "新增权限失败!");
			}
		} catch (Exception ex) {
			log.error("新增权限失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "新增权限失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "新增权限失败!");
		}

	}

	/**
	 * 功能：修改权限 2014-4-23
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"permOfUser","searchPerms","permNameByUri","allPerms","UnselPermsByRole","selectPermRole","authCache"},  allEntries=true)
	public void modAuth(Permission perm, ModelMap model) {
		try {
			if (this.dao.update(perm) > 0) {
				model.addAttribute(CoreConstans.OPTRESULT, "修改权限成功");
				model.put(CoreConstans.SUCCESSMESSAGE, "修改权限成功!");
			} else {
				model.addAttribute(CoreConstans.OPTRESULT, "修改权限失败");
				model.put(CoreConstans.SUCCESSMESSAGE, "修改权限失败!");
			}
		} catch (Exception ex) {
			log.error("修改权限失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "修改权限失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "修改权限失败!");
		}

	}

	/**
	 * 功能： 2014-4-30
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"permOfUser","searchPerms","permNameByUri","allPerms","UnselPermsByRole","selectPermRole","authCache"},  allEntries=true)
	public void deleteAuth(Permission perm, ModelMap model) {
		try {
			String[] ids = perm.getPermid().split(",");
			for (String id : ids) {
				perm.setPermid(id);
				this.dao.delete(perm);
			}
			model.addAttribute(CoreConstans.OPTRESULT, "删除权限成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除权限成功!");
		} catch (Exception ex) {
			log.error("删除权限失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "删除权限失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除权限失败!");
		}
	}

	/**
	 * 功能： 2014-5-4
	 * @param page 
	 */
	@Cacheable(value="allroles",key="#role.roleid+''+#role.rolename+'AuthPlugin.getRoles'+#page.pageIndex")
	public List<Role> getRoles(Role role, PageInfo page) {

		String sql = "select * from role r where 1=1 ";

		if (!FuncUtil.isEmpty(role.getRoleid())) {
			sql += " and r.roleid =" + role.getRoleid();
		}
		if (!FuncUtil.isEmpty(role.getRolename())) {
			sql += " and r.rolename ='" + role.getRolename() + "'";
		}
		// 总笔数
		int count = this.dao.count(sql, null);
		page.setItemCount(count);
		List<Role> roles = this.dao.searchPage(sql, Role.class,Integer.parseInt(page.getPageIndex()),Integer.parseInt(page.getPageSize()),null);
		if(roles==null||roles.size()==0){
			roles=Lists.newArrayList();
			roles.add(new Role());
		}
		return roles;
	}

	/**
	 * 功能：已选择的权限 2014-5-5
	 */
	@Cacheable(value="selectPermRole",key="#role.roleid")
	public List<Permission> getSelPermsByRole(Role role) {
		String sql = "select p.* from roleperm r ,permission p where p.permid=r.permid and r.roleid=?";
		List<Permission> roleperms = this.dao.search(sql, Permission.class,
				role.getRoleid());
		return roleperms;
	}

	/**
	 * 功能：未选中的权限 2014-5-5
	 */
	@Cacheable(value="UnselPermsByRole",key="#role.roleid")
	public List<Permission> getUnselPermsByRole(Role role) {
		String sql = "select p.* from permission p where p.permid not in(select r.permid from roleperm r,permission p  where r.roleid=? and p.permid=r.permid)";
		List<Permission> perms = this.dao.search(sql, Permission.class,
				role.getRoleid());
		return perms;
	}

	/**
	 * 功能：更新角色 2014-5-5
	 * @param unselpermids 
	 * 
	 * @param selperms
	 * 
	 * @throws AIPGException
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"allroles","selectPermRole","UnselPermsByRole","permOfUser"},  allEntries=true)
	public void modRole(Role role, String permids, String unselpermids, ModelMap model)
			throws AIPGException {
		try {
			Roleperm rperm;
			this.dao.update(role);
			List<Permission> selperms = getSelPermsByRole(role);
			for (Permission perm : selperms) {
				rperm = new Roleperm();
				rperm.setPermid(perm.getPermid());
				rperm.setRoleid(role.getRoleid());
				this.dao.delete(rperm);
			}
			if(permids==null)
				permids="";
			if(unselpermids==null)
				unselpermids="";
			String[] selectIds = permids.split(",");
			for (String permid : selectIds) {
				rperm = new Roleperm();
				rperm.setPermid(permid);
				rperm.setRoleid(role.getRoleid());
				this.dao.add(rperm);
			}
			String[] unSelectIds = unselpermids.split(",");
			for (String permid : unSelectIds) {
				rperm = new Roleperm();
				rperm.setPermid(permid);
				rperm.setRoleid(role.getRoleid());
				this.dao.delete(rperm);
			}
			model.addAttribute(CoreConstans.OPTRESULT, "更新角色成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "更新角色成功!");

		} catch (Exception ex) {
			log.error("更新角色失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "更新角色失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "更新角色失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "更新角色失败!");
		}

	}

	/**
	 * 功能：
	 * 2014-5-5
	 */
	public void batchImportAuth(List<Permission> perms) {
		// TODO Auto-generated method stub
	}

	/**
	 * 功能：添加新角色
	 * 2014-5-5
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"allroles"},  allEntries=true)
	public void addRole(Role role, String permids, ModelMap model) throws AIPGException {
		try {
			Roleperm rperm;
			Long roleid=this.getSeqOrc("RLPERMSEQ");
			role.setRoleid(String.valueOf(roleid));
			this.dao.add(role);
			if(permids==null)
				permids="";
			String[] selectIds = permids.split(",");
			for (String permid : selectIds) {
				rperm = new Roleperm();
				rperm.setPermid(permid);
				rperm.setRoleid(role.getRoleid());
				this.dao.add(rperm);
			}
			model.addAttribute(CoreConstans.OPTRESULT, "新增角色成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "新增角色成功!");
		} catch (Exception ex) {
			log.error("添加角色失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "添加角色失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "添加角色失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "添加角色失败!");
		}
	}

	/**
	 * 功能：删除角色
	 * 2014-5-5
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"allroles"},  allEntries=true)
	public void deleteRole(Role role, String permids, ModelMap model) throws AIPGException {
		try {
			String[] ids = role.getRoleid().split(",");
			for (String id : ids) {
				role.setRoleid(id);
				this.dao.delete(role);
				this.dao.delete("delete from roleperm r where r.roleid=?", role.getRoleid());
			}
			model.addAttribute(CoreConstans.OPTRESULT, "删除角色成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除角色成功!");
		}catch (Exception ex) {
			log.error("删除角色失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "删除角色失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除角色失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "删除角色失败!");
		}
	}

	/**
	 * 功能：角色信息
	 * 2014-5-7
	 */
	@Cacheable({"rolesFoMap"})
	public Map<String, String> getRolesFoMap() {
		String sql = "select t.roleid,t.rolename from role t";
		List<Object[]> roles = this.dao.searchForArray(sql, null);
		Map<String, String> rolsMap = Maps.newConcurrentMap();
		for (Object[] value : roles) {
			rolsMap.put(value[0].toString(), value[1].toString());
		}
		return rolsMap;
	}
	
	/**
	 * 功能：角色信息
	 * 2014-5-7
	 * @param userid 
	 */
	@Cacheable(value={"getroles"},key="#userid+'AuthPlugin.getRoles'")
	public List<RoleVo> getRoles(String userid) {
		String sql = "select t.roleid,t.rolename,u.userid from role t left join (select r.* from userrole r where r.userid=?) u on u.roleid=t.roleid";
		if(FuncUtil.isEmpty(userid)){
			userid="";
		}
		List<RoleVo>  roles = this.dao.search(sql,RoleVo.class, userid);
		return roles;
	}
}
