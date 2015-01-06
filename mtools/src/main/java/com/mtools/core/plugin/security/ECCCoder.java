package com.mtools.core.plugin.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
 
import javax.crypto.Cipher;
import javax.crypto.NullCipher;
 
import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
 
/**
 * ECC安全编码组件
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 * 
 * ECC 
	ECC-Elliptic Curves Cryptography，椭圆曲线密码编码学，是目前已知的公钥体制中，
	对每比特所提供加密强度最高的一种体制。在软件注册保护方面起到很大的作用，一般的序列号通常由该算法产生。 
         当我开始整理《Java加密技术（二）》的时候，我就已经在开始研究ECC了，
         但是关于Java实现ECC算法的资料实在是太少了，无论是国内还是国外的 资料，无论是官方还是非官方的解释，
         最终只有一种答案——ECC算法在jdk1.5后加入支持，目前仅仅只能完成密钥的生成与解析。 
         如果想要获得ECC算法实现，需要调用硬件完成加密/解密（ECC算法相当耗费资源，如果单纯使用CPU进行加密/解密，
         效率低下），涉及到Java Card领域，PKCS#11。
          其实，PKCS#11配置很简单，但缺乏硬件设备，无法尝试！ 
 * 
 */
public abstract class ECCCoder extends Coder {
 
    public static final String ALGORITHM = "EC";
    private static final String PUBLIC_KEY = "ECCPublicKey";
    private static final String PRIVATE_KEY = "ECCPrivateKey";
 
    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
 
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = ECKeyFactory.INSTANCE;
 
        ECPrivateKey priKey = (ECPrivateKey) keyFactory
                .generatePrivate(pkcs8KeySpec);
 
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
                priKey.getParams());
 
        // 对数据解密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());
 
        return cipher.doFinal(data);
    }
 
    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String privateKey)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(privateKey);
 
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = ECKeyFactory.INSTANCE;
 
        ECPublicKey pubKey = (ECPublicKey) keyFactory
                .generatePublic(x509KeySpec);
 
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
                pubKey.getParams());
 
        // 对数据加密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());
 
        return cipher.doFinal(data);
    }
 
    /**
     * 取得私钥
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
 
        return encryptBASE64(key.getEncoded());
    }
 
    /**
     * 取得公钥
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
 
        return encryptBASE64(key.getEncoded());
    }
 
    /**
     * 初始化密钥
     * 
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        BigInteger x1 = new BigInteger(
                "2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
        BigInteger x2 = new BigInteger(
                "289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);
 
        ECPoint g = new ECPoint(x1, x2);
 
        // the order of generator
        BigInteger n = new BigInteger(
                "5846006549323611672814741753598448348329118574063", 10);
        // the cofactor
        int h = 2;
        int m = 163;
        int[] ks = { 7, 6, 3 };
        ECFieldF2m ecField = new ECFieldF2m(m, ks);
        // y^2+xy=x^3+x^2+1
        BigInteger a = new BigInteger("1", 2);
        BigInteger b = new BigInteger("1", 2);
 
        EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);
 
        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
                n, h);
        // 公钥
        ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);
 
        BigInteger s = new BigInteger(
                "1234006549323611672814741753598448348329118574063", 10);
        // 私钥
        ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
 
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
 
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
 
        return keyMap;
    }
 
}