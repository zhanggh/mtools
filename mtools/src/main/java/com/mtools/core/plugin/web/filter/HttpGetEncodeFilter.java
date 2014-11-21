package com.mtools.core.plugin.web.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.properties.CoreParams;

public class HttpGetEncodeFilter implements Filter {
	private static final Log log = LogFactory.getLog(HttpGetEncodeFilter.class);

	String tomcatEncode="UTF-8";
	 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req=(HttpServletRequest) request;
		if(req.getMethod().equalsIgnoreCase("get")){
//			log.debug("get 请求原编码："+req.getCharacterEncoding());
			if("ISO8859-1".equals(tomcatEncode)){
				log.debug("get tomcat 默认编码 ISO8859-1");
				Map<String,String> params = request.getParameterMap();
				Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
				  while (it.hasNext()) {
				   Map.Entry<String, String> entry = it.next();
				   String value = request.getParameter(entry.getKey()); 
				   value = new String(value.getBytes("ISO8859-1"),  
	                       request.getCharacterEncoding()); 
				   log.debug("get 请求参数："+entry.getKey()+"--"+value);
//				   params.put(entry.getKey(), value);
				  }
				req.setCharacterEncoding("UTF-8");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.tomcatEncode=config.getInitParameter("tomcatEncode");
	}

}
