/**
 * 
 */
package com.mtools.core.plugin.security;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * @author Administrator
 * 
 */
public class CryptNoRestrict implements CryptInf
{
	public static Provider provider = new BouncyCastleProvider();
	/**
	 * 构造函数
	 */
	public CryptNoRestrict()
	{
	}
	public CryptNoRestrict(String encoding)
	{
		this.encoding=encoding;
	}

	private String encoding = "GBK";

	/**
	 * 取出上次调用加密、解密、签名函数成功后的输出结果
	 */
	protected String lastResult;

	/**
	 * 返回上一次签名结果
	 */
	protected String lastSignMsg;

	/**
	 * 对字符串进行签名
	 * 
	 * @param TobeSigned
	 *            需要进行签名的字符串
	 * @param KeyFile
	 *            PFX证书文件路径
	 * @param PassWord
	 *            私钥保护密码
	 * @return 签名成功返回true(从LastResult属性获取结果)，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	public boolean SignMsg(final String TobeSigned, final String KeyFile, final String PassWord) throws Exception
	{
		boolean result = false;
		FileInputStream fiKeyFile = null;
		this.lastSignMsg = "";
		KeyStore ks = KeyStore.getInstance("PKCS12");
		// ks.load(new FileInputStream(KeyFile), PassWord.toCharArray());
		fiKeyFile = new FileInputStream(KeyFile);
		//PassWord.toCharArray()
		try
		{
			ks.load(fiKeyFile,PassWord.toCharArray());
		}
		catch(Exception ex)
		{
			if(fiKeyFile!=null) fiKeyFile.close();
			throw ex;
		}
		Enumeration myEnum = ks.aliases();
		String keyAlias = null;
		RSAPrivateCrtKey prikey = null;
		// keyAlias = (String) myEnum.nextElement();
		/* IBM JDK必须使用While循环取最后一个别名，才能得到个人私钥别名 */
		while (myEnum.hasMoreElements())
		{
			keyAlias = (String) myEnum.nextElement();
			// System.out.println("keyAlias==" + keyAlias);
			if (ks.isKeyEntry(keyAlias))
			{
				prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, PassWord.toCharArray());
				break;
			}
		}
		if (prikey == null)
		{
			result = false;
			throw new Exception("没有找到匹配私钥");
		}
		else
		{
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initSign(prikey);
			sign.update(TobeSigned.getBytes(encoding));
			byte signed[] = sign.sign();
			byte sign_asc[] = new byte[signed.length * 2];
			Hex2Ascii(signed.length, signed, sign_asc);
			this.lastResult = new String(sign_asc);
			this.lastSignMsg = this.lastResult;
			result = true;
		}
		return result;
	}

	/**
	 * 验证签名
	 * 
	 * @param TobeVerified
	 *            待验证签名的密文
	 * @param PlainText
	 *            待验证签名的明文
	 * @param CertFile
	 *            签名者公钥证书
	 * @return 验证成功返回true，失败返回false(从LastErrMsg属性获取失败原因)
	 */
	public boolean VerifyMsg(String TobeVerified, String PlainText, String CertFile) throws Exception
	{
		boolean result = false;
		FileInputStream certfile = null;
		certfile = new FileInputStream(CertFile);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		
		X509Certificate x509cert=null;
		try
		{
			x509cert = (X509Certificate) cf.generateCertificate(certfile);
		}
		catch(Exception ex)
		{
			if(certfile!=null) certfile.close();
			throw ex;
		}

		RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();
		Signature verify = Signature.getInstance("SHA1withRSA");
		verify.initVerify(pubkey);
		byte signeddata[] = new byte[TobeVerified.length() / 2];
		Ascii2Hex(TobeVerified.length(), TobeVerified.getBytes(encoding), signeddata);
		verify.update(PlainText.getBytes(encoding));
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

	/**
	 * 返回上次调用加密、解密、签名函数成功后的输出结果
	 * 
	 * @return 返回上次调用加密、解密、签名函数成功后的输出结果
	 */
	public String getLastResult()
	{
		return this.lastResult;
	}

	/**
	 * 返回上一次签名结果
	 * 
	 * @return 签名结果
	 */
	public String getLastSignMsg()
	{
		return this.lastSignMsg;
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

	protected String replaceAll(String strURL, String strAugs)
	{

		// JDK1.3中String类没有replaceAll的方法
		/** ********************************************************** */
		int start = 0;
		int end = 0;
		String temp = new String();
		while (start < strURL.length())
		{
			end = strURL.indexOf(" ", start);
			if (end != -1)
			{
				temp = temp.concat(strURL.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strURL.length())
				{
					strURL = temp;
					break;
				}

			}
			else if (end == -1)
			{
				if (start == 0)
					break;
				if (start < strURL.length())
				{
					temp = temp.concat(strURL.substring(start, strURL.length()));
					strURL = temp;
					break;
				}
			}

		}

		temp = "";
		start = end = 0;

		while (start < strAugs.length())
		{
			end = strAugs.indexOf(" ", start);
			if (end != -1)
			{
				temp = temp.concat(strAugs.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strAugs.length())
				{
					strAugs = temp;
					break;
				}

			}
			else if (end == -1)
			{
				if (start == 0)
					break;
				if (start < strAugs.length())
				{
					temp = temp.concat(strAugs.substring(start, strAugs.length()));
					strAugs = temp;
					break;
				}
			}

		}

		/** **************************************************************** */
		return strAugs;
	}

}
