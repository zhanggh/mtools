/**
 * 通联支付-研发中心
 * SystemController.java
 * 2014-5-6
 */
package com.mtools.core.plugin.auth.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtools.core.plugin.auth.web.BaseController;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.Department;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.TraceLog;
import com.mtools.core.plugin.entiy.vo.DepartVo;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.HttpTools;
import com.mtools.core.plugin.optlog.LogPlugin;

/**
 * @author zhang 功能：系统参数控制类
 * @date 2014-5-6
 */
@Controller
@RequestMapping("/sys")
public class SystemController extends BaseController {


	@Resource(name="logPlugin")
	LogPlugin logPlugin;
	@Resource(name="cacheManager")
	EhCacheCacheManager ehcachemagaer;
	
	/**
	 * 功能：部门列表 2014-5-6
	 */
	@RequestMapping(value = "/depsearch")
	public String departSearch(ModelMap model, HttpSession session,PageInfo page,
			DepartVo dep, String flag, HttpServletRequest request)
			throws Exception {

		List<DepartVo> deps = this.sysPlugin.getDeps(dep, this.page);
		model.addAttribute("deps", deps);
		if ("1".equals(flag)) {
			return "admin/sys/group/listTable";
		} else {
			return "admin/sys/group/list";
		}
	}

	/**
	 * 功能：部门创建 2014-5-6
	 */
	@RequestMapping(value = "/depsearch/create")
	public String departCreate(ModelMap model, HttpSession session,
			@ModelAttribute("dep") DepartVo dep, String flag,
			HttpServletRequest request) throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "新增");
		if ("1".equals(flag)) {
			this.sysPlugin.addDepartment(dep, model);
		}
		Map<String, String> deps = this.sysPlugin.getDepsFoMap();
		model.addAttribute("deps", deps);
		return "admin/sys/group/editForm";

	}

	/**
	 * 功能：查看部门 2014-5-6
	 */
	@RequestMapping(value = "/depsearch/viewdep")
	public String viewDepart(ModelMap model, HttpSession session,PageInfo page,
			Department dep, String flag, String id, HttpServletRequest request)
			throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "查看");
		dep.setDepid(id);
		List<DepartVo> deps = this.sysPlugin.getDeps(dep, page);
		model.addAttribute("dep", deps.get(0));
		Map<String, String> depsMap = this.sysPlugin.getDepsFoMap();
		model.addAttribute("deps", depsMap);
		return "admin/sys/group/editForm";

	}

	/**
	 * 功能：修改部门 2014-5-6
	 */
	@RequestMapping(value = "/depsearch/update")
	public String updateDepart(ModelMap model, HttpSession session,PageInfo page,
			Department dep, String flag, String id, HttpServletRequest request)
			throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "修改");
		if ("1".equals(flag)) {
			this.sysPlugin.updateDepartment(dep, model);
		}
		Map<String, String> depsMap = this.sysPlugin.getDepsFoMap();
		model.addAttribute("deps", depsMap);
		List<DepartVo> deps = this.sysPlugin.getDeps(dep, page);
		model.addAttribute("dep", deps.get(0));
		return "admin/sys/group/editForm";

	}

	/**
	 * 功能：删除部门 2014-5-6
	 */
	@RequestMapping(value = "/depsearch/delete")
	public String deleteDepart(ModelMap model, HttpSession session,PageInfo page,
			Department dep, String flag, String id, HttpServletRequest request)
			throws Exception {
		model.addAttribute(CoreConstans.OP_NAME, "删除");

		Map<String, String> depsMap = this.sysPlugin.getDepsFoMap();
		model.addAttribute("deps", depsMap);
		if (!FuncUtil.isEmpty(dep.getDepid())) {
			List<DepartVo> deps = this.sysPlugin.getDeps(dep, page);
			model.addAttribute("dep", deps.get(0));
			if ("1".equals(flag)) {
				this.sysPlugin.deleteDepartment(dep, model);
			}
			return "admin/sys/group/editForm";
		} else {
			if ("1".equals(flag)) {
				dep.setDepid(id);
				this.sysPlugin.deleteDepartment(dep, model);
			}
			//重定向
			return toView(request, model,"/sys/depsearch");
		}
	}
	
	/**
	 * 功能：日志跟踪
	 * 2014-7-24
	 */
	@RequestMapping(value = "/tracelog/query")
	public String traceQuery(ModelMap model, HttpSession session,PageInfo page,
		 String userid, String startTime, String endTime,HttpServletRequest request){
				
		List<TraceLog>  tracelogs = logPlugin.traceQuery(userid, "", startTime, endTime,this.page);
		model.addAttribute("tracelogs", tracelogs);
		return "admin/sys/trace/list";
	}
	
	/**
	 * 功能：日志跟踪
	 * 2014-7-24
	 */
	@RequestMapping(value = "/tracelog/query", headers = "table=true")
	public String asytraceQuery(ModelMap model, HttpSession session,PageInfo page,
			String userid, String startTime, String endTime,HttpServletRequest request){
		
		List<TraceLog>  tracelogs = logPlugin.traceQuery(userid, "", startTime, endTime,this.page);
		model.addAttribute("tracelogs", tracelogs);
		return "admin/sys/trace/listTable";
	}
	
	@RequestMapping(value = "/delallcaches")
	public String deleteAllCaches(ModelMap model, HttpSession session,PageInfo page,
		 String userid, String startTime, String endTime,HttpServletRequest request){
			
		log.debug("清除全部缓存");
		
		executor.execute(new Runnable(){

			public void run() {
				String jsonString = "{\"body\":{\"cacheName\":\"all\",\"reqtime\":\"2014-07-30 17:48:26\"},\"info\":{\"leve\":\"1\",\"trxcode\":\"deleteAllCaches\"}}";
				try {
					jsonString = HttpTools.send(URL, jsonString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("返回报文："+jsonString);
			}
			
		});
		
		ehcachemagaer.getCacheManager().clearAll();
		request.setAttribute(CoreConstans.ERROR_MESSAGE,"delete caches success!");
    	log.debug("force delete All Caches success");
    	return "front/msgdialog";
	}
	
}
