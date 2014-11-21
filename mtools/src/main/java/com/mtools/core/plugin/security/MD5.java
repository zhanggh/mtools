package com.mtools.core.plugin.security;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.mtools.core.plugin.helper.FuncUtil;

/** 
 * @author 张广海
* 功能：通联网关MD5签名处理核心文件，不需要修改
* 版本：1.3
* 修改日期：2014-08-03
* 说明：
* 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
* 该代码仅供学习和研究通联网关接口使用，只是提供一个
* */

public class MD5 {
	private static Logger log = Logger.getLogger(MD5.class);

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text+"&key=" + key;
    	log.info("签名原字符串："+FuncUtil.filteSepcStr(text));
    	String signed=DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	log.info("签名后结果："+signed);
        return signed;
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text+"&key=" + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	log.info("验签名原文："+FuncUtil.filteSepcStr(text));
    	log.info("验签签名信息："+mysign);
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args){
    	String content ="childmerid=000000000000006&completetime=20141021102929&errorCode=0000&merchantId=1400100029&orderAmount=77000&orderDatetime=20141021102828&orderNo=141021220190149&payAmount=77000&payResult=支付成功&payType=0&returnDatetime=201410294102929&signType=1&transnumber=9999141021000751&version=v1.0";
    	String content2="&payType=1&returnDatetime=201408219125024";//&signType=1&transnumber=10042&version=1.0v&key=123456
    	//    	String org="zhanggh";
    	String key="1q2w3e4r5t6y7u8i9o";
    	String input_charset="UTF-8";
//    	String input_charset="GBK";
    	String signmsg=MD5.sign(content,key, input_charset);
    	System.err.println(signmsg);
//    	boolean verfiy_result=MD5.verify(org, signmsg, key, input_charset);
//    	if(content.equals(content2)){
//    		log.error("相等");
//    	}else{c05aa48639e613ead6a5dfe98f9f3dd0
//    		log.error("不相等");
//    	}
//    	String sign = DigestUtils.md5Hex(getContentBytes(content, "utf-8"));
//    	System.err.println(sign);
//    	sign = DigestUtils.md5Hex(getContentBytes(content2, "utf-8"));
//    	System.err.println(sign);
    }
}