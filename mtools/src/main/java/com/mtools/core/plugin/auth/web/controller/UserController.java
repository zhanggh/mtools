/**
 * UserController.java
 * 2014-4-16
 */
package com.mtools.core.plugin.auth.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.mtools.core.plugin.annotation.AuthAccess;
import com.mtools.core.plugin.annotation.AuthLogin;
import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.RoleVo;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.entity.UserRole;
import com.mtools.core.plugin.entiy.vo.UserVo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.CookieUtil;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.JsonParse;
import com.mtools.core.plugin.security.Crypto;

/**
 * @author zhang
 * 
 *         2014-4-16
 */
@Controller
public class UserController extends BaseController {

	@SuppressWarnings("unchecked")
	@AuthAccess
	@AuthLogin
	@RequestMapping("/login")
	public String login(ModelMap model, UserInfo user, String flag,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgUri = null;
		if ("1".equals(flag)) {
			UserInfo us = this.userSv.getUserInfo(user);
			if (us != null) {
				List<UserInfo> onlineUser = null;
				onlineUser = (List<UserInfo>) session.getServletContext()
						.getAttribute(CoreConstans.ONLINEUSERS);
				if (onlineUser == null) {
					onlineUser = Lists.newArrayList();
				}
				us.setFromIp(request.getRemoteHost());
				us.setLoginTime(FuncUtil.formatTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
				onlineUser.add(us);
				session.getServletContext().setAttribute(
						CoreConstans.ONLINEUSERS, onlineUser);
				List<Permission> perms = authPlugin.getPermOfUser(
						us.getUserid(), coreParams.serverName);
				us.setPerms(perms);
				session.setAttribute(CoreConstans.LOGINGUSER, us);
				request.setAttribute(CoreConstans.OPTRESULT, "1");// 操作成功
				request.setAttribute(CoreConstans.ORGPARAMS,
						JsonParse.obj2json(user));// 操作成功
				log.info("org data:" + JsonParse.obj2json(user));
				orgUri = CookieUtil.getCookie(request, CoreConstans.REQESTURI);
			} else {
				model.put(CoreConstans.ERROR_MESSAGE, "用户名或者密码错误，请检查!");
			}
		}
		if (this.getUser(session) == null)
			return "front/login";
		else if (!FuncUtil.isEmpty(orgUri)) {
			String uri = orgUri.substring(1);
			CookieUtil.setCookie(response, CoreConstans.REQESTURI, "");
			return "redirect:" + uri;
		} else
			return "redirect:admin/index";
	}

	@SuppressWarnings("unchecked")
	@AuthLogin
	@AuthAccess
	@RequestMapping("logout")
	public String logout(ModelMap m, HttpSession session,
			HttpServletRequest request) throws Exception {
		UserInfo user = this.getUser(session);
		List<UserInfo> onlineUser = null;
		onlineUser = (List<UserInfo>) session.getServletContext().getAttribute(
				CoreConstans.ONLINEUSERS);
		if (onlineUser == null) {
			log.debug("servletContext的List<UserInfo>为空？请系统管理员检查！");
		} else {
			onlineUser.remove(user);
		}
		session.removeAttribute(CoreConstans.LOGINGUSER);
		return "front/login";
	}

	@RequestMapping("viewInfo")
	public String viewInfo(UserInfo user, ModelMap model, HttpSession session,
			HttpServletRequest request) throws Exception {
		UserVo userVo = userSv.getUserVo(this.getUser(session));
		model.addAttribute(CoreConstans.OP_NAME, "查看个人资料");
		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		model.addAttribute("userVo", userVo);
		// log.info(XStreamIg.toXml(this.getUser(session)));
		return "admin/sys/user/loginUser/editForm";
	}

	@RequestMapping(value = "/updateInfo")
	public String updateInfo(UserInfo user, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		if ("1".equals(flag)) {
			userSv.upateStmUser(user, null, model);
		}
		UserVo userVo = userSv.getUserVo(this.getUser(session));
		model.addAttribute(CoreConstans.OP_NAME, "修改个人资料");
		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		model.addAttribute("userVo", userVo);

		return "admin/sys/user/loginUser/editForm";
	}

	@RequestMapping(value = "/changePassword")
	public String changePassword(UserInfo user, String flag,
			String oldPassword, String newPassword1, String newPassword2,
			HttpSession session, HttpServletRequest request, ModelMap model)
			throws Exception {

		model.addAttribute(CoreConstans.OP_NAME, "修改密码");
		model.addAttribute("userVo", this.getUser(session));
		if ("1".equals(flag)) {
			if (StringUtils.isEmpty(newPassword1)
					|| StringUtils.isEmpty(newPassword2)) {
				model.addAttribute(CoreConstans.ERROR_MESSAGE, "必须输入新密码");
			} else if (!newPassword1.equals(newPassword2)) {
				model.addAttribute(CoreConstans.ERROR_MESSAGE, "两次输入的密码不一致");
			} else {
				String pwd = Crypto.encode(newPassword2);
				user.setPassword(pwd);
				userSv.upateStmUser(user, null, model);
			}

		}
		return "admin/sys/user/loginUser/changePasswordForm";
	}

	/**
	 * 功能：查询用户列表 2014-4-29
	 */
	@RequestMapping(value = "usersearch", method = RequestMethod.GET)
	public String usersearch(UserVo user, ModelMap model, HttpSession session,
			PageInfo page, HttpServletRequest request) throws Exception {
		List<UserVo> userVos = userSv.getUserVos(user, this.page);
		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		model.addAttribute("userVos", userVos);
		return "admin/sys/user/list";
	}

	/**
	 * 功能：查询用户列表 2014-4-29
	 */
	@RequestMapping(value = "/usersearch", headers = "table=true")
	public String usersearch2(UserVo user, ModelMap model, HttpSession session,
			PageInfo page, HttpServletRequest request) throws Exception {
		List<UserVo> userVos = userSv.getUserVos(user, this.page);
		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		model.addAttribute("userVos", userVos);
		return "admin/sys/user/listTable";
	}

	/**
	 * 功能：新增用户 2014-4-29
	 * @throws Exception 
	 */
	@RequestMapping(value = "/usersearch/create")
	public String createUser(@ModelAttribute("user") UserInfo user,
			UserRole urole, ModelMap model, HttpSession session,
			HttpServletRequest request, String flag, String id) throws Exception {
		try {
			model.addAttribute(CoreConstans.OP_NAME, "新增");
			if ("1".equals(flag)) {
				this.userSv.addUser(user, urole, model);
			}
			// 部门
			Map<String, String> depM = this.sysPlugin.getDepsFoMap();
			model.addAttribute("depmap", depM);
			// 角色
			List<RoleVo> roles = this.authPlugin.getRoles(user.getUserid());
			model.addAttribute("roles", roles);

		} catch (AIPGException e) {
			log.error("新增用户失败");
		}
		return "admin/sys/user/editForm";
	}

	/**
	 * 功能：查看用户详情 2014-4-29
	 */
	@RequestMapping(value = "/usersearch/viewuser")
	public String viewuser(UserInfo user, ModelMap model, UserRole urole,
			HttpSession session, HttpServletRequest request, String flag,
			String id) {
		try {
			model.addAttribute(CoreConstans.OP_NAME, "查看");
			UserVo usr = userSv.getUserVo(user);
			model.addAttribute("user", usr);
			// 部门
			Map<String, String> depM = this.sysPlugin.getDepsFoMap();
			model.addAttribute("depmap", depM);
			// 角色
			List<RoleVo> roles = this.authPlugin.getRoles(user.getUserid());
			model.addAttribute("roles", roles);
		} catch (Exception e) {
			log.error("修改用户失败");
		}

		return "admin/sys/user/editForm";
	}

	/**
	 * 功能：更新用户 2014-4-29
	 */
	@RequestMapping(value = "/usersearch/update")
	public String updateUser(UserInfo user, ModelMap model, UserRole urole,
			HttpSession session, HttpServletRequest request, String flag,
			String id) {
		try {
			if (!FuncUtil.isEmpty(id)) {
				user.setUserid(id);
			}
			model.addAttribute(CoreConstans.OP_NAME, "修改");
			if ("1".equals(flag)) {
				userSv.upateStmUser(user, urole, model);
			}
			UserVo usr = userSv.getUserVo(user);
			model.addAttribute("user", usr);
			// 部门
			Map<String, String> depM = this.sysPlugin.getDepsFoMap();
			model.addAttribute("depmap", depM);
			// 角色
			List<RoleVo> roles = this.authPlugin.getRoles(user.getUserid());
			model.addAttribute("roles", roles);
		} catch (Exception e) {
			log.error("修改用户失败");
		}

		return "admin/sys/user/editForm";
	}

	/**
	 * 功能：删除用户 2014-4-29
	 * @throws Exception 
	 */
	@RequestMapping(value = "/usersearch/delete")
	public String deleteUser(UserInfo user, ModelMap model,
			HttpSession session, HttpServletRequest request, String flag,
			String id) throws Exception {
		try {
			model.addAttribute(CoreConstans.OP_NAME, "删除");
			// 部门
			Map<String, String> depM = this.sysPlugin.getDepsFoMap();
			model.addAttribute("depmap", depM);
			if (!FuncUtil.isEmpty(user.getUserid())) {
				user.setUserid(user.getUserid().split(",")[0]);
				// 角色
				List<RoleVo> roles = this.authPlugin.getRoles(user.getUserid());
				model.addAttribute("roles", roles);
				UserVo usr = userSv.getUserVo(user);
				model.addAttribute("user", usr);
				if ("1".equals(flag)) {
					userSv.deletStmUser(user, model);
					return "admin/sys/user/editForm";
				}
			} else {
				if ("1".equals(flag)) {
					user.setUserid(id);
					userSv.deletStmUser(user, model);
					return this.toView(request, model, "/usersearch");
				}
			}
		} catch (AIPGException e) {
			log.error("删除用户失败");
			return "admin/sys/user/list";
		}
		return "admin/sys/user/editForm";
	}

	/**
	 * 功能：在线用户监控
	 * 2014-7-23
	 */
	@SuppressWarnings({"unchecked" })
	@RequestMapping(value = "online/monitor ", method = RequestMethod.GET)
	public String OnlMonitor(UserVo user, ModelMap model,
			HttpSession session, PageInfo page, HttpServletRequest request) {

		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		List<UserInfo> onlineUser = null;
		onlineUser = (List<UserInfo>) session.getServletContext()
				.getAttribute(CoreConstans.ONLINEUSERS);
		int start= Integer.parseInt(this.page.getPageIndex())-1;
		int size = Integer.parseInt(this.page.getPageSize());
		if(onlineUser.size()<size)
			size=onlineUser.size();
		this.page.setItemCount(onlineUser.size());
		model.addAttribute("userVos", onlineUser.subList(start*size, start*size+size));
		return "admin/sys/online/list";
	}
	/**
	 * 功能：在线用户监控
	 * 2014-7-23
	 */
	@SuppressWarnings({"unchecked" })
	@RequestMapping(value = "online/monitor ", headers = "table=true")
	public String asyOnlMonitor(UserVo user, ModelMap model,
			HttpSession session, PageInfo page, HttpServletRequest request) {
		
		request.setAttribute(CoreConstans.OPTRESULT, "succ");
		List<UserInfo> onlineUser = null;
		List<UserInfo> onlineUser2 = Lists.newArrayList();
		
		onlineUser = (List<UserInfo>) session.getServletContext()
		.getAttribute(CoreConstans.ONLINEUSERS);
		
		for(UserInfo us:onlineUser){
			if(!FuncUtil.isEmpty(user.getUserid())&&!FuncUtil.isEmpty(user.getUsername())){
				if(us.getUserid().equals(user.getUserid())&&us.getUsername().indexOf(user.getUsername())>=0){
					onlineUser2.add(us);
				}
			}else if(!FuncUtil.isEmpty(user.getUserid())&&FuncUtil.isEmpty(user.getUsername())){
				if(us.getUserid().equals(user.getUserid())){
					onlineUser2.add(us);
				}
			}else if(FuncUtil.isEmpty(user.getUserid())&&!FuncUtil.isEmpty(user.getUsername())){
				if(us.getUsername().equals(user.getUsername())){
					onlineUser2.add(us);
				}
			}
		}
		if(onlineUser2.size()==0)
			onlineUser2=onlineUser;
		
		int start= Integer.parseInt(this.page.getPageIndex())-1;
		int size = Integer.parseInt(this.page.getPageSize());
		if(onlineUser2.size()<size)
			size=onlineUser2.size();
		this.page.setItemCount(onlineUser2.size());
		model.addAttribute("userVos", onlineUser2.subList(start*size, start*size+size));
		return "admin/sys/online/listTable";
	}
	/**
	 * 功能：强制下线
	 * 2014-7-23
	 */
	
	@SuppressWarnings({"unchecked" })
	@RequestMapping(value = "online/monitor/update", method = RequestMethod.GET)
	public String forceOffLine(UserVo user, ModelMap model,String id,
			HttpSession session, PageInfo page, HttpServletRequest request) {
		UserInfo usr=null;
		List<UserInfo> onlineUser = null;
		onlineUser = (List<UserInfo>) session.getServletContext()
				.getAttribute(CoreConstans.ONLINEUSERS);
		for(UserInfo us:onlineUser){
			if(us.getUserid().equals(id)){
				usr=us;
				break;
			}
		}
		((List<UserInfo>) session.getServletContext().getAttribute(CoreConstans.ONLINEUSERS)).remove(usr);
		request.setAttribute(CoreConstans.ERROR_MESSAGE,"force user offline success!");
    	log.debug("force user offline success");
    	return "front/msgdialog";
	}
}
