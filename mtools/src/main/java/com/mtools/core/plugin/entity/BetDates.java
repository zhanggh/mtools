package com.mtools.core.plugin.entity;

import java.sql.Timestamp;

public class BetDates {

	/**
	 * 时间段字段类型是否为字符串
	 */
	private boolean isStr=false;
	/**
	 * 数据库中的时间字段类型是否为字符串
	 */
	private boolean isDbTimeStr=false;
	
	private String startDateTimeStr;
	private String endDateTimeStr;
	private Timestamp startDateTime;
	private Timestamp endDateTime;
	public String getStartDateTimeStr() {
		return startDateTimeStr;
	}
	public void setStartDateTimeStr(String startDateTimeStr) {
		this.startDateTimeStr = startDateTimeStr;
	}
	public String getEndDateTimeStr() {
		return endDateTimeStr;
	}
	public void setEndDateTimeStr(String endDateTimeStr) {
		this.endDateTimeStr = endDateTimeStr;
	}
	public Timestamp getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Timestamp getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Timestamp endDateTime) {
		this.endDateTime = endDateTime;
	}
	public boolean isStr() {
		return isStr;
	}
	public void setStr(boolean isStr) {
		this.isStr = isStr;
	}
	public boolean isDbTimeStr() {
		return isDbTimeStr;
	}
	public void setDbTimeStr(boolean isDbTimeStr) {
		this.isDbTimeStr = isDbTimeStr;
	}
}
