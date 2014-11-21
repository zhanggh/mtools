/**
 * BaseController.java
 * 2014-4-14
 */
package com.mtools.core.plugin.auth.web;
import java.util.concurrent.Executor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mtools.core.plugin.auth.AuthPlugin;
import com.mtools.core.plugin.auth.MenuPlugin;
import com.mtools.core.plugin.auth.service.UserService;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.helper.XStreamIg;
import com.mtools.core.plugin.properties.CoreParams;
import com.mtools.core.plugin.sys.SysConfigPlugin;

/**
 * @author zhang
 *	控制层基类
 * 2014-4-14
 */
public abstract class BaseController {

	public  Log log= null;
	@Resource(name="userService")
	public UserService userSv;
	@Resource(name="menuPlugin")
	public MenuPlugin menuPlugin;
	@Resource(name="auth")
	public AuthPlugin authPlugin;
	@Resource(name="sysPlugin")
	public SysConfigPlugin sysPlugin;;
	@Resource(name="coreParams")
	public CoreParams coreParams;;
	@Resource(name = "taskExecutor")
	public Executor executor;
	public PageInfo page;
	
	public String menuType;//=SpringUtil.getApplicationContext().getApplicationName().substring(1);
	public String permType;
	public static final String URL="http://127.0.0.1:8282/techsp/helperServlet";
	public String backUri;
	
	public BaseController() {
		super();
		 menuType=SpringUtil.getApplicationContext().getApplicationName().substring(1);
		 permType=SpringUtil.getApplicationContext().getApplicationName().substring(1);
		 log=LogFactory.getLog(this.getClass());
	}
	/**
	 * 获取回话用户对象
	 * @param session
	 * @return
	 */
	public  UserInfo getUser(HttpSession session){
		UserInfo user=(UserInfo) session.getAttribute(CoreConstans.LOGINGUSER);
		return user;
	}
	/**
	 * 获取回话用户名
	 * @param session
	 * @return
	 */
	public String getUsername(HttpSession session){
		UserInfo user=(UserInfo) session.getAttribute(CoreConstans.LOGINGUSER);
		return user.getUsername();
	}
	
	/**
	 * 获取回话用户号
	 * @param session
	 * @return
	 */
	public String getUserid(HttpSession session){
		UserInfo user=(UserInfo) session.getAttribute(CoreConstans.LOGINGUSER);
		return user.getUserid();
	}
	
	@ModelAttribute("page") 
	public PageInfo initPage(HttpServletRequest request, HttpServletResponse response,PageInfo pageinfo,String pageIndex,String pageSize){
		//获取原请求URI
		getCtxPath(request, response);
		backUri= request.getHeader("Referer");  
		request.setAttribute(CoreConstans.BACK_URL,backUri);
		//初始化翻页
		page=pageinfo; 
		this.page.setPageIndex(pageIndex);
		this.page.setPageSize(pageSize);
		log.info(XStreamIg.toXml(this.page));
		return page;
	}
	
	public String getCtxPath(HttpServletRequest request, HttpServletResponse response){
		//定义一些常用变量
		String path = request.getContextPath();
		String ctx = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + path;
		request.setAttribute("ctx",path);
		request.setAttribute("curOnline",currentOnline(request));
		return ctx;
	}
	
	/**
	 * 当前在线人数
	 * @param request
	 * @return
	 */
	public String currentOnline(HttpServletRequest request){
		int totalCount = (Integer) request.getSession().getServletContext().getAttribute(CoreConstans.ACESS_COUNT);
		return String.valueOf(totalCount);
	}
	
	public void setCommonData(Model model){} ;
	
	public String toView(HttpServletRequest request,ModelMap model,String uri){
		String cxt=(String) request.getAttribute("ctx");
		request.setAttribute(CoreConstans.ERROR_MESSAGE,model.get(CoreConstans.OPTRESULT));
		request.setAttribute(CoreConstans.BACK_URL,cxt+uri);
		return "front/msgdialog";
	}
}
