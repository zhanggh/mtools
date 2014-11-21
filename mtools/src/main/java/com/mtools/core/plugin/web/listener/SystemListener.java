package com.mtools.core.plugin.web.listener;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.helper.FuncUtil;

/**
 * 系统监控
 */
public final class SystemListener implements HttpSessionListener, HttpSessionAttributeListener{
	protected  final Log log = LogFactory.getLog(this.getClass());
	private int count=0;
	private int totalCount=0;
    /**
     * Default constructor. 
     */
    public SystemListener() {
    	log.info("****************SystemListener********************");
    }

	 
	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    @SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent arg0) {
    	log.info("****************sessionDestroyed********************");
    	UserInfo user=(UserInfo) arg0.getSession().getAttribute(CoreConstans.LOGINGUSER);
		List<UserInfo> onlineUser = null;
		onlineUser = (List<UserInfo>) arg0.getSession().getServletContext().getAttribute(
				CoreConstans.ONLINEUSERS);
		if (onlineUser!=null&&onlineUser.contains(onlineUser)) {
			onlineUser.remove(user);
		} else {
			log.debug("普通游客退线");
		}
		//访问人数减少
		totalCount--;
    	arg0.getSession().getServletContext().setAttribute(CoreConstans.ACESS_COUNT, totalCount);
    	log.info("当前在线人数:"+totalCount);
    }

	 
	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
    	long lastaccess = arg0.getSession().getLastAccessedTime();
    	Date date = new Date(lastaccess);
    	log.info("last access time :"+FuncUtil.formatTime(date, "yyyy年MM月dd日 HH时mm分ss秒 E "));
    	log.info("****************创建连接********************");
    	totalCount++;
    	arg0.getSession().getServletContext().setAttribute(CoreConstans.ACESS_COUNT, totalCount);
    	log.info("当前在线人数:"+totalCount);
    }


	public void attributeAdded(HttpSessionBindingEvent arg0) {
		log.info("****************增加session属性********************");
		if(CoreConstans.LOGINGUSER.equals(arg0.getName())){
			count++;
			UserInfo user=(UserInfo) arg0.getValue();
			if(user!=null)
				log.info("****************用户："+user.getUserid()+" 登录成功********************");
			arg0.getSession().getServletContext().setAttribute(CoreConstans.LOGIN_COUNT, count);
		}
	}


	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		log.info("****************删除session属性********************");
		if(CoreConstans.LOGINGUSER.equals(arg0.getName())){
			count--;
			UserInfo user=(UserInfo) arg0.getValue();
			if(user!=null)
				log.info("****************用户："+user.getUserid()+" 已退出********************");
			arg0.getSession().getServletContext().setAttribute(CoreConstans.LOGIN_COUNT, count);
		}
		
	}


	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
