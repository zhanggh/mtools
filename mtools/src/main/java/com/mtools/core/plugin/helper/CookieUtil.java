/**
 * CookieUtil.java
 * 2014-4-18
 */
package com.mtools.core.plugin.helper;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.constant.CoreConstans;

/**
 * @author zhang
 *
 * 2014-4-18
 */
public class CookieUtil  {
	private static final Log log = LogFactory.getLog(CookieUtil.class);
	
	/**
	 * 功能：获取原请求uri
	 * 2014-4-18
	 * @throws UnsupportedEncodingException 
	 */
	public static String initOrgReqUri(ServletRequest request, ServletResponse response) throws UnsupportedEncodingException{
		String orgUri="login";
		try{
			HttpServletRequest req=(HttpServletRequest) request;
			HttpServletResponse resp=(HttpServletResponse) response;
			String method=req.getMethod();
			String queryStr=req.getQueryString();
			String reqestUri=req.getServletPath();//.getRequestURI();
			if("GET".equals(method)&&queryStr!=null){
				reqestUri+="?"+queryStr;
			}
			orgUri=  CookieUtil.getCookie(req, CoreConstans.REQESTURI);
			log.info("原请求uri:"+reqestUri);
			CookieUtil.setCookie(resp, CoreConstans.REQESTURI, reqestUri);
		}catch(Exception e){
			e.printStackTrace();
			log.error("初始化cookie异常:"+e.getMessage());
		}
		return orgUri;
	}
	
	
	public static void setCookie(HttpServletResponse response,String key,String value) throws UnsupportedEncodingException{
		Cookie oItem;  
		oItem = new Cookie(key,Base64.encodeBase64URLSafeString(value.getBytes()));  
		oItem.setMaxAge(-1); //关闭浏览器后，cookie立即失效          
	    oItem.setPath("/");  
	    response.addCookie(oItem);
	}
	
	public static String getCookie(HttpServletRequest request,String key) throws UnsupportedEncodingException{
		final Cookie[] oCookies = request.getCookies();  
	    if (oCookies != null)  
	    {  
	        for (final Cookie oItem : oCookies)  
	        {  
	            final String sName = oItem.getName();  
	  
	            if (sName.equals(key))  
	            {  
	            	String value=new String(Base64.decodeBase64(oItem.getValue()));
	            	log.info("cookies key:"+key+" value:"+value);
	            	return value; //解码 CookieUtil.decode()是BASE64解码方法,略..  
	            }  
	        }  
	    }  
	    return null;  
	}
}
