/**
 * 通联支付-研发中心
 * Sort.java
 * 2014-4-25
 */
package com.mtools.core.plugin.entity;

import java.io.Serializable;

/**
 * @author zhang
 *  功能：
 * @date 2014-4-25
 */
public class Sort implements Serializable{

	public String id;
	public String name;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
