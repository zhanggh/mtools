/**
 * RespMsg.java
 * 2014-4-14
 */
package com.mtools.core.plugin.entity;

/**
 * @author zhang
 * 
 *         2014-4-14
 */
public class RespMsg  extends BaseDbStruct{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933955674830484187L;
	private String code;
	private String message;
	private String type;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
