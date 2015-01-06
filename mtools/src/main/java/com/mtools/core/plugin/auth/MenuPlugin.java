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
import com.mtools.core.plugin.db.DBSqlCreater;
import com.mtools.core.plugin.entity.MenuInfo;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.SqlParam;
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
	 * @throws Exception 
	 */
	@Cacheable(value={"getMenus"},key="#userid+'MenuPlugin.getMenus'+#menutype")
	public List<MenuInfo> getUserMenus(String userid,String menutype) throws Exception {
		boolean contain=false;
		String sql ="select t.* from menuinfo t  where t.menuid in(select distinct (p.menuid)"
					+" from permission p ,roleperm r, USERROLE U "
					+" where p.permid = r.permid"
					+" and u.roleid = r.roleid"
					+" and p.permtype =?"
					+" and u.userid =?)";
		List<MenuInfo> menus = this.dao.search(sql, MenuInfo.class,menutype,userid);
		List<MenuInfo> temMs=null;
		List<MenuInfo> retMenus=Lists.newArrayList();
		for(MenuInfo menu:menus){
			temMs=this.dao.search("select * from menuinfo m start with m.menuid=? connect by prior m.parentid=m.menuid", MenuInfo.class, menu.getMenuid());
			if(temMs!=null){
				for(MenuInfo tmenu:temMs){
					for(MenuInfo tmu:retMenus){
						if(tmu.getMenuid().equals(tmenu.getMenuid())){
							contain=true;
							break;
						}
					}
					if(!contain){
						retMenus.add(tmenu);
					}else{
						contain=false;
					}
				}
			}
		}
		menus.clear();
		List<MenuInfo> parantMenu = Lists.newArrayList();
		for (MenuInfo menu : retMenus) {
			if (menu.getParentid() == null||"0".equals(menu.getParentid())) {
				parantMenu.add(menu);
			}
		}
		for(MenuInfo menu : parantMenu){
			retMenus.remove(menu);
		}
		getPackgMenus(retMenus, parantMenu);
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
	 * @throws Exception 
	 */
	@Cacheable(value={"searchMenu"},key="#menu.menuid+''+#menu.menuname+'MenuPlugin.searchMenu'+#menutype+''+#page.pageIndex+''+#page.pageSize")
	public List<MenuInfo> searchMenu(MenuInfo menu,
			PageInfo page) throws Exception {
		DBSqlCreater<MenuInfo> dbSqlCreater=new DBSqlCreater<MenuInfo>();
		dbSqlCreater.addTableObj(menu);
		//增加条件
		dbSqlCreater.addLiketField("menuname");
		if (!FuncUtil.isEmpty(page.getSort().getId())) {
			dbSqlCreater.setOrderbyField("menuid", page.getSort().getId());
		}
		if (!FuncUtil.isEmpty(page.getSort().getName())) {
			dbSqlCreater.setOrderbyField("menuname", page.getSort().getName());
		}
		//构建where语句
		SqlParam sqlParam = dbSqlCreater.buildWhereSql(menu);
		
		// 总笔数
		int count = this.dao.count(dbSqlCreater.buildCountSql()+sqlParam.getSql(),sqlParam.getParams().toArray());
		page.setItemCount(count);
		
		List<MenuInfo> menus = this.dao.searchPage(dbSqlCreater.buildSelect()+sqlParam.getSql(), MenuInfo.class,
				Integer.parseInt(page.getPageIndex()),
				Integer.parseInt(page.getPageSize()),sqlParam.getParams().toArray());
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
	public Map<String, String> getMenuFoMap() throws Exception {
		String sql = "select t.menuid,t.menuname from menuinfo t";
		List<Object[]> menus = this.dao.searchForArray(sql, null);
		Map<String, String> menusMap = Maps.newConcurrentMap();
		for (Object[] value : menus) {
			menusMap.put(value[0].toString(), value[1].toString());
		}
		return menusMap;
	}
}
