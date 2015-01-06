package com.ztools.security;
 
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

import com.mtools.core.plugin.security.CertificateCoderSSL;
 
/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class CertificateCoderSSLTest {
    private String password = "123456";
    private String alias = "www.zlex.org";
    private String certificatePath = "d:/zlex.cer";
    private String keyStorePath = "d:/zlex.keystore";
    private String clientKeyStorePath = "d:/zlex-client.keystore";
    private String clientPassword = "654321";
 
    @Test
    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "Ceritifcate";
        byte[] data = inputStr.getBytes();
 
        byte[] encrypt = CertificateCoderSSL.encryptByPublicKey(data,
                certificatePath);
 
        byte[] decrypt = CertificateCoderSSL.decryptByPrivateKey(encrypt,
                keyStorePath, alias, password);
        String outputStr = new String(decrypt);
 
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
 
        // 验证数据一致
        assertArrayEquals(data, decrypt);
 
        // 验证证书有效
        assertTrue(CertificateCoderSSL.verifyCertificate(certificatePath));
 
    }
 
    @Test
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
 
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
 
        byte[] encodedData = CertificateCoderSSL.encryptByPrivateKey(data,
                keyStorePath, alias, password);
 
        byte[] decodedData = CertificateCoderSSL.decryptByPublicKey(encodedData,
                certificatePath);
 
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);
 
        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = CertificateCoderSSL.sign(encodedData, keyStorePath, alias,
                password);
        System.err.println("签名:\r" + sign);
 
        // 验证签名
        boolean status = CertificateCoderSSL.verify(encodedData, sign,
                certificatePath);
        System.err.println("状态:\r" + status);
        assertTrue(status);
 
    }
 
    @Test
    public void testHttps() throws Exception {
        URL url = new URL("https://www.zlex.org/examples/");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
 
        conn.setDoInput(true);
        conn.setDoOutput(true);
 
        CertificateCoderSSL.configSSLSocketFactory(conn, clientPassword,
                clientKeyStorePath, clientKeyStorePath);
 
        InputStream is = conn.getInputStream();
 
        int length = conn.getContentLength();
 
        DataInputStream dis = new DataInputStream(is);
        byte[] data = new byte[length];
        dis.readFully(data);
 
        dis.close();
        System.err.println(new String(data));
        conn.disconnect();
    }
}