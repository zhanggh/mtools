package com.mtools.core.plugin.web.interceptor;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class TokenInterceptor extends BaseInterceptor
{

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
  {
	 log.info("**********************进入TokenInterceptor**********************");
	 return true;
  }
}
 