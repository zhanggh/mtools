/**
 * 通联支付-研发中心
 * DepartVo.java
 * 2014-5-6
 */
package com.mtools.core.plugin.entiy.vo;

import com.mtools.core.plugin.entity.Department;

/**
 * @author zhang
 *  功能：
 * @date 2014-5-6
 */
public class DepartVo extends Department {

	public String pdepname;

	/**
	 * @return the pdepname
	 */
	public String getPdepname() {
		return pdepname;
	}

	/**
	 * @param pdepname the pdepname to set
	 */
	public void setPdepname(String pdepname) {
		this.pdepname = pdepname;
	}
}
