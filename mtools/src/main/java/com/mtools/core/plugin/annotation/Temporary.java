package com.mtools.core.plugin.annotation;
 
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 临时属性，不需要参与数据库的 增删改查
 * @author zhanggh
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Temporary {
	String value() default "";
}
