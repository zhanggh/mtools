package com.mtools.core.plugin.entity;

public class Sequence {

	public static final String TABLE_ALIAS = "tb_sequence";
	public static final String TABLE_NAME = "tb_sequence";
	public static final String[] TABLE_KEYS = { "name" };
	
	public String name;
	public int currentValue;
	public int _increment;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}
	public int get_increment() {
		return _increment;
	}
	public void set_increment(int _increment) {
		this._increment = _increment;
	}
	
	
}
