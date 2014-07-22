package com.mtools.core.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制类的方法上加入这个注解，该方法的访问则不需要登录判断
 * @author zhang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthLogin {
	String value() default "";
}
