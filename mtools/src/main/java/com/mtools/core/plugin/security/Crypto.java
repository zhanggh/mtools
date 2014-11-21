package com.mtools.core.plugin.security;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Crypto {
	public static String decode(String password) throws Exception {
		CEA cea = new CEA();
		BASE64Decoder b64 = new BASE64Decoder();
		byte[] decode = b64.decodeBuffer(password);
		byte[] res = new byte[decode.length];
		byte[] key = "12345678".getBytes();
		cea.Decrypt(decode, res, decode.length, key, key.length);
		return new String(res);
	}

	public static String encode(String vClearText) throws Exception {
		String encode = "";
		CEA cea = new CEA();
		byte[] key = "12345678".getBytes();
		int intStrLen = vClearText.length();
		if (intStrLen < 16) {
			for (int i = 0; i < 16 - intStrLen; i++)
				vClearText = String.valueOf(String.valueOf(vClearText)).concat(
						" ");
		}
		byte plain[] = vClearText.getBytes();
		byte cipher[] = new byte[plain.length];
		cea.Encrypt(plain, cipher, plain.length, key, key.length);
		BASE64Encoder b64Enc = new BASE64Encoder();
		encode = b64Enc.encode(cipher);
		return encode;
	}
	
	public static void main(String[] args){
		String str="UR+On9NZY0z6IKqQIGLWYA==";
		try{
			str=decode(str);
//			str=encode("222222");
		}catch(Exception e){
			
		}
		System.out.println(str);
	}
}
