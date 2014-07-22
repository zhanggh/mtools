/**
 * 通联支付-研发中心
 * AuthVo.java
 * 2014-4-30
 */
package com.mtools.core.plugin.entiy.vo;

import com.mtools.core.plugin.entity.Permission;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-30
 */
public class AuthVo extends Permission {

	public String menuname;

	/**
	 * @return the menuname
	 */
	public String getMenuname() {
		return menuname;
	}

	/**
	 * @param menuname the menuname to set
	 */
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	
	
}
