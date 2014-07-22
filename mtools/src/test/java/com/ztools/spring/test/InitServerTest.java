package com.ztools.spring.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.entity.Emailconfig;
import com.mtools.core.plugin.helper.SpringUtil;
import com.ztools.entity.SystemInfo;

public class InitServerTest {

	/**
	 * @param args
	 */
	public static  Log log= LogFactory.getLog(InitServerTest.class);
	public static void main(String[] args) {
		log.info("服务开始启动");
		SpringUtil.initSpringCfg("ztools.testbeans.xml");
    	
    	Emailconfig email=(Emailconfig) SpringUtil.getBean("email");
    	SystemInfo sysInfo=(SystemInfo) SpringUtil.getAnoBean("sysInfo");
    	System.out.println(email.getToemails());
    	System.out.println(sysInfo.getVersion());
    	System.out.println(Boolean.parseBoolean("true1"));
    	System.out.println(Boolean.parseBoolean("false"));
//    	
//    	ThreadLocal th=new ThreadLocal();
//    	System.out.println(th.);
    	System. out .println(Thread. currentThread ().getName());
    	log.info("服务结束");
	}

}
