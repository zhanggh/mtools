package com.mtools.core.plugin.auth.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtools.core.plugin.annotation.AuthAccess;
import com.mtools.core.plugin.annotation.AuthLogin;
import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.entity.MenuInfo;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.entiy.vo.JsonMsg;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {

	@AuthAccess
	@RequestMapping(value = "welcome")
	public String toHomePage(ModelMap m,String cardnum,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		return "admin/index/welcome";
	}
 
	@RequestMapping(value = "index")
	public String index(ModelMap model, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<MenuInfo> menus=menuPlugin.getUserMenus(this.getUserid(session),coreParams.serverName);
		model.addAttribute("menus", menus);
		return "admin/index/index";
	}
	
	
	
	@AuthLogin
	@AuthAccess
	@ResponseBody
	@RequestMapping(value = "test")
	public Object test(ModelMap m,String cardnum,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		UserInfo user=new UserInfo();
		user.setUserid("zhangg");
		user.setUsername("张广海");
		JsonMsg<UserInfo> json=new JsonMsg<UserInfo>();
		json.setCode("0");
		json.setMessage("处理失败");
		json.setItems(user);
		return json;
	}
	
	
	@AuthLogin
	@AuthAccess
	@ResponseBody
	@RequestMapping(value = "polling")
	public Object polling(ModelMap m,String cardnum,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		UserInfo user=new UserInfo();
		user.setUserid("zhangg");
		user.setUsername("张广海");
		JsonMsg<UserInfo> json=new JsonMsg<UserInfo>();
		json.setCode("0");
		json.setMessage("处理失败");
		json.setItems(user);
		return json;
	}
	
	
	/**
	 * 功能：异常提示
	 * 2014-4-21
	 */
	@AuthAccess
	@AuthLogin
	@RequestMapping(value = "excepmsg")
	public String toException(ModelMap m,String cardnum,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		log.info("**********excepmsg************");
		return "front/msgdialog";
	}
	/**
	 * 功能：百度编辑器demo
	 * 2014-4-21
	 */
	@AuthAccess
	@AuthLogin
	@RequestMapping(value = "toueditor")
	public String toueditor(ModelMap m,String cardnum,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		log.info("**********百度编辑器实例************");
		return "uitools/ueditor";
	}

	/**  
	 * 功能：
	 */
	@Override
	public void setCommonData(Model model) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * @param backURL null 将重定向到默认getViewPrefix()
     * @return
     */
    protected String redirectToUrl(String backURL) {
    	 if (StringUtils.isEmpty(backURL)) {
             backURL = this.backUri;
         }
         if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
             backURL = "/" + backURL;
         }
         return "redirect:" + backURL;
    }
	
}
