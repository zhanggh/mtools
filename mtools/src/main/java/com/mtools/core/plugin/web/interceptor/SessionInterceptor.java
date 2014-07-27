package com.mtools.core.plugin.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.mtools.core.plugin.annotation.AuthLogin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.helper.AIPGException;



/**
 * @author zhang
 * 登陆过滤器
 * 2014-4-14
 */
public class SessionInterceptor extends BaseInterceptor {

	private static final String INDEX_URL = "/login";
	List<UserInfo> onlineUser=null;
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		log.info("进入session超时拦截器，访问ip："+request.getRemoteHost()+"访问端口："+request.getRemotePort()+" 访问者："+request.getRemoteUser());
		log.info("进入session超时拦截器，访问URI："+request.getRequestURI());
		Object handlerObj = handler;
		boolean authLoginMethod = false;
		if(handlerObj instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handlerObj;
			handlerObj = handlerMethod.getBean();
			AuthLogin auLogin = handlerMethod.getMethodAnnotation(AuthLogin.class);
			authLoginMethod = auLogin!=null;
		}else{
			return true;
		}
		if(authLoginMethod){
			return true;
		}
		onlineUser = (List<UserInfo>) request.getSession().getServletContext().getAttribute(CoreConstans.ONLINEUSERS);
		UserInfo user = (UserInfo) request.getSession().getAttribute(CoreConstans.LOGINGUSER);
		if(request.getSession().getAttribute(CoreConstans.LOGINGUSER)==null||(onlineUser!=null&&!onlineUser.contains(user))){
			request.getSession().removeAttribute(CoreConstans.LOGINGUSER);
			request.setAttribute(CoreConstans.REQUEST_URL, request.getContextPath()+INDEX_URL);
			throw new AIPGException(CoreConstans.EXCEPTON_02,"登陆超时,请重新登陆");
		} 
		return true;
	}
}
