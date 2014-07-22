/**
 * JsonMsg.java
 * 2014-4-17
 */
package com.mtools.core.plugin.entiy.vo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author zhang
 *	向前端返回json格式的信息
 * 2014-4-17
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class JsonMsg<T> {

	public String code="1";//1成功，非1失败
	public String message="成功";
	public String type;
	public T items;//返回详细信息
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the items
	 */
	public T getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(T items) {
		this.items = items;
	}
}
