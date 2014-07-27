/**
 * AjaxJCaptchaValidateController.java
 * 2014-4-22
 */
package com.mtools.core.plugin.auth.web;
 

 
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtools.core.plugin.entity.ValidateResponse;
import com.mtools.core.plugin.web.jcaptcha.JCaptcha;

/**
 * jcaptcha 验证码验证
 * <p>User: Zhang
 
 */
@Controller
@RequestMapping("/jcaptcha-validate")
public class AjaxJCaptchaValidateController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object jqueryValidationEngineValidate(
            HttpServletRequest request,
            @RequestParam(value = "fieldId", required = false) String fieldId,
            @RequestParam(value = "fieldValue", required = false) String fieldValue) {

        ValidateResponse response = ValidateResponse.newInstance();

        if (JCaptcha.hasCaptcha(request, fieldValue) == false) {
            response.validateFail(fieldId,"验证码错误");
        } else {
            response.validateSuccess(fieldId,"验证码正确");
        }

        return response.result();
    }
}