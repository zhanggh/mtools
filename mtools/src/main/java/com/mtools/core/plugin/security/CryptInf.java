package com.mtools.core.plugin.security;

public interface CryptInf
{
	public boolean VerifyMsg(String TobeVerified, String PlainText, String CertFile) throws Exception;
	public boolean SignMsg(String TobeSigned, String KeyFile, String PassWord) throws Exception;
	public String getLastSignMsg();
}
