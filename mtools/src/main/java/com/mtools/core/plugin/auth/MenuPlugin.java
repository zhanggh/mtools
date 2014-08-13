/**
 * MenuPlugin.java
 * 2014-4-14
 */
package com.mtools.core.plugin.auth;

import java.util.List;
import java.util.Map;

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
import com.mtools.core.plugin.entity.MenuInfo;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.XStreamIg;

/**
 * @author zhang 菜单管理插件 2014-4-14
 */
@Component("menuPlugin")
public class MenuPlugin extends BasePlugin {

	/**
	 * 功能：根据用户号查询该用户的所有菜单 2014-4-14
	 */
	@Cacheable(value={"getMenus"},key="#userid+'MenuPlugin.getMenus'+#menutype")
	public List<MenuInfo> getMenus(String userid,String menutype) {
		String sql = "select * from menuinfo t where t.menutype=?";
		List<MenuInfo> menus = this.dao.search(sql, MenuInfo.class, menutype);
		List<MenuInfo> parantMenu = Lists.newArrayList();
		for (MenuInfo menu : menus) {
			if (menu.getParentid() == null||"0".equals(menu.getParentid())) {
				parantMenu.add(menu);
			}
		}
		for(MenuInfo menu : parantMenu){
			menus.remove(menu);
		}
		getPackgMenus(menus, parantMenu);
//		log.info(XStreamIg.toXml(parantMenu));
		return parantMenu;
	}

	/**
	 * 功能：建立菜单树形结构 2014-4-23
	 */
	private void getPackgMenus(List<MenuInfo> menus, List<MenuInfo> parantMenus) {
		for (MenuInfo pmenu : parantMenus) {
			if (pmenu.getChildren() == null) {
				List<MenuInfo> childmenus = Lists.newArrayList();
				pmenu.setChildren(childmenus);
			}
			for (MenuInfo childMenu : menus) {
				if (pmenu.getMenuid().equals(childMenu.getParentid())) {
					pmenu.getChildren().add(childMenu);
				}
			}
			for(MenuInfo menu : pmenu.getChildren()){
				menus.remove(menu);
			}
			getPackgMenus(menus, pmenu.getChildren());
		}
	}

	/**
	 * 功能：菜单列表 2014-4-23
	 * 
	 * @param page
	 */
	@Cacheable(value={"searchMenu"},key="#menu.menuid+''+#menu.name+'MenuPlugin.searchMenu'+#menutype+''+#page.pageIndex+''+#page.pageSize")
	public List<MenuInfo> searchMenu(MenuInfo menu, String menutype,
			PageInfo page) {
		String sql = "select * from menuinfo t where t.menutype=? ";
		if (!FuncUtil.isEmpty(menu.getMenuname())) {
			sql += " and t.menuname like '%" + menu.getMenuname() + "%'";
		}
		if (!FuncUtil.isEmpty(menu.getMenuid())) {
			sql += "and t.menuid = " + menu.getMenuid();
		}
		// 总笔数
		int count = this.dao.count(sql, menutype);
		page.setItemCount(count);
		if (!FuncUtil.isEmpty(page.getSort().getId())) {
			sql += " order by menuid " + page.getSort().getId();
		}
		if (!FuncUtil.isEmpty(page.getSort().getName())) {
			sql += " order by menuname " + page.getSort().getName();
		}
		List<MenuInfo> menus = this.dao.searchPage(sql, MenuInfo.class,
				Integer.parseInt(page.getPageIndex()),
				Integer.parseInt(page.getPageSize()), menutype);
		if (menus == null)
			menus = Lists.newArrayList();
		return menus;
	}

	/**
	 * 功能：新增菜单 2014-4-23
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"searchMenu","getMenus","getMenuFoMap","menuCache"},  allEntries=true)
	public void addmenu(MenuInfo menu, ModelMap model) throws AIPGException {
		try {

			if (this.dao.add(menu) > 0) {
				model.addAttribute(CoreConstans.OPTRESULT, "新增菜单成功");
				model.put(CoreConstans.SUCCESSMESSAGE, "新增菜单成功!");
			} else {
				model.addAttribute(CoreConstans.OPTRESULT, "新增菜单失败");
				model.put(CoreConstans.SUCCESSMESSAGE, "新增菜单失败!");
			}
		} catch (Exception ex) {
			log.error("新增菜单失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "新增菜单失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "新增菜单失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "修改菜单失败!");
		}

	}

	/**
	 * 功能：修改菜单 2014-4-23
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"searchMenu","getMenus","getMenuFoMap","menuCache"},  allEntries=true)
	public void modmenu(MenuInfo menu, ModelMap model) throws AIPGException {
		try {
			if (this.dao.update(menu) > 0) {
				model.addAttribute(CoreConstans.OPTRESULT, "修改菜单成功");
				model.put(CoreConstans.SUCCESSMESSAGE, "修改菜单成功!");
			} else {
				model.addAttribute(CoreConstans.OPTRESULT, "修改菜单失败");
				model.put(CoreConstans.SUCCESSMESSAGE, "修改菜单失败!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("修改菜单失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "修改菜单失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "修改菜单失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "修改菜单失败!");
		}

	}

	/**
	 * 功能：删除菜单 2014-4-23
	 * @throws AIPGException 
	 */
	@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	@CacheEvict(value={"searchMenu","getMenus","getMenuFoMap","menuCache"},  allEntries=true)
	public void deleteMenu(MenuInfo menu, ModelMap model) throws AIPGException {
		try {
			String[] ids = menu.getMenuid().split(",");
			for (String id : ids) {
				menu.setMenuid(id);
				this.dao.delete(menu);
			}
			model.addAttribute(CoreConstans.OPTRESULT, "删除菜单成功");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除菜单成功!");
		} catch (Exception ex) {
			log.error("删除菜单失败\n" + ex.getMessage(), ex);
			model.addAttribute(CoreConstans.OPTRESULT, "删除菜单失败");
			model.put(CoreConstans.SUCCESSMESSAGE, "删除菜单失败!");
			AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "删除菜单失败!");
		}
	}

	@Cacheable(value={"getMenuFoMap"})
	public Map<String, String> getMenuFoMap() {
		String sql = "select t.menuid,t.name from menuinfo t";
		List<Object[]> menus = this.dao.searchForArray(sql, null);
		Map<String, String> menusMap = Maps.newConcurrentMap();
		for (Object[] value : menus) {
			menusMap.put(value[0].toString(), value[1].toString());
		}
		return menusMap;
	}
}
