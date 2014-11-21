package com.mtools.core.plugin.notify;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
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
@Scope("prototype")
@Component("sysRunningNotify")
public class SystemRunningNotify extends BasePlugin implements AsyncNotify{

	@Autowired
	AuthPlugin auth;
	List<String> to;//目标邮件地址
	List<String> fileList;//附件
	String context;//邮件内容
	String subject;//邮件主题
	/**
	 *  01系统异常通知 02对账文件发送
	 */
	String mailType;//邮件通知类型

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
		this.mailImpl.Send(sb, fileList);
		return true;
	}
 
	public void run() {
		try {
			String sql="select * from emailconfig e where e.apptype=?";
			List<Emailconfig> econfigs=this.dao.search(sql, Emailconfig.class,mailType);
			if(econfigs!=null&&econfigs.size()>0){
				for(Emailconfig cfg:econfigs){
					sendEmail(cfg);
				}
				log.info("邮件发送成功");
			}else{
				log.info("没有代发邮件");
			}
		} catch (Exception e) {
			log.error("邮件通知服务发生异常",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param request
	 * @param e
	 * @param appName 应用名
	 * @throws Exception 
	 */
	public void initData(ServletRequest request,Throwable e,String appName){
		try {
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
		} catch (Exception e1) {
			log.error("邮件服务发生异常",e1);
			e1.printStackTrace();
		}
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

	public List<String> getFileList() {
		if(this.fileList==null)
			this.fileList=Lists.newArrayList();
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
}
