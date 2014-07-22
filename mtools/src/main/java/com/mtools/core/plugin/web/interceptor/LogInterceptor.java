package com.mtools.core.plugin.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mtools.core.plugin.optlog.LogPlugin;

public class LogInterceptor extends BaseInterceptor
{

	@Resource(name="logPlugin")
	LogPlugin logPlugin;
	/**  
	 * 功能：执行后记录日志
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("**********************进入LogInterceptor**********************");
		logPlugin.traceSave(request);
		super.afterCompletion(request, response, handler, ex);
	}
}
 