/**
 * 通联支付-研发中心
 * MenuController.java
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

import com.mtools.core.plugin.annotation.AuthAccess;
import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.BooleanEnum;
import com.mtools.core.plugin.entity.MenuInfo;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Sex;
import com.mtools.core.plugin.helper.AIPGException;

/**
 * @author zhang 功能：系统菜单管理控制类
 * @date 2014-4-23
 */
@Controller
public class MenuController extends BaseController {

	/**
	 * 功能：菜单列表 2014-4-23
	 */
	@RequestMapping(value = "/menusearch", headers = "table=true")
	public String menusearch(MenuInfo menu, ModelMap model, String flag,PageInfo page,
			String sortid, HttpSession session, HttpServletRequest request)
			throws Exception {
		menu.setMenutype(coreParams.serverName);
		List<MenuInfo> menus = this.menuPlugin.searchMenu(menu,this.page);
		model.addAttribute("menus", menus);

		return "admin/sys/menus/listTable";
	}

	@RequestMapping(value = "/menusearch", method = RequestMethod.GET)
	public String menusearch2(MenuInfo menu, ModelMap model, String flag,PageInfo page,
			HttpSession session, HttpServletRequest request) throws Exception {
		menu.setMenutype(coreParams.serverName);
		List<MenuInfo> menus = this.menuPlugin.searchMenu(menu,this.page);
		model.addAttribute("menus", menus);

		return "admin/sys/menus/list";
	}

	@AuthAccess
	@RequestMapping(value = "/menusearch/viewMenu", method = RequestMethod.GET)
	public String viewMenu(MenuInfo menu, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "查看");
		menu.setMenutype(coreParams.serverName);
		List<MenuInfo> menus = this.menuPlugin.searchMenu(menu, this.page);
		model.addAttribute("menu", menus.get(0));

		Map<String,String> menusMap =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menusMap);
		return "admin/sys/menus/editForm";
	}

	 
	@RequestMapping(value = "/menusearch/update")
	public String updateMenu(MenuInfo menu, ModelMap model, String flag,String id,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "修改");
		menu.setMenuid(id);
		if ("1".equals(flag)) {
			this.menuPlugin.modmenu(menu,model);
		}
		menu.setMenutype(coreParams.serverName);
		List<MenuInfo> menus = this.menuPlugin.searchMenu(menu,this.page);
		model.addAttribute("menu", menus.get(0));
		Map<String,String> menusMap =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menusMap);
		return "admin/sys/menus/editForm";
	}

	 
	@RequestMapping(value = "/menusearch/delete")
	public String deleteMenu(MenuInfo menu, ModelMap model, String flag,String id,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "删除");
		if ("1".equals(flag)) {
			try {
				menu.setMenuid(id);
				this.menuPlugin.deleteMenu(menu, model);
				return "redirect:/menusearch";
			} catch (AIPGException e) {
				return "admin/sys/menus/editForm";
			}
			
		}else{
			menu.setMenutype(coreParams.serverName);
			List<MenuInfo> menus = this.menuPlugin.searchMenu(menu,this.page);
			model.addAttribute("menu", menus.get(0));
			Map<String,String> menusMap =this.menuPlugin.getMenuFoMap();
			model.addAttribute("menus", menusMap);
		}
		return "admin/sys/menus/editForm";
	}
 
	@RequestMapping(value = "/menusearch/create")
	public String createMenu(@ModelAttribute("menu") MenuInfo menu, ModelMap model, String flag,
			HttpSession session, HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "新增");
		
		if ("1".equals(flag)) {
			menu.setMenutype(this.menuType);
			this.menuPlugin.addmenu(menu, model);;
		}
		Map<String,String> menus =this.menuPlugin.getMenuFoMap();
		model.addAttribute("menus", menus);
		return "admin/sys/menus/editForm";
	}
	

	public void setCommonData(Model model) {
		model.addAttribute("sexList", Sex.values());
		model.addAttribute("booleanList", BooleanEnum.values());
	}
}
