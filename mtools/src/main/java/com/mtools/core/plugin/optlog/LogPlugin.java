/**
 * LogPlugin.java
 * 2014-4-14
 */
package com.mtools.core.plugin.optlog;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.auth.AuthPlugin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.entity.Permission;
import com.mtools.core.plugin.entity.TraceLog;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.XStreamIg;

/**
 * @author zhang 操作日志插件 2014-4-14
 */
@Component("logPlugin")
public class LogPlugin extends BasePlugin {

	TraceLog tlog;
	@Resource(name = "auth")
	AuthPlugin auth;

	/**
	 * 功能：保存敏感操作痕迹 2014-4-14
	 * @throws Exception 
	 */
	public void traceSave(ServletRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();
		String fromIp = req.getRemoteHost();
		log.info("操作日志记录--访问地址:" + path);
		UserInfo user = (UserInfo) req.getSession().getAttribute(
				CoreConstans.LOGINGUSER);
		if (user == null) {
			user = new UserInfo();
			user.setUserid("游客");
		}
		tlog = new TraceLog();
		tlog.setFromIp(fromIp);
		tlog.setLoginuser(user.getUserid());
		tlog.setOptUrl(path);
		String optName=(String) req.getAttribute(CoreConstans.OP_NAME);
		if(FuncUtil.isEmpty(optName)){
			tlog.setOptName(auth.getPermName(path));
		}else{
			tlog.setOptName(optName);
		}
		tlog.setOptTime(FuncUtil.getCurrTimestamp());
		tlog.setOptResult((String) req.getAttribute(CoreConstans.OPTRESULT));
		tlog.setOrgParams((String) req.getAttribute(CoreConstans.ORGPARAMS));
		Permission perm = auth.getPermByUri(path);
		if (!FuncUtil.isEmpty(tlog.getOptResult())&& perm != null&& perm.getPermlevel() > 0) {
			this.dao.add(tlog);
		} else {
			log.info("查询操作，操作日志无需入库");
			log.info(XStreamIg.toXml(tlog));
		}
	}
	
	
	/**
	 * 功能：操作日志查询
	 * 2014-7-24
	 * @param page 
	 * @throws Exception 
	 */
	public List<TraceLog> traceQuery(String userid,String username,String startTime,String endTime, PageInfo page) throws Exception{
		String sql = "select t.* from tracelog t where 1=1 ";
		if(!FuncUtil.isEmpty(userid)){
			sql+=" and loginuser = '"+userid+"'";
		}
		if(!FuncUtil.isEmpty(username)){
			sql+=" and username = '"+username+"'";
		}
		if(!FuncUtil.isEmpty(startTime)){
			sql+=" and opt_Time >= to_date('"+startTime+"','yyyy-MM-dd')";
		}
		if(!FuncUtil.isEmpty(endTime)){
			sql+=" and opt_Time <= to_date('"+endTime+"','yyyy-MM-dd')";
		}
		int count = this.dao.count(sql, null);
		page.setItemCount(count);
		List<TraceLog> logs = this.dao.searchPage(sql, TraceLog.class,  Integer.parseInt(page.getPageIndex()), Integer.parseInt(page.getPageSize()), null);
		return logs;
	}
	
	
	
}
