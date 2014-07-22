package com.mtools.core.plugin.notify;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.auth.AuthPlugin;
import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.entity.Emailconfig;
import com.mtools.core.plugin.entity.UserInfo;
import com.mtools.core.plugin.helper.FuncUtil;
 

/**
 * @author zhang
 * 系统异常监控
 */
@Component("sysRunningNotify")
public class SystemRunningNotify extends BasePlugin implements AsyncNotify{

	@Autowired
	AuthPlugin auth;
	List<String> to;//目标邮件地址
	String context;//邮件内容
	String subject;//邮件主题

	/**
	 * 功能：发送邮件
	 * 2014-4-14
	 */
	private boolean sendEmail(Emailconfig cfg){
		if(FuncUtil.isEmpty(context)){
			context=cfg.getContext();
		}
//		if(this.to==null)
		to=new ArrayList<String>();
		String[] emails=cfg.getToemails().split(",");
		for(String e:emails){
			to.add(e);
		}
		StringBuffer sb=new StringBuffer(context);
		this.mailImpl.getMailparam().setTo(to);
		if(FuncUtil.isEmpty(subject)){
			subject=cfg.getSubject();
		}
		this.mailImpl.getMailparam().setSubject(subject);
		this.mailImpl.getMailparam().setContent(context);
		log.info("Subject:"+subject);
		log.info("Smtpport:"+this.mailImpl.getMailparam().getSmtpport());
		log.info("Frommail:"+this.mailImpl.getMailparam().getFrommail());
		log.info("To:"+this.mailImpl.getMailparam().getTo());
		log.info("Content:"+this.mailImpl.getMailparam().getContent());
		this.mailImpl.Send(sb, null);
		return true;
	}
 
	public void run() {
		String sql="select * from emailconfig e where e.apptype=?";
		List<Emailconfig> econfigs=this.dao.search(sql, Emailconfig.class, CoreConstans.EXCEPTON_01);
		if(econfigs!=null&&econfigs.size()>0){
			for(Emailconfig cfg:econfigs){
				sendEmail(cfg);
			}
			log.info("邮件发送成功");
		}else{
			log.info("没有代发邮件");
		}
	}
	
	/**
	 * @param request
	 * @param e
	 * @param appName 应用名
	 * @throws IOException
	 */
	public void initData(ServletRequest request,Throwable e,String appName) throws IOException{
		this.setSubject(appName+"系统异常监控日志");
		StringBuffer error=new StringBuffer();
		String path="";
		if(request!=null){
			error.append("访问ip："+request.getRemoteHost()+"访问端口："+request.getRemotePort()+"\n");
			path = ((HttpServletRequest) request).getServletPath();
			log.info("访问地址:"+path);
			UserInfo user=(UserInfo) ((HttpServletRequest) request).getSession().getAttribute(CoreConstans.LOGINGUSER);
			if(request.getAttribute("trxCode")!=null){
				error.append("app请求码:");
				error.append(request.getAttribute("trxCode"));
				error.append("\n\n<br/><br/>");
			}
			if(user!=null){
				error.append("操作为用户号:");
				error.append(user.getUserid());
				error.append("\n\n<br/><br/>");
				error.append("操作为人姓名:");
				error.append(user.getUsername());
				error.append("\n\n<br/><br/>");
			}
			error.append("异常操作行为:");
			if(auth.getPermByUri(path)!=null){
				error.append(auth.getPermByUri(path).getPermname());
				error.append(" : ");
			}
		}

//		File epath=new File("logs");//异常临时文件
//		File errorfile = File.createTempFile("error", ".er", epath);
//		OutputStream out =new FileOutputStream(errorfile);
//		PrintStream printstream = new PrintStream(out);
//		e.printStackTrace(printstream);
//		String errormsg=FileUtils.readFileToString(errorfile, "UTF-8");

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		String errormsg = stringWriter.toString().replaceAll("\\r?\\n", "</br>");
		
		
		error.append(path);
		error.append("\n\n<br/><br/>");
		error.append("异常信息:\n<br/><br/>");
		error.append(e.getMessage());
		error.append("\n\n<br/><br/>");
		error.append(errormsg);
		error.append("\n\n<br/><br/>");
		this.setContext(error.toString());
	}
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
