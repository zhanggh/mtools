/**
 * JCaptcha.java
 * 2014-4-22
 */
package com.mtools.core.plugin.web.jcaptcha;

/**
 * @author zhang
 *
 * 2014-4-22
 */

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-3-22 下午3:46
 * <p>Version: 1.0
 */
/**
 *  功能：
 */
public class JCaptcha {
    public static final EsManageableImageCaptchaService captchaService = new EsManageableImageCaptchaService(new FastHashMapCaptchaStore(), new GMailEngine(), 180, 100000, 75000);


    
    /**
     * 验证码校验
     * @param request
     * @param userCaptchaResponse
     * @return
     */
    public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse) {
        if (request.getSession(false) == null) return false;

        boolean validated = false;
        try {
            String id = request.getSession().getId();
            validated = captchaService.validateResponseForID(id, userCaptchaResponse).booleanValue();
        } catch (CaptchaServiceException e) {
            e.printStackTrace();
        }
        return validated;
    }

    public static boolean hasCaptcha(HttpServletRequest request, String userCaptchaResponse) {
        if (request.getSession(false) == null) return false;
        boolean validated = false;
        try {
            String id = request.getSession().getId();
            validated = captchaService.hasCapcha(id, userCaptchaResponse);
        } catch (CaptchaServiceException e) {
            e.printStackTrace();
        }
        return validated;
    }

}
