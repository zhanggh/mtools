package com.mtools.core.plugin.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

/**
 * @author 张广海
 *  功能：
 */
public final class RSAUtils
{
  private static final Provider provider = new BouncyCastleProvider();
  private static final int size = 2048;
  private static String seed = "12345678";

  public static KeyPair generateKeyPair()
  {
	long start = System.currentTimeMillis();
    try
    {
      KeyPairGenerator localKeyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
      localKeyPairGenerator.initialize(size, new SecureRandom(seed.getBytes()));
      long end = System.currentTimeMillis();
      
      System.out.println("产生密钥对耗时："+(end-start));
      return localKeyPairGenerator.generateKeyPair();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return null;
  }

  public static byte[] encrypt(PublicKey publicKey, byte[] data)
  {
    Assert.notNull(publicKey);
    Assert.notNull(data);
    try
    {
      Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
      localCipher.init(1, publicKey);
      return localCipher.doFinal(data);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    return null;
  }

  public static String encrypt(PublicKey publicKey, String text)
  {
    Assert.notNull(publicKey);
    Assert.notNull(text);
    byte[] arrayOfByte = encrypt(publicKey, text.getBytes());
    return arrayOfByte != null ? Base64.encodeBase64String(arrayOfByte) : null;
  }

  public static byte[] decrypt(PrivateKey privateKey, byte[] data)
  {
    Assert.notNull(privateKey);
    Assert.notNull(data);
    try
    {
      Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
      localCipher.init(2, privateKey);
      return localCipher.doFinal(data);
    }
    catch (Exception ex)
    {
    	ex.printStackTrace();
    }
    return null;
  }

  public static String decrypt(PrivateKey privateKey, String text)
  {
    Assert.notNull(privateKey);
    Assert.notNull(text);
    byte[] arrayOfByte = decrypt(privateKey, Base64.decodeBase64(text));
    return arrayOfByte != null ? new String(arrayOfByte) : null;
  }
  
  public static String sign(PrivateKey privateKey, String msg) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException{
	  Assert.notNull(privateKey);
	    Assert.notNull(msg);
	    Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initSign(privateKey);
		sign.update(msg.getBytes("GBK"));
		byte signed[] = sign.sign();
		byte sign_asc[] = new byte[signed.length * 2];
		Hex2Ascii(signed.length, signed, sign_asc);
		return  new String(sign_asc);
  }
  
  public static boolean verify(PublicKey publicKey, String signedMsg,String orgMsg) throws Exception{
	  Signature verify = Signature.getInstance("SHA1withRSA");
		verify.initVerify(publicKey);
		byte signeddata[] = new byte[signedMsg.length() / 2];
		Ascii2Hex(signedMsg.length(), signedMsg.getBytes("GBK"), signeddata);
		verify.update(orgMsg.getBytes("GBK"));
		boolean result;
		if (verify.verify(signeddata))
		{
			result = true;
		}
		else
		{
			result = false;
			throw new Exception("验签失败");
		}
		return result;
  }
  
  public static void main(String[] args) throws Exception{
	  RSAUtils.seed="111111345213524";
	  KeyPair  kpair = RSAUtils.generateKeyPair();
	  long start = System.currentTimeMillis();
	 String orgStr="zhanggh";
//	 String encStr = RSAUtils.encrypt(kpair.getPublic(),orgStr);
//	 System.out.println(encStr);
//	 orgStr = RSAUtils.decrypt(kpair.getPrivate(), encStr);
//	 System.out.println(orgStr);
	 
	 String signedMsg = RSAUtils.sign(kpair.getPrivate(), orgStr);
	 System.out.println("签名后：\n"+signedMsg);
	 boolean flag = RSAUtils.verify(kpair.getPublic(), signedMsg, orgStr);
	 System.out.println("验签结果：\n"+flag);
	 long end = System.currentTimeMillis();
	 
	 System.out.println("验签耗时：\n"+(end-start));
  }
  
	/**
	 * 将十六进制数据转换成ASCII字符串
	 * 
	 * @param len
	 *            十六进制数据长度
	 * @param data_in
	 *            待转换的十六进制数据
	 * @param data_out
	 *            已转换的ASCII字符串
	 */
	private static void Hex2Ascii(int len, byte data_in[], byte data_out[])
	{
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; i++)
		{
			temp1[0] = data_in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0x0f);
			temp2[0] = data_in[i];
			temp2[0] = (byte) (temp2[0] & 0x0f);
			if (temp1[0] >= 0x00 && temp1[0] <= 0x09)
			{
				(data_out[j]) = (byte) (temp1[0] + '0');
			}
			else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f)
			{
				(data_out[j]) = (byte) (temp1[0] + 0x57);
			}

			if (temp2[0] >= 0x00 && temp2[0] <= 0x09)
			{
				(data_out[j + 1]) = (byte) (temp2[0] + '0');
			}
			else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f)
			{
				(data_out[j + 1]) = (byte) (temp2[0] + 0x57);
			}
			j += 2;
		}
	}

	/**
	 * 将ASCII字符串转换成十六进制数据
	 * 
	 * @param len
	 *            ASCII字符串长度
	 * @param data_in
	 *            待转换的ASCII字符串
	 * @param data_out
	 *            已转换的十六进制数据
	 */
	private static void Ascii2Hex(int len, byte data_in[], byte data_out[])
	{
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; j++)
		{
			temp1[0] = data_in[i];
			temp2[0] = data_in[i + 1];
			if (temp1[0] >= '0' && temp1[0] <= '9')
			{
				temp1[0] -= '0';
				temp1[0] = (byte) (temp1[0] << 4);

				temp1[0] = (byte) (temp1[0] & 0xf0);

			}
			else if (temp1[0] >= 'a' && temp1[0] <= 'f')
			{
				temp1[0] -= 0x57;
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xf0);
			}

			if (temp2[0] >= '0' && temp2[0] <= '9')
			{
				temp2[0] -= '0';

				temp2[0] = (byte) (temp2[0] & 0x0f);

			}
			else if (temp2[0] >= 'a' && temp2[0] <= 'f')
			{
				temp2[0] -= 0x57;

				temp2[0] = (byte) (temp2[0] & 0x0f);
			}
			data_out[j] = (byte) (temp1[0] | temp2[0]);

			i += 2;
		}

	}

}
 