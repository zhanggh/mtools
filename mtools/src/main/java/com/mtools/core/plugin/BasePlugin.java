/**
 * BasePlugin.java
 * 2014-4-14
 */
package com.mtools.core.plugin;

import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mtools.core.plugin.db.CoreDao;
import com.mtools.core.plugin.db.DBSqlUtil;
import com.mtools.core.plugin.mail.MailImpl;
import com.mtools.core.plugin.properties.CoreParams;

/**
 * @author zhang
 * 	插件基类
 * 2014-4-14
 */
public class BasePlugin {

	/**
	 * 尽量使用该日志组件，里面做了特殊字符过滤
	 */
	public static Log log=null;
	@Autowired
	public CoreDao dao;
	@Autowired
	public MailImpl mailImpl;
	@Resource(name = "taskExecutor")
	public Executor executor;
	public String errorMsg="处理失败";
	@Resource(name = "coreParams")
	public CoreParams coreParams;
	
//	@Resource(name = "dbSqlUtil")
//	public DBSqlUtil dbSqlUtil;
	
	public Long getSeq(String seqName){
		return this.dao.getSeq(seqName);
	}

	public CoreDao getDao() {
		return dao;
	}

	public void setDao(CoreDao dao) {
		this.dao = dao;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public BasePlugin() {
		super();
		if(log==null)
			log=LogFactory.getLog(this.getClass());
	}

//	public DBSqlUtil getDbSqlUtil() {
//		return dbSqlUtil;
//	}
//
//	public void setDbSqlUtil(DBSqlUtil dbSqlUtil) {
//		this.dbSqlUtil = dbSqlUtil;
//	}
}
