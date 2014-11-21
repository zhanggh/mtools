///**
// * @author 张广海
// * @date 2014-7-22
// */
//package com.mtools.core.plugin.groovy;
//
//import groovy.lang.GroovyClassLoader;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.codehaus.groovy.control.CompilationFailedException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
///**
// *  功能：
// */
//@Component("gvyFactory")
//@Lazy
//public class GroovyBeanFactory<T> {
//
//	@Value("${groovyScript}")
//	String gvyScript;
//	
//	public String getGvyScript() {
//		return gvyScript;
//	}
//
//	public void setGvyScript(String gvyScript) {
//		this.gvyScript = gvyScript;
//	}
//
//	public T getBeanFrmGvy() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException{
//		File file = new File(gvyScript);
//		ClassLoader cl = GroovyBeanFactory.class.getClassLoader();
//	    GroovyClassLoader groovyCl = new GroovyClassLoader(cl);
//		Class<T> groovyClass = groovyCl.parseClass(file);
//		T convertObj = groovyClass.newInstance();
//		return convertObj;
//	}
//	
//	public static void main(String[] args) throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException {
//		GroovyBeanFactory<GroovyConvert> gvyFactory=new GroovyBeanFactory<GroovyConvert>();
//		gvyFactory.setGvyScript("src/main/java/com/mtools/core/plugin/groovy/GroovyTest.groovy");
//		gvyFactory.getBeanFrmGvy().printName();
//	}
//}
