/**
 * CurrentUser.java
 * 2014-4-17
 */
package com.mtools.core.plugin.annotation;


import java.lang.annotation.*;

import com.mtools.core.plugin.constant.CoreConstans;

/**
 * @author zhang
 *
 * 2014-4-17
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    /**
     * 当前用户在request中的名字 默认{@link CoreConstans#LOGINGUSER}
     *
     * @return
     */
    String value() default CoreConstans.LOGINGUSER;
}
