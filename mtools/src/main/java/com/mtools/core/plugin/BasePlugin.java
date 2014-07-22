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
import com.mtools.core.plugin.mail.MailImpl;

/**
 * @author zhang
 * 	插件基类
 * 2014-4-14
 */
public class BasePlugin {

	public  Log log= LogFactory.getLog(this.getClass());
	@Autowired
	public CoreDao dao;
	@Autowired
	public MailImpl mailImpl;
	@Resource(name = "taskExecutor")
	Executor executor;
	public String errorMsg="处理失败";
	public Long getSeq(String seqName){
		String sql="select RLPERMSEQ.Nextval from dual";
		return this.dao.getSeq(seqName);
	}
}
