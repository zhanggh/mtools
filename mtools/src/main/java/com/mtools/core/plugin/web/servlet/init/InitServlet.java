package com.mtools.core.plugin.web.servlet.init;

import java.io.IOException;
import java.security.Security;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mtools.core.plugin.db.CoreDao;
import com.mtools.core.plugin.helper.FuncUtil;
import com.mtools.core.plugin.helper.SpringUtil;
//import com.mtools.core.plugin.staticres.StaticResPlugin;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 111111L;
	protected static final Log log = LogFactory.getLog(InitServlet.class);

	private String log4j;
	public void init() throws ServletException {

		try {
			//自定义log4j配置
			if(!FuncUtil.isEmpty(log4j)){
				PropertyConfigurator.configure(SpringUtil.cfgPath(log4j));
			}
			log.info("初始化spring容器");
			log.info("SpringUtil init");
			ServletContext context = getServletContext();
//		WebApplicationContext webAppContext = WebApplicationContextUtils
//				.getWebApplicationContext(context);
			ApplicationContext ctx = WebApplicationContextUtils
					.getWebApplicationContext(context);
			SpringUtil.initCxt(ctx);
			CoreDao dao = (CoreDao) SpringUtil.getAnoBean("dao");
//		CoreDao daoExt = (CoreDao) SpringUtil.getAnoBean("daoExt");
//		CoreDao daoExt = (CoreDao) SpringUtil.getBean("daoExt");
			List<String> values = dao.search("select 1 from dual", String.class,
					null);
//		values = daoExt.search("select 1 from dual", String.class,
//				null);
			//使用BouncyCastleProvider提供的安全策略 需要引入bcprov-jdk16-1.46.jar
			Security.addProvider(new BouncyCastleProvider());
			
			log.info("加载系统参数成功");
		} catch (Exception e) {
			log.error("初始化服务应用发生异常", e);
			e.printStackTrace();
		}
	}
	/**  
	 * 功能：
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.log4j=config.getInitParameter("log4j");
		super.init(config);
	}
	
	
}
