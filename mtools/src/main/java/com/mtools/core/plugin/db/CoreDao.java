package com.mtools.core.plugin.db;
 
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.notify.AsyncNotify;

//
@SuppressWarnings("unchecked")

 
public class CoreDao {

	static Log log=LogFactory.getLog(CoreDao.class);
	@Resource(name="sysRunningNotify")
	private AsyncNotify notify;
	@Resource(name = "taskExecutor")
	public Executor executor;
	
	public CoreDao() {
		super();
		log.info("start dao...");
	}
	private JdbcTemplate dbop;
	private boolean isOrcl;

	/**
	 * @return the isOrcl
	 */
	public boolean getIsOrcl() {
		return isOrcl;
	}

	/**
	 * @param isOrcl the isOrcl to set
	 */
	public void setIsOrcl(String isOrcl) {
		//false : MYSQL数据库   true ORALCE
		this.isOrcl = Boolean.parseBoolean(isOrcl);
	}

	public JdbcTemplate getDbop() {
		return dbop;
	}

	public void setDbop(JdbcTemplate dbop) {
		this.dbop = dbop;
	}
	
	public <T> List<T> search(String sql,Class<T> clz,Object...args) throws Exception{
		try {
			return DBUtil.getList(dbop, sql, clz, args);
		} catch (Exception e) {
			 if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	public <T> List<T> searchSimp(Class<T> clz,int start,int size,int flag,Object startObj,Object endObj,String... ranges) throws Exception
	{
		try {
			return DBUtil.search(this.isOrcl,true, dbop, DBUtil.getTableNameEx(clz), start, size, flag, startObj, endObj, clz, ranges);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	public <T> long countSimp(Class<T> clz,int flag,Object startObj,Object endObj,String... ranges) throws Exception
	{
		try {
			return DBUtil.count(true, dbop, DBUtil.getTableNameEx(clz), flag, startObj, endObj, clz, ranges);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public <T> List<T> searchPage(String sql,Class<T> clz,int start,int size,Object...args) throws Exception{
		try {
			return DBUtil.getPage(this.isOrcl,dbop, sql, clz, (start-1)*size, size, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public List<Object[]> searchForArray(String sql,Object...args) throws Exception
	{
		try {
			return DBUtil.getListForArray(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public List<Object[]> searchForArrayPage(String sql,int start,int size,Object...args) throws Exception{
		try {
			return DBUtil.getListForArrayPage(this.isOrcl,dbop, sql,start,size, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public List<Map<String,Object>> searchForMap(String sql,Object...args) throws Exception{
		try {
			return DBUtil.getListForMap(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public List<Map<String,Object>> searchForMapPage(String sql,int start,int size,Object...args) throws Exception{
		try {
			return DBUtil.getListForMapPage(this.isOrcl,dbop, sql,start,size, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int count(String sql,Object...args) throws Exception{
		try {
			return DBUtil.count(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object getObj(String sql,Class clz,Object...args) throws Exception{
		try {
			return DBUtil.getObj(dbop, sql, clz, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object getSimpleObj(String sql,Class clz,Object...args) throws Exception{
		try {
			return DBUtil.getSimpleObj(dbop, sql, clz, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public Long getSeq(String seqname){
		return Long.valueOf(DBUtil.getSeq(dbop, seqname));
	}
	public String getSeqStr(String seqname){
		return DBUtil.getSeq(dbop, seqname);
	}

	
	public int add(Object obj) throws Exception{
		try {
			String tableName = DBUtil.getTableName(obj);
			if(tableName!=null)
				return DBUtil.insertObj(true,dbop, tableName, obj);
			else
				return 0;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int add(String sql,Object args) throws Exception{
		try {
			return DBUtil.updateEx(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public KeyHolder add(Object obj,String...retCols) throws Exception{
		try {
			String tableName = DBUtil.getTableName(obj);
			if(tableName!=null)
				return DBUtil.insertObj(true,dbop, tableName, obj, retCols);
			else
				return null;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}

	public int[] addBatch(Object...objects) throws Exception{
		try {
			String tableName = null;
			if(objects==null||objects.length==0)
				return null;
			else
				tableName = DBUtil.getTableName(objects[0]);
			if(tableName!=null)
				return DBUtil.insertObjs(true,dbop.getDataSource(), tableName, objects);
			else
				return null;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int update(Object obj) throws Exception{
		try {
			String tableName = DBUtil.getTableName(obj);
			if(tableName!=null)
				return DBUtil.updateObjFull(true,dbop, tableName, obj.getClass(), obj, DBUtil.getTableKeys(obj));
			else
				return 0;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int update(Object obj,int flag) throws Exception{
		try {
			String tableName = DBUtil.getTableName(obj);
			if(tableName!=null)
				return DBUtil.updateObj(true, dbop, tableName, obj.getClass(), obj, flag, DBUtil.getTableKeys(obj));
			else
				return 0;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int[] updateBatch(String sql,Object[]... params) throws Exception{
		try {
			return DBUtil.updateBatch(dbop,sql, params);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public KeyHolder update(final String sql,final Object[] args,final String[] retCols) throws Exception{
		try {
			return DBUtil.update(dbop, sql, args, retCols);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int update(String sql,Object...args) throws Exception{
		try {
			return DBUtil.updateEx(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}

	public int delete(String sql,Object...args) throws Exception{
		try {
			return DBUtil.delete(dbop, sql, args);
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	
	public int delete(Object obj) throws Exception{
		try {
			String tableName = DBUtil.getTableName(obj);
			if(tableName!=null)
				return DBUtil.deleteObj(true,dbop, tableName, obj, DBUtil.getTableKeys(obj));
			else
				return 0;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}

	public <T> T find(Object obj,Class<T> clz) throws Exception{
		try {
			if(clz==null) clz=(Class<T>)obj.getClass();
			String tableName = DBUtil.getTableName(clz);
			if(tableName!=null)
				return clz.cast(DBUtil.findObj(true,dbop, tableName, obj, clz, DBUtil.getTableKeys(obj)));
			else
				return null;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	//args: name1,value1,name2,value2.....
	public <T> T find(Class<T> clz,String tableName,String...args) throws Exception
	{
		try {
			if(tableName==null) tableName = DBUtil.getTableName(clz);
			if(tableName!=null)
				return clz.cast(DBUtil.findObj(dbop, tableName, clz, args));
			else
				return null;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
	//args: name1,value1,name2,value2.....
	public <T> T findList(Class<T> clz,String tableName,String...args) throws Exception
	{
		try {
			if(tableName==null) tableName = DBUtil.getTableName(clz);
			if(tableName!=null)
				return clz.cast(DBUtil.findList(dbop, tableName, clz, args));
			else
				return null;
		} catch (Exception e) {
			if(e instanceof DataAccessException||e instanceof CannotGetJdbcConnectionException){
				 notify.initData(null, e,SpringUtil.getApplicationContext().getApplicationName());
				 executor.execute(notify);
			 }
			e.printStackTrace();
			throw e;
		}
	}
}
