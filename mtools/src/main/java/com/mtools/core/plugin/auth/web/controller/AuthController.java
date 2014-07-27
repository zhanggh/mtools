/**
 * 通联支付-研发中心
 * AuthController.java
 * 2014-4-23
 */
package com.mtools.core.plugin.auth.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entiy.vo.AuthVo;

/**
 * @author zhang 功能：访问权限控制类
 * @date 2014-4-23
 */
@Controller
public class AuthController extends BaseController {

	/**
	 * 功能：查询权限列表 2014-4-23
	 */
	@RequestMapping(value = "/authsearch", method = RequestMethod.GET)
	public String authsearch(Permission perm, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {

		List<AuthVo> perms = this.authPlugin.searchPerm(perm,
				coreParams.serverName,this.page);
		model.addAttribute("perms", perms);

		return "admin/sys/auth/list";
	}

	/**
	 * 功能：查询权限列表 2014-4-23
	 */
	@RequestMapping(value = "/authsearch", headers = "table=true")
	public String authsearch2(Permission perm, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {

		List<AuthVo> perms = this.authPlugin.searchPerm(perm,
				coreParams.serverName,this.page);
		model.addAttribute("perms", perms);

		return "admin/sys/auth/listTable";
	}

	/**
	 * 功能：新增权限 2014-4-23
	 */
	@RequestMapping(value = "/authsearch/create")
	public String addauth(@ModelAttribute("perm")Permission perm, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "新增");
		if ("1".equals(flag)) {
			this.authPlugin.addAuth(perm, coreParams.serverName, model);
		}
		Map<String,String> menus =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menus);
		return "admin/sys/auth/editForm";
	}

	/**
	 * 功能：查看权限 2014-4-23
	 */
	@RequestMapping(value = "/authsearch/viewperm")
	public String viewPerm(Permission perm, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		
		model.addAttribute(CoreConstans.OP_NAME, "查看");
		List<AuthVo> perms = this.authPlugin.searchPerm(perm,
				coreParams.serverName,this.page);
		model.addAttribute("perm", perms.get(0));
		Map<String,String> menus =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menus);
		return "admin/sys/auth/editForm";
	}
	/**
	 * 功能：修改权限 2014-4-23
	 */
	@RequestMapping(value = "/authsearch/update")
	public String modAuth(Permission perm, ModelMap model, String flag, String id,
			HttpSession session, HttpServletRequest request) throws Exception {
		perm.setPermid(id);
		model.addAttribute(CoreConstans.OP_NAME, "修改");
		if ("1".equals(flag)) {
			this.authPlugin.modAuth(perm, model);
		}
		List<AuthVo> perms = this.authPlugin.searchPerm(perm,
				coreParams.serverName,this.page);
		model.addAttribute("perm", perms.get(0));
		Map<String,String> menus =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menus);
		return "admin/sys/auth/editForm";
	}
	/**
	 * 功能：删除权限 2014-4-23
	 */
	@RequestMapping(value = "/authsearch/delete")
	public String deleteAuth(Permission perm, ModelMap model, String flag,String id,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "删除");
		perm.setPermid(id);
		List<AuthVo> perms = this.authPlugin.searchPerm(perm,
				coreParams.serverName,this.page);
		model.addAttribute("perm", perms.get(0));
		if ("1".equals(flag)) {
			this.authPlugin.deleteAuth(perm, model);
		}
		return "admin/sys/auth/editForm";
	}

	/**
	 * 功能：
	 */
	@Override
	public void setCommonData(Model model) {
		// TODO Auto-generated method stub

	}
}
