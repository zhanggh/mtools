/**
 * 通联支付-研发中心
 * Sort.java
 * 2014-4-25
 */
package com.mtools.core.plugin.entity;


/**
 * @author zhang
 *  功能：
 * @date 2014-4-25
 */
public class Sort  extends BaseDbStruct{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5633653779844823353L;
	public String id;
	public String name;
	public String  creattime;
	public String  ascOrDes="desc";//升序或者降序
	
	
	
	public String getAscOrDes() {
		return ascOrDes;
	}
	public void setAscOrDes(String ascOrDes) {
		this.ascOrDes = ascOrDes;
	}
	public String getCreattime() {
		return creattime;
	}
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
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
