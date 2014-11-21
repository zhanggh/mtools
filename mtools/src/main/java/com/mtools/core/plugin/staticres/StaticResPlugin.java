///**
// * 通联支付-研发中心
// * @author zhanggh
// * 2014-6-30
// * version 1.0
// * 说明：静态资源处理器
// */
//package com.mtools.core.plugin.staticres;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.stereotype.Component;
//
//import com.google.common.collect.Maps;
//import com.mtools.core.plugin.BasePlugin;
//import com.mtools.core.plugin.freemark.FreemarkerUtils;
//import com.mtools.core.plugin.helper.SpringUtil;
//
///**
// *  功能：
// * @date 2014-6-30
// */
//@Component("staticResPlugin")
//public class StaticResPlugin extends BasePlugin {
//
//	@Resource(name="freemarkerUtils")
//	FreemarkerUtils freemarkerUtils;
//	public boolean initStaticFile(ServletContext servletContext){
//		log.info("*************创建静态文件*************");
//		freemarkerUtils.setServletContext(servletContext);
//		Map<String, Object> model=Maps.newConcurrentMap();
//		model.put("ctx", "http://172.16.1.11:8282/techsp");
//		model.put("serverPath", "http://172.16.1.11:8282/");
//		model.put("serverHost", "172.16.1.11");
//		model.put("serverName",SpringUtil.getApplicationContext().getApplicationName().substring(1));
//		model.put("title", "通联支付帮助中心");
//		model.put("index", true);
//		freemarkerUtils.buildFile("index.ftl", "index.html", model);
//		
//		log.info("*************创建静态文件完成*************");
//		return true;
//	}
//	 
//}
