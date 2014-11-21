/**
 * BaseInterceptor.java
 * 2014-4-17
 */
package com.mtools.core.plugin.web.interceptor;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mtools.core.plugin.properties.CoreParams;

/**
 * @author zhang
 *
 * 2014-4-17
 */
public class BaseInterceptor extends HandlerInterceptorAdapter{
	public  Log log= LogFactory.getLog(this.getClass());
	
	@Resource(name="coreParams")
	public CoreParams coreParams;
}
