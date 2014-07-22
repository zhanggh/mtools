package com.mtools.core.plugin.entity;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;


public class MenuInfo implements Serializable{

	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4343797726853346163L;
	public static final String TABLE_ALIAS = "Menuinfo";
	public static final String TABLE_NAME = "MENUINFO";
	public static final String[] TABLE_KEYS = { "MENUID" };
	private String menuid;
	private String parentid;
	private String name;
	private String help;
	private String linkurl;
	private long ordernum;
	private String menutype;

	/**
	 * @return the menutype
	 */
	public String getMenutype() {
		return menutype;
	}

	/**
	 * @param menutype the menutype to set
	 */
	public void setMenutype(String menutype) {
		this.menutype = menutype;
	}

	private List<MenuInfo> children;
	
	
	/**
	 * @return the children
	 */
	public List<MenuInfo> getChildren() {
		
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MenuInfo> children) {
		this.children = children;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public long getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(long ordernum) {
		this.ordernum = ordernum;
	}
	
	 /**
     * @return
     */
    public boolean isHasChildren() {
        return !(this.getChildren()==null||this.getChildren().isEmpty());
    }

}
