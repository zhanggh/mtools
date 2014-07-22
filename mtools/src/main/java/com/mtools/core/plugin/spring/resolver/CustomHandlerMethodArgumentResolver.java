/**
 * CustomHandlerMethodArgumentResolver.java
 * 2014-4-17
 */
package com.mtools.core.plugin.spring.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.mtools.core.plugin.annotation.CurrentUser;

/**
 * @author zhang
 * 
 * 方法参数赋值
 *         2014-4-17
 */
public class CustomHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {


	/**
	 * 功能：
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(CurrentUser.class)) {
			return true;
		}
		return false;
	}

	/**  
	 * 功能：
	 */
	  public Object resolveArgument(MethodParameter parameter, 
			  ModelAndViewContainer mavContainer, 
			  NativeWebRequest webRequest, 
			  WebDataBinderFactory binderFactory) throws Exception {
		  Object value=null;
		 CurrentUser currentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class);
		 HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		 if(currentUserAnnotation!=null){
			 value=request.getSession().getAttribute(currentUserAnnotation.value());
		 }else{
			 value=request.getAttribute(currentUserAnnotation.value());
		 }
		 return value;
	}

}
