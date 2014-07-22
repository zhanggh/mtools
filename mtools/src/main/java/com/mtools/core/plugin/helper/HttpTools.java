package com.mtools.core.plugin.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

import com.mtools.core.plugin.security.CryptInf;
import com.mtools.core.plugin.security.CryptNoRestrict;

 

public class HttpTools
{
	private static final SSLHandler simpleVerifier=new SSLHandler();
	private static SSLSocketFactory sslFactory;
	private static class SSLHandler implements X509TrustManager,HostnameVerifier
	{	
		private SSLHandler()
		{
		}
		
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean verify(String arg0, SSLSession arg1)
		{
			return true;
		}
	}
	public static HostnameVerifier getVerifier()
	{
		return simpleVerifier;
	}
	public static synchronized SSLSocketFactory getSSLSF() throws Exception
	{
		if(sslFactory!=null) return sslFactory; 
		SSLContext sc = SSLContext.getInstance("SSLv3");
		sc.init(null, new TrustManager[]{simpleVerifier}, null);
		sslFactory = sc.getSocketFactory();
		return sslFactory;
	}	
	 
	private static URLConnection createRequest(String strUrl, String strMethod) throws Exception
	{
		String settleNo = String.valueOf(new Date().getTime());
		URL url = new URL(strUrl+"?parameter="+settleNo);
//		URL url = new URL(strUrl);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
//		conn.setUseCaches(false);
//		conn.s
		if (conn instanceof HttpsURLConnection)
		{
			HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
			httpsConn.setRequestMethod(strMethod);
			httpsConn.setSSLSocketFactory(HttpTools.getSSLSF());
			httpsConn.setHostnameVerifier(HttpTools.getVerifier());
		}
		else if (conn instanceof HttpURLConnection)
		{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod(strMethod);
		}
		return conn;
	}
	private static void close(InputStream c)
	{
		try
		{
			if(c!=null) c.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	private static void close(OutputStream c)
	{
		try
		{
			if(c!=null) c.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	public static String send(String url,String msg) throws Exception
	{
		OutputStream reqStream=null;
		InputStream resStream =null;
		URLConnection request = null;
		String respText=null;
		byte[] postData;
		try
		{
			postData = msg.getBytes();
			request = createRequest(url, "POST");
	
			request.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			request.setRequestProperty("Content-length", String.valueOf(postData.length));
			request.setRequestProperty("Keep-alive", "false");
			request.setRequestProperty("Connection", "close");
	
			reqStream = request.getOutputStream();
			reqStream.write(postData);
//			reqStream.flush();
//			if(reqStream!=null)
			reqStream.close();
 
			ByteArrayOutputStream ms = null;	
			resStream = request.getInputStream();
			ms = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int count;
			while ((count = resStream.read(buf, 0, buf.length)) > 0)
			{
				ms.write(buf, 0, count);
			}
			resStream.close();
			respText = new String(ms.toByteArray());
			
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			close(reqStream);
			close(resStream);
		}
		return respText;
	}
	public static String sendFtp(String url,String xml) throws Exception
	{
		OutputStream reqStream=null;
		InputStream resStream =null;
		URLConnection request = null;
		String respText=null;
		byte[] postData;
		try
		{
			postData = xml.getBytes("GBK");
			request = createRequest(url, "POST");
			
			request.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			request.setRequestProperty("Content-length", String.valueOf(postData.length));
			request.setRequestProperty("Keep-alive", "false");
			
			reqStream = request.getOutputStream();
			reqStream.write(postData);
			reqStream.close();
 
			ByteArrayOutputStream ms = null;	
			resStream = request.getInputStream();
			ms = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int count;
			while ((count = resStream.read(buf, 0, buf.length)) > 0)
			{
				ms.write(buf, 0, count);
			}
			resStream.close();
			respText = new String(ms.toByteArray(), "GBK");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			close(reqStream);
			close(resStream);
		}
		return respText;
	}
	public static boolean verifySign(String strXML, String cerFile,boolean isFront) throws Exception
	{
	 
		String signedMsg =null;
		String strMsg =null;
		// 签名
		CryptInf crypt;
		crypt=new CryptNoRestrict("GBK");
		//
		File file = new File(cerFile);
		if (!file.exists()) throw new Exception("文件"+cerFile+"不存在");
		System.out.println("返回报文：\n"+strXML);
		if(!isFront){
			int iStart = strXML.indexOf("<SIGNED_MSG>");
			if(iStart==-1) throw new Exception("XML报文中不存在<SIGNED_MSG>");
			int end = strXML.indexOf("</SIGNED_MSG>");
			if(end==-1) throw new Exception("XML报文中不存在</SIGNED_MSG>");	
			signedMsg = strXML.substring(iStart + 12, end);
			strMsg = strXML.substring(0, iStart) + strXML.substring(end + 13);
			return crypt.VerifyMsg(signedMsg.toLowerCase(), strMsg,cerFile);
		}else{
			return true;
		}
	}
	public static String signPlain(String strData, String pathPfx, String pass) throws Exception
	{
		CryptInf crypt;
		crypt=new CryptNoRestrict("GBK");
		String strRnt = "";
		if (crypt.SignMsg(strData, pathPfx, pass))
		{
			String signedMsg = crypt.getLastSignMsg();
			strRnt = signedMsg;
		}
		else
		{
			throw new Exception("签名失败");
		}
		return strRnt;		
	}//200604000000445
	public static String signMsg(String strData, String pathPfx, String pass) throws Exception
	{
		final String IDD_STR="<SIGNED_MSG></SIGNED_MSG>";
		String strMsg = strData.replaceAll(IDD_STR, "");
		String signedMsg = signPlain(strMsg, pathPfx, pass);
		String strRnt = strData.replaceAll(IDD_STR, "<SIGNED_MSG>" + signedMsg + "</SIGNED_MSG>");
		return strRnt;
	}
}
