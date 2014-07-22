/**
 * SampleAspect.java
 * 2014-4-18
 */
package com.mtools.core.plugin.aop.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


/**
 * @author zhang
 *
 * 2014-4-18
 * 说明：面向切面编程
 * 解释一下(* com.tools.test.commons.service..*.*(..))中几个通配符的含义：
 * 第一个 * —— 通配 任意返回值类型
 * 第二个 * —— 通配 包com.evan.crm.service下的任意class
 * 第三个 * —— 通配 包com.evan.crm.service下的任意class的任意方法
 * 第四个 .. —— 通配 方法可以有0个或多个参数
 */
@Aspect
public class SampleAspect {

	@Pointcut("execution(* com.tools.test.commons.service..*.*(..))")
    public void inServiceLayer() {
		System.out.println("**************************************inServiceLayer**************************************");
    }
	
	@Before("execution(* com.tools.test.commons.service..*.*(..))")
	public void doBeforeInServiceLayer() {
		System.out.println("**************************************doBeforeInServiceLayer**************************************");
	}
	@After("execution(* com.tools.test.commons.service..*.*(..))")
	public void doAfterInServiceLayer() {
		System.out.println("**************************************doAfterInServiceLayer**************************************");
	}
}
