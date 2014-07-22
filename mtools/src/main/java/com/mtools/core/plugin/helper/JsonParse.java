package com.mtools.core.plugin.helper;

import com.alibaba.fastjson.JSON;

 
public class JsonParse  {
	
	public static Object fromObject(String jsonString,Class<?> c) {
		 
		try {
			Object obj=JSON.parseObject(jsonString,c);
			return obj;
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		} finally {
		}
		return null;
	}
	
	public static String obj2json(Object obj){
		String jsonString = JSON.toJSONString(obj);
		return jsonString;
	}

}
