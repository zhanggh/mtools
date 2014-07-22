/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-6-24
 * version 1.0
 * 说明：
 */
package com.mtools.core.plugin.freemark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mtools.core.plugin.helper.SpringUtil;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 *  功能：静态模板工具
 * @date 2014-6-24
 */
@Service("freemarkerUtils")
public class FreemarkerUtils {

	@Resource(name="freeMarkerConfigurer")
	public FreeMarkerConfigurer freeMarkerConfigurer;
	
	private ServletContext servletContext;
	
	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public FreemarkerUtils()
    {
    }

    public static String process(String template, Map model)
    {
        Configuration configuration = null;
        FreeMarkerConfigurer freemarkerconfigurer = (FreeMarkerConfigurer)SpringUtil.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
        if(freemarkerconfigurer != null)
            configuration = freemarkerconfigurer.getConfiguration();
        return process(template, model, configuration);
    }

    public static String process(String template, Map model, Configuration configuration)
    {
        if(template == null)
            return null;
        if(configuration == null)
            configuration = new Configuration();
        StringWriter stringwriter = new StringWriter();
        try
        {
            new Template("template", new StringReader(template), configuration).process(model, stringwriter);
        }
        catch(TemplateException templateexception)
        {
            templateexception.printStackTrace();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return stringwriter.toString();
    }

    public static <T> T getParameter(String name, Class<T> type, Map<String, TemplateModel> params) throws TemplateModelException
    {
      Assert.hasText(name);
      Assert.notNull(type);
      Assert.notNull(params);
      TemplateModel model = (TemplateModel)params.get(name);
      if (model == null)
        return null;
      Object obj = DeepUnwrap.unwrap(model);
      return (T) convertUtilsBean.convert(obj, type);
    }

    public static TemplateModel getVariable(String name, Environment env) throws TemplateModelException
    {
        Assert.hasText(name);
        Assert.notNull(env);
        return env.getVariable(name);
    }

    public static void setVariable(String name, Object value, Environment env) throws TemplateModelException
    {
        Assert.hasText(name);
        Assert.notNull(env);
        if(value instanceof TemplateModel)
            env.setVariable(name, (TemplateModel)value);
        else
            env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
    }

    public static void setVariables(Map variables, Environment env) throws TemplateModelException
    {
        Assert.notNull(variables);
        Assert.notNull(env);
        for(Iterator iterator = variables.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String s = (String)entry.getKey();
            Object obj = entry.getValue();
            if(obj instanceof TemplateModel)
                env.setVariable(s, (TemplateModel)obj);
            else
                env.setVariable(s, ObjectWrapper.BEANS_WRAPPER.wrap(obj));
        }

    }

    private static final ConvertUtilsBean convertUtilsBean;

    static 
    {
        convertUtilsBean = new FreemarkerUtils().new FreemarkerInner();
        DateConverter dateconverter = new DateConverter();
        dateconverter.setPatterns(CommonAttributes.DATE_PATTERNS);
        convertUtilsBean.register(dateconverter, Date.class);
    }

    

         class FreemarkerInner extends ConvertUtilsBean
        {

            public String convert(Object value)
            {
                if(value != null)
                {
                    Class class1 = value.getClass();
                    if(class1.isEnum() && super.lookup(class1) == null)
                        super.register(new EnumConverter(class1), class1);
                    else
                    if(class1.isArray() && class1.getComponentType().isEnum())
                    {
                        if(super.lookup(class1) == null)
                        {
                            ArrayConverter arrayconverter = new ArrayConverter(class1, new EnumConverter(class1.getComponentType()), 0);
                            arrayconverter.setOnlyFirstToString(false);
                            super.register(arrayconverter, class1);
                        }
                        Converter converter = super.lookup(class1);
                        return (String)converter.convert(String.class, value);
                    }
                }
                return super.convert(value);
            }

            public Object convert(String value, Class clazz)
            {
                if(clazz.isEnum() && super.lookup(clazz) == null)
                    super.register(new EnumConverter(clazz), clazz);
                return super.convert(value, clazz);
            }

            public Object convert(String values[], Class clazz)
            {
                if(clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null)
                    super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
                return super.convert(values, clazz);
            }

            public Object convert(Object value, Class targetType)
            {
                if(super.lookup(targetType) == null)
                    if(targetType.isEnum())
                        super.register(new EnumConverter(targetType), targetType);
                    else
                    if(targetType.isArray() && targetType.getComponentType().isEnum())
                    {
                        ArrayConverter arrayconverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
                        arrayconverter.setOnlyFirstToString(false);
                        super.register(arrayconverter, targetType);
                    }
                return super.convert(value, targetType);
            }

            FreemarkerInner()
            {
            }
        }
         
         

         /**
         * 功能：构建静态文件
         * 2014-6-26
         */
        @Transactional(readOnly=true)
         public int buildFile(String templatePath, String staticPath, Map<String, Object> model)
         {
           Assert.hasText(templatePath);
           Assert.hasText(staticPath);
           FileOutputStream fileOutputStream = null;
           OutputStreamWriter outputStreamWriter = null;
           BufferedWriter fileBuffer = null;
           try
           {
             freemarker.template.Template template = this.freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
             File outFile = null;
             if(this.servletContext!=null){
            	 outFile = new File(this.servletContext.getRealPath(staticPath));
             }else{
            	 outFile = new File(staticPath); 
             }
             File outDir = outFile.getParentFile();
             if (!outDir.exists())
            	 outDir.mkdirs();
             fileOutputStream = new FileOutputStream(outFile);
             outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
             fileBuffer = new BufferedWriter(outputStreamWriter);
             template.process(model, fileBuffer);
             fileBuffer.flush();
             return 1;
           }
           catch (Exception ex)
           {
        	   ex.printStackTrace();
           }
           finally
           {
             IOUtils.closeQuietly(fileBuffer);
             IOUtils.closeQuietly(outputStreamWriter);
             IOUtils.closeQuietly(fileOutputStream);
           }
           return 0;
         }
}
