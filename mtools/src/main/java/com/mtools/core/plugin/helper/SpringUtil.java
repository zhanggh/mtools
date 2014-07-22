package com.mtools.core.plugin.helper;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @author zhang
 * spring上下文工具，可以获取xml中配置的bean也可以或者annotation产生的bean
 * 如果这个Bean已经实现了ApplicationContextAware接口，会调用setApplicationContext(ApplicationContext)方法，
 * 传入Spring上下文（同样这个方式也可以实现步骤4的内容，
 * 2014-4-14
 */
//@Component("springUtil")
//@Lazy(false)
public class SpringUtil implements DisposableBean, ApplicationContextAware
{
	public final static Logger lg=Logger.getLogger(SpringUtil.class);
	
	private static Map<String ,Object> beansMap=new HashMap<String ,Object>();
	
	private static ApplicationContext appContext;
	protected static ApplicationContext cxt;
	protected static String springCfg = null;
	protected static boolean loaded=false;
	private final static String DEF_CFG="appcxt.xml";
	public final static String CFG_KEY="coxcmn.spring.cfg";
	public static Resource getRes(String f)
	{
		Resource res = new ClassPathResource(f);
		return res;
	}
	public static String resPath(String f)
	{
		try
		{
			File fx=new File(f);
			if(fx.exists()) return fx.getAbsolutePath();
			Resource res = new ClassPathResource(f);
			return res.getFile().getAbsolutePath();
		}
		catch(Exception e)
		{
			Auxs.lg.error("error get resource path=["+f+"],Using itself "+e.getMessage());
		}
		return f;
	}
	public static URL cfgPath(String f)
	{
		try
		{
			
			return getRes(f).getURL();
		}
		catch(Exception e)
		{
			return null; //contextRoot+"/"+f;
		}
	}
	public static Object getBean(Map<String,Object> m,String name,Object def)
	{
		String val=(String) Auxs.fetch(m, name, def);
		return getBean(val);
	}
	public static Object getBean(Map<String,Object> m,String name)
	{
		String val=(String) m.get(name);
		return getBean(val);
	}
	public static ApplicationContext loadCxt(String path)
	{
		try
		{
			return new ClassPathXmlApplicationContext(path);
		}
		catch(Throwable t)
		{
			try
			{
				if(t.getCause() instanceof FileNotFoundException)
					return new FileSystemXmlApplicationContext(path);
			}
			catch(Throwable t1)
			{
				lg.error("loading spring context fail",t1);
				return null;
			}
			lg.error("loading spring context fail",t);
		}
		return null;
	}
	public static boolean isLoad()
	{
		return cxt!=null;
	}
	public static ApplicationContext getCxt()
	{
		if(cxt!=null) return cxt;
		return getCxtSync();
	}
	private synchronized static ApplicationContext getCxtSync()
	{
		try
		{
			if(cxt!=null) return cxt;
			if(loaded)
			{
				Auxs.lg.error("初始化"+springCfg+"失败，load=true cxt=null");
				throw new Exception("递归死循环加载spring配置,请检查类初始化功能");
			}
			loaded=true;
			if(springCfg==null) springCfg=System.getProperty(CFG_KEY);
			if(springCfg==null)
			{
				lg.info("system.property "+CFG_KEY+" NOT Set,Using Default Spring Context "+DEF_CFG);
				springCfg=DEF_CFG;
			}
			URL url=cfgPath(springCfg);
			String path;
			if(url!=null) path=url.toString();
			else path=springCfg;
			lg.info("Loading Spring Context:"+path);
			cxt = loadCxt(path);
			if(cxt==null)
			{
				loaded=false;
				lg.error("Loading Spring Context Fail:"+path);
			}
		}
		catch(Throwable t)
		{
			lg.error("error ",t);
			System.exit(1);
		}
		return cxt;
	}
	public static void initCxt(ApplicationContext ac)
	{
		cxt=ac;
	}
	public static Object getBean(String beanName)
	{
		ApplicationContext cxt=getCxt();
		if(cxt==null) return null; //throw new RuntimeException("spring context不存在");
		return cxt.getBean(beanName);
	}
	public static Object bean(String beanName)
	{
		return getBean(beanName);
	}
	public static <T> T bean(String beanName,Class<T> clz)
	{
		return clz.cast(getBean(beanName));
	}
	public static <T> T getBean(Class<T> clz)
	{
		return clz.cast(getBean(clz.getName()));
	}
	public synchronized static void initSpringCfg(String cfg)
	{
		springCfg=cfg;
		System.setProperty(CFG_KEY, cfg);
	}
	public synchronized static void initCfgIfNotSet(String cfg)
	{
		if(Auxs.empty(System.getProperty(CFG_KEY))&&springCfg==null)
		{
			initSpringCfg(cfg);
		}
	}
	public synchronized static void load(String cfg)
	{
		initCfgIfNotSet(cfg);
		getCxt();
	}
	 

	public static void putBean(String beanName, Object obj) {
		beansMap.put(beanName, obj);
	}
	public static Object getBeanFromMap(String beanName) {
		return beansMap.get(beanName);
	}
	/**  
	 * 功能：初始化bean
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}
	/**  
	 * 功能：
	 */
	public void destroy() throws Exception {
		 appContext = null;
	}
	
	 public static ApplicationContext getApplicationContext()
	  {
	    return appContext;
	  }

	  /**
	 * 功能：获取annotation 产生的bean
	 * 2014-4-14
	 */
	public static Object getAnoBean(String name)
	  {
	    Assert.hasText(name);
	    if(appContext==null)
	    	appContext=SpringUtil.getCxt();
	    return appContext.getBean(name);
	  }

	  /**
	 * 功能：获取annotation 产生的bean
	 * 2014-4-14
	 */
	public static <T> T getBean(String name, Class<T> type)
	  {
	    Assert.hasText(name);
	    Assert.notNull(type);
	    return appContext.getBean(name, type);
	  }
}
