package com.ztools.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mtools.core.plugin.security.RSACoder;
 
/**
 * 
 * @author zhanggh
 * @version 1.0
 * @since 1.0
 */
public class RSACoderTest {
    private String publicKey;
    private String privateKey;
 
    @Before
    public void setUp() throws Exception {
        Map<String, Object> keyMap = RSACoder.initKey();
 
        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);
        System.err.println("公钥: \n\r" + publicKey);
        System.err.println("私钥： \n\r" + privateKey);
    }
 
    @Test
    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
 
        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
 
        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,
                privateKey);
 
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);
 
    }
 
    @Test
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
 
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);
 
        byte[] decodedData = RSACoder
                .decryptByPublicKey(encodedData, publicKey);
 
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);
 
        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RSACoder.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
 
        // 验证签名
        boolean status = RSACoder.verify(encodedData, publicKey, sign);
        System.err.println("状态:\r" + status);
        assertTrue(status);
 
    }
 
}