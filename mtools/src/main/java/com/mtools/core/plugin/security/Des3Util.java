package com.mtools.core.plugin.security;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Des3Util {
    private static final String ALGORITHM = "DESede";

    //定义 加密算法,可用 DES,DESede,Blowfish        
    //keybyte为加密密钥，长度为24字节    
    //src为被加密的数据缓冲区（源）    

    /**
     * 设置数据流为加密模式
     * @param secretKey
     * @param outputStream
     * @return
     */
    public static CipherOutputStream encryptMode(SecretKey secretKey, OutputStream outputStream) {
        try {
            //加密            
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, secretKey);
            return new CipherOutputStream(outputStream, c1);

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置数据流为加密模式
     * @param des3Key
     * @param outputStream
     * @return
     */
    public static CipherOutputStream encryptMode(String des3Key, OutputStream outputStream) {
        //生成密钥
        SecretKey secretKey = new SecretKeySpec(des3Key.getBytes(), ALGORITHM);
        return encryptMode(secretKey, outputStream);

    }

    /**
     * 设置数据流为解密模式
     * @param des3Key
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(String des3Key, InputStream inputStream) {
        //生成密钥          
        SecretKey deskey = new SecretKeySpec(des3Key.getBytes(), ALGORITHM); //解密         
        return decryptMode(deskey, inputStream);
    }

    /**
     * 设置数据流为解密模式
     * @param secretKey
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(SecretKey secretKey, InputStream inputStream) {
        try {
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, secretKey);
            return new CipherInputStream(inputStream, c1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
