/**
 * 通联支付-研发中心
 * RoleServiceImpl.java
 * 2014-4-29
 */
package com.mtools.core.plugin.auth.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.auth.service.RoleService;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.Role;
import com.mtools.core.plugin.entiy.vo.UserVo;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-29
 */
@Service("roleService")
public class RoleServiceImpl extends BasePlugin implements RoleService {

	/**  
	 * 功能：
	 */
	
	public List<Role> getRoles(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	/**  
	 * 功能：
	 * @throws Exception 
	 */
	
	public void setUserRole(UserVo user) throws Exception {
		String sql="select ur.userid, r.rolename,r.roleid from userrole ur, role r where r.roleid = ur.roleid and ur.userid=?";
		List<Role> roles=this.dao.search(sql, Role.class, user.getUserid());
		
		StringBuffer sb=new StringBuffer();
		int index=0;
		for(Role role:roles){
			if(index%2==0&&index!=0)
				sb.append("<br>");
			index++;
			sb.append(role.getRolename()).append(" ");
		}
		user.setRolename(sb.toString());
		user.setRoles(roles);
	}

	/**  
	 * 功能：
	 */
	
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public int addRole(Role role, Permission perm) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**  
	 * 功能：
	 */
	
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public int modRole(Role role, Permission perm) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**  
	 * 功能：
	 */
	
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public int deleteRole(Role role, Permission perm) {
		// TODO Auto-generated method stub
		return 0;
	}

}
