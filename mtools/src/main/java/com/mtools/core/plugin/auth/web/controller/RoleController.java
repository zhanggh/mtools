/**
 * 通联支付-研发中心
 * RoleController.java
 * 2014-4-30
 */
package com.mtools.core.plugin.auth.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mtools.core.plugin.annotation.AuthAccess;
import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.Role;
import com.mtools.core.plugin.helper.FuncUtil;

/**
 * @author zhang 功能：
 * @date 2014-4-30
 */
@Controller
public class RoleController extends BaseController {


	@RequestMapping(value = "/rolesearch", method = RequestMethod.GET)
	public String rolesearch(Role role, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {

		return "admin/sys/roles/main";
	}

	/**
	 * 功能：角色列表 2014-5-5
	 */
	@AuthAccess
	@RequestMapping(value = "/rolesearch/left", method = RequestMethod.GET)
	public String roleleft1(Role role, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {

		List<Role> roles = this.authPlugin.getRoles(role, this.page);
		model.addAttribute("roles", roles);

		return "admin/sys/roles/left";
	}

	/**
	 * 功能：角色列表 2014-5-5
	 */
	@AuthAccess
	@RequestMapping(value = "/rolesearch/left", headers = "table=true")
	public String roleleft2(Role role, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {

		List<Role> roles = this.authPlugin.getRoles(role, this.page);
		model.addAttribute("roles", roles);

		return "admin/sys/roles/listTable";
	}

	/**
	 * 功能：查看角色详情 2014-5-5
	 */
	@RequestMapping(value = { "/rolesearch/viewrole",
			"/rolesearch/left/viewrole" }, method = RequestMethod.GET)
	public String viewrole(Role role, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "查看");
		List<Role> roles = this.authPlugin.getRoles(role, this.page);
		model.addAttribute("role", roles.get(0));
		List<Permission> selperms = this.authPlugin.getSelPermsByRole(role);
		List<Permission> unselperms = this.authPlugin.getUnselPermsByRole(role);
		model.addAttribute("selperms", selperms);
		model.addAttribute("unselperms", unselperms);
		return "admin/sys/roles/editForm";
	}

	/**
	 * 功能：更新角色详情 2014-5-5
	 */
	@RequestMapping(value = { "/rolesearch/update", "/rolesearch/left/update" }, method = RequestMethod.GET)
	public String updaterole(Role role, ModelMap model, String flag,
			String permids, String id, String unselpermids,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "修改");
		try {
			if (!FuncUtil.isEmpty(id)) {
				role.setRoleid(id);
			}
			List<Role> roles = this.authPlugin.getRoles(role, this.page);
			model.addAttribute("role", roles.get(0));
			if ("1".equals(flag)) {
				this.authPlugin.modRole(role, permids, unselpermids, model);
			}
			List<Permission> selperms = this.authPlugin.getSelPermsByRole(role);
			List<Permission> unselperms = this.authPlugin
					.getUnselPermsByRole(role);
			model.addAttribute("selperms", selperms);
			model.addAttribute("unselperms", unselperms);
			return "admin/sys/roles/editForm";
		} catch (Exception ex) {
			return "admin/sys/roles/editForm";
		}

	}

	/**
	 * 功能：新增角色详情 2014-5-5
	 */
	@RequestMapping(value = { "/rolesearch/create", "/rolesearch/left/create" }, method = RequestMethod.GET)
	public String createrole(@ModelAttribute("role") Role role, ModelMap model,
			String flag, String permids, HttpSession session,
			HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "新增");
		if ("1".equals(flag)) {
			this.authPlugin.addRole(role, permids, model);
			List<Permission> selperms = this.authPlugin.getSelPermsByRole(role);
			model.addAttribute("selperms", selperms);
		}
		List<Permission> unselperms = this.authPlugin.getUnselPermsByRole(role);
		model.addAttribute("unselperms", unselperms);
		return "admin/sys/roles/editForm";
	}

	/**
	 * 功能：删除角色 2014-5-5
	 */
	@RequestMapping(value = { "/rolesearch/delete", "/rolesearch/left/delete" }, method = RequestMethod.GET)
	public String deleterole(@ModelAttribute("role") Role role, ModelMap model,
			String flag, String permids, String id, HttpSession session,
			HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "删除");
		 
		if (!FuncUtil.isEmpty(role.getRoleid())) {
			List<Role> roles = this.authPlugin.getRoles(role, this.page);
			model.addAttribute("role", roles.get(0));
			List<Permission> selperms = this.authPlugin.getSelPermsByRole(role);
			List<Permission> unselperms = this.authPlugin
					.getUnselPermsByRole(role);
			model.addAttribute("selperms", selperms);
			model.addAttribute("unselperms", unselperms);
			if ("1".equals(flag)) {
				this.authPlugin.deleteRole(role, permids, model);
			}
			return "admin/sys/roles/editForm";
		} else {
			if ("1".equals(flag)) {
				if (!FuncUtil.isEmpty(id)) {
					role.setRoleid(id);
					this.authPlugin.deleteRole(role, permids, model);
					//重定向
					return toView(request, model,"/rolesearch/left");
				}
			}
		}
		return "admin/sys/roles/listTable";
	}
}
