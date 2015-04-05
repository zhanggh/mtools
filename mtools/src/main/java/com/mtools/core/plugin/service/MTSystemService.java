package com.mtools.core.plugin.service;

import com.mtools.core.plugin.db.CoreDao;
import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.notify.AsyncNotify;


/**
 * @author zhang
 * 系统服务组件
 */
public final class MTSystemService {

	static MTSystemService self;
	
	public CoreDao getCoreDao(String beanName){
		CoreDao dao = (CoreDao) SpringUtil.getBean(beanName);
		return dao;
	}
	public AsyncNotify getNotifyService(){
		AsyncNotify nofity = (AsyncNotify) SpringUtil.getBean("sysRunningNotify");
		return nofity;
	}
	
	public static MTSystemService getInstance(){
		if(self==null){
			self=new MTSystemService();
		}
		return self;
	}
}
