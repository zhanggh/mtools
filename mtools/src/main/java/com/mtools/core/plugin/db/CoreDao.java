package com.mtools.core.plugin.db;
 
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.helper.AIPGException;

//
@SuppressWarnings("unchecked")

 
public class CoreDao {
	private static Map tableNameMap = new HashMap();
	private static Map tableKeysMap = new HashMap();
	static Log log = LogFactory.getLog(CoreDao.class);
	public CoreDao() {
		super();
		DBUtil.isOrcl=false;//非Oracle数据库
		log.info("start dao...");
	}
	private JdbcTemplate dbop;

	public JdbcTemplate getDbop() {
		return dbop;
	}

	public void setDbop(JdbcTemplate dbop) {
		this.dbop = dbop;
	}
	
	public <T> List<T> search(String sql,Class<T> clz,Object...args){
		return DBUtil.getList(dbop, sql, clz, args);
	}
	public <T> List<T> searchSimp(Class<T> clz,int start,int size,int flag,Object startObj,Object endObj,String... ranges) throws AIPGException
	{
		return DBUtil.search(true, dbop, getTableNameEx(clz), start, size, flag, startObj, endObj, clz, ranges);
	}
	public <T> long countSimp(Class<T> clz,int flag,Object startObj,Object endObj,String... ranges) throws AIPGException
	{
		return DBUtil.count(true, dbop, getTableNameEx(clz), flag, startObj, endObj, clz, ranges);
	}
	
	public <T> List<T> searchPage(String sql,Class<T> clz,int start,int size,Object...args){
		return DBUtil.getPage(dbop, sql, clz, (start-1)*size, size, args);
	}
	
	public List<Object[]> searchForArray(String sql,Object...args)
	{
		return DBUtil.getListForArray(dbop, sql, args);
	}
	
	public List<Object[]> searchForArrayPage(String sql,int start,int size,Object...args){
		return DBUtil.getListForArrayPage(dbop, sql,start,size, args);
	}
	
	public List<Map<String,Object>> searchForMap(String sql,Object...args){
		return DBUtil.getListForMap(dbop, sql, args);
	}
	
	public List<Map<String,Object>> searchForMapPage(String sql,int start,int size,Object...args){
		return DBUtil.getListForMapPage(dbop, sql,start,size, args);
	}
	
	public int count(String sql,Object...args){
		return DBUtil.count(dbop, sql, args);
	}
	
	public Object getObj(String sql,Class clz,Object...args){
		return DBUtil.getObj(dbop, sql, clz, args);
	}
	
	public Object getSimpleObj(String sql,Class clz,Object...args){
		return DBUtil.getSimpleObj(dbop, sql, clz, args);
	}
	
	public Long getSeq(String seqname){
		return Long.valueOf(DBUtil.getSeq(dbop, seqname));
	}
	public String getSeqStr(String seqname){
		return DBUtil.getSeq(dbop, seqname);
	}
	public String getTableName(Object obj) throws AIPGException
	{
		if(obj==null) return null;
		return getTableNameEx(obj.getClass());
	}
	public String getTableNameEx(Class clz) throws AIPGException
	{
		if(CoreDao.tableNameMap.containsKey(clz))
			return (String)CoreDao.tableNameMap.get(clz);
		else{
			try{
				Field tnField = clz.getDeclaredField("TABLE_NAME");
				String name = tnField.get(clz).toString();
				tableNameMap.put(clz, name);
				return name;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "找不到对象的pojo类的表名属性:"+clz.getName());
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+clz.getName(),ex);
				AIPGException.throwExcp(CoreConstans.EXCEPTON_01, "无法获取对象的pojo类的表名属性:"+clz.getName());
			}
		}
		return null;
	}
	
	public String getTableName(Class c){
		if(c==null)
			return null;
		if(CoreDao.tableNameMap.containsKey(c))
			return (String)CoreDao.tableNameMap.get(c);
		else{
			try{
				Field tnField = c.getDeclaredField("TABLE_NAME");
				String name = tnField.get(null).toString();
				tableNameMap.put(c, name);
				return name;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+c.getClass().getName(),ex);
				return null;
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+c.getClass().getName(),ex);
				return null;
			}
		}
	}
	
	public String getDataKeys(Object obj){
		if(obj==null)
			return null;
		try{
			Method method = obj.getClass().getDeclaredMethod("toDataKeys");
			return (String)method.invoke(obj);
		}
		catch(NoSuchMethodException ex){
			log.error("找不到对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
		catch(InvocationTargetException ex){
			log.error("找不到对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
		catch(IllegalAccessException ex){
			log.error("无法获取对象的pojo类的toDataKeys方法:"+obj.getClass().getName(),ex);
			return null;
		}
	}	
	
	public String[] getTableKeys(Object obj){
		if(obj==null)
			return null;
		if(CoreDao.tableKeysMap.containsKey(obj.getClass()))
			return (String[])CoreDao.tableKeysMap.get(obj.getClass());
		else{
			try{
				Field tnField = obj.getClass().getDeclaredField("TABLE_KEYS");
				String[] keys = (String[])tnField.get(obj);
				if(keys!=null&&keys.length>0)
					tableKeysMap.put(obj.getClass(), keys);
				return keys;
			}
			catch(NoSuchFieldException ex){
				log.error("找不到对象的pojo类的表名属性:"+obj.getClass().getName(),ex);
				return null;
			}
			catch(IllegalAccessException ex){
				log.error("无法获取对象的pojo类的表名属性:"+obj.getClass().getName(),ex);
				return null;
			}
		}
	}
	
	public int add(Object obj) throws AIPGException{
		String tableName = getTableName(obj);
		if(tableName!=null)
			return DBUtil.insertObj(true,dbop, tableName, obj);
		else
			return 0;
	}
	
	public int add(String sql,Object args){
		return DBUtil.updateEx(dbop, sql, args);
	}
	
	public KeyHolder add(Object obj,String...retCols) throws AIPGException{
		String tableName = getTableName(obj);
		if(tableName!=null)
			return DBUtil.insertObj(true,dbop, tableName, obj, retCols);
		else
			return null;
	}

	public int[] addBatch(Object...objects) throws AIPGException{
		String tableName = null;
		if(objects==null||objects.length==0)
			return null;
		else
			tableName = getTableName(objects[0]);
		if(tableName!=null)
			return DBUtil.insertObjs(true,dbop.getDataSource(), tableName, objects);
		else
			return null;
	}
	
	public int update(Object obj) throws AIPGException{
		String tableName = getTableName(obj);
		if(tableName!=null)
			return DBUtil.updateObjFull(true,dbop, tableName, obj.getClass(), obj, getTableKeys(obj));
		else
			return 0;
	}
	
	public int update(Object obj,int flag) throws AIPGException{
		String tableName = getTableName(obj);
		if(tableName!=null)
			return DBUtil.updateObj(true, dbop, tableName, obj.getClass(), obj, flag, getTableKeys(obj));
		else
			return 0;
	}
	
	public int[] updateBatch(String sql,Object[]... params){
		return DBUtil.updateBatch(dbop,sql, params);
	}
	
	public KeyHolder update(final String sql,final Object[] args,final String[] retCols){
		return DBUtil.update(dbop, sql, args, retCols);
	}
	
	public int update(String sql,Object...args){
		return DBUtil.updateEx(dbop, sql, args);
	}

	public int delete(String sql,Object...args){
		return DBUtil.delete(dbop, sql, args);
	}
	
	public int delete(Object obj) throws AIPGException{
		String tableName = getTableName(obj);
		if(tableName!=null)
			return DBUtil.deleteObj(true,dbop, tableName, obj, getTableKeys(obj));
		else
			return 0;
	}

	public <T> T find(Object obj,Class<T> clz){
		if(clz==null) clz=(Class<T>)obj.getClass();
		String tableName = getTableName(clz);
		if(tableName!=null)
			return clz.cast(DBUtil.findObj(true,dbop, tableName, obj, clz, getTableKeys(obj)));
		else
			return null;
	}
	//args: name1,value1,name2,value2.....
	public <T> T find(Class<T> clz,String tableName,String...args)
	{
		if(tableName==null) tableName = getTableName(clz);
		if(tableName!=null)
			return clz.cast(DBUtil.findObj(dbop, tableName, clz, args));
		else
			return null;
	}
	//args: name1,value1,name2,value2.....
	public <T> T findList(Class<T> clz,String tableName,String...args)
	{
		if(tableName==null) tableName = getTableName(clz);
		if(tableName!=null)
			return clz.cast(DBUtil.findList(dbop, tableName, clz, args));
		else
			return null;
	}
}
