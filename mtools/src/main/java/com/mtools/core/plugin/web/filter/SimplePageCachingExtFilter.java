/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-6-25
 * version 1.0
 * 说明：页面缓存过滤器
 */
package com.mtools.core.plugin.web.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

/**
 *  功能：对post表单的值进行过滤
 * @date 2014-6-25
 */
public class SimplePageCachingExtFilter extends SimplePageCachingFilter {

	public Log log = LogFactory.getLog(this.getClass());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String calculateKey(HttpServletRequest httpRequest)
	  {
		
		String serverHost = httpRequest.getHeader("Host");
		StringBuffer query=new StringBuffer();
		
		if(httpRequest.getMethod().equalsIgnoreCase("post")){
			Map map = httpRequest.getParameterMap();
			Set<Map.Entry> set = map.entrySet();
			for(Iterator<Map.Entry> it = set.iterator(); it.hasNext();){
				Map.Entry entry = (Map.Entry) it.next();
				String[] vals=(String[]) entry.getValue();
				log.debug("key:"+entry.getKey()+"--"+"value:"+vals[0]);
				query.append(entry.getKey()).append("=").append(vals[0]).append("&&");
			}
		}else{
			query.append(httpRequest.getQueryString());
		}
		query.append("serverHost=").append(serverHost);
	    StringBuffer stringBuffer = new StringBuffer();
	    stringBuffer.append(httpRequest.getMethod()).append(httpRequest.getRequestURI()).append(query);
	    String key = stringBuffer.toString();
	    return key;
	  }
}
