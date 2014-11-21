package com.mtools.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.helper.SpringUtil;

public class ListenerInit {

	static Log log=LogFactory.getLog(ListenerInit.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("服务开始启动");
		     
		SpringUtil.initSpringCfg("ztools.testbeans.xml");
//    	
//    	Emailconfig email=(Emailconfig) SpringUtil.getBean("email");
//    	SystemInfo sysInfo=(SystemInfo) SpringUtil.getAnoBean("sysInfo");
//    	System.out.println(email.getToemails());
//    	System.out.println(sysInfo.getVersion());
//    	System.out.println(Boolean.parseBoolean("true1"));
//    	System.out.println(Boolean.parseBoolean("false"));
//    	
//    	ThreadLocal th=new ThreadLocal();
//    	System.out.println(th.);
    	System. out .println(Thread. currentThread ().getName());
    	log.info("服务结束");
	}


}
