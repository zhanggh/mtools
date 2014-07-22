package com.mtools.core.plugin.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.mtools.core.plugin.annotation.AuthAccess;
import com.mtools.core.plugin.auth.AuthPlugin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.helper.CookieUtil;
 
/**
 * 权限拦截器 
 * zhang
 */
public class AuthorityInterceptor extends BaseInterceptor {

	@Resource(name="auth")
	AuthPlugin auth;
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		String uri=request.getServletPath();
		log.info("请求uri:"+uri);
		log.info("**********************进入AuthorityInterceptor**********************");
		if("/".equals(uri))
			return true;
		
		Object handlerObj = handler;
		boolean authLessMethod = false;
		if(handlerObj instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handlerObj;
			AuthAccess access = handlerMethod.getMethodAnnotation(AuthAccess.class);
			authLessMethod = access!=null;
			handlerObj = handlerMethod.getBean();
		}

		if(!authLessMethod){
			auth.checkAccess(request);
		} 
		return true;
	}
}