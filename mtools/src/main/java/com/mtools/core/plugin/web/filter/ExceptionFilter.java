package com.mtools.core.plugin.web.filter;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.RespMsg;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.CookieUtil;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.JsonParse;
import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.notify.AsyncNotify;
 

 
/**
 * @author zhang
 * 异常拦截器
 * 2014-4-14
 */
public class ExceptionFilter implements Filter {
	private static final Log log = LogFactory.getLog(ExceptionFilter.class);
	private RespMsg respmsg;
	private AsyncNotify notify;
	private TaskExecutor taskExecutor;
	private String errorUrl;
	private String ajaxUrl;
	
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		respmsg=(RespMsg) SpringUtil.getAnoBean("respmsg");
		try{
			chain.doFilter(request, response);
		}catch(Exception e){
			log.error("捕获到异常"+e.getMessage(),e);
			String errorMsg = "未知错误";
			String errorCode = CoreConstans.EXCEPTON_01;
			if(e.getCause() instanceof AIPGException){
				AIPGException aipex=(AIPGException) e.getCause();
				errorMsg = aipex.getMessage();
				errorCode=aipex.getErrorNum();
			}else if(!(e.getCause()  instanceof SocketException)){//如果catch到的异常不属于AIPGException类型的，将发送邮件通知
				if(notify==null)
					notify= (AsyncNotify) SpringUtil.getAnoBean("sysRunningNotify");
				notify.initData(request, e,"");
				if(taskExecutor==null)
					taskExecutor=(TaskExecutor) SpringUtil.getAnoBean("taskExecutor");
				taskExecutor.execute(notify);
			}
			//发生异常时，记住当前页面
			if(!errorCode.equals(CoreConstans.EXCEPTON_03)){
				CookieUtil.initOrgReqUri(request, response);
			}

			if(isAjaxRequest((HttpServletRequest)request)){
				respmsg.setMessage(errorMsg);
				request.setAttribute("jsonString",JsonParse.obj2json(respmsg));
				RequestDispatcher dispathcher = request.getRequestDispatcher(ajaxUrl);
				dispathcher.forward(request, response);
			}else{
				request.setAttribute(CoreConstans.ERROR_MESSAGE, errorMsg);
				RequestDispatcher dispathcher = request.getRequestDispatcher(errorUrl);
				dispathcher.forward(request, response);
			}
		}
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {   
        String header = request.getHeader("X-Requested-With");   
        if (header != null && "XMLHttpRequest".equals(header))   
            return true;   
        else  
            return false;   
    } 

	public void init(FilterConfig filterConfig) throws ServletException {
		errorUrl = filterConfig.getInitParameter("errorurl");
		ajaxUrl = filterConfig.getInitParameter("ajaxurl");
	}
}
