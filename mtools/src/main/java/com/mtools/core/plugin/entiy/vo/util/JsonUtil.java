package com.mtools.core.plugin.entiy.vo.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 针对easyui返回json数据的工具类
 */
public class JsonUtil {
	public static Map<String,Object> datagrid(Object obj,int total){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", String.valueOf(total));
		map.put("rows", obj);
		return map;
	}
	public static Map<String,Object> datagrid(String key,Object obj){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(key,obj);
		return map;
	}

}
