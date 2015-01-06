package com.ztools.security;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.mtools.core.plugin.security.ECCCoder;
 
/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class ECCCoderTest {
 
    @Test
    public void test() throws Exception {
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
 
        Map<String, Object> keyMap = ECCCoder.initKey();
 
        String publicKey = ECCCoder.getPublicKey(keyMap);
        String privateKey = ECCCoder.getPrivateKey(keyMap);
        System.err.println("公钥: \n" + publicKey);
        System.err.println("私钥： \n" + privateKey);
 
        byte[] encodedData = ECCCoder.encrypt(data, publicKey);
 
        byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);
 
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);
    }
}