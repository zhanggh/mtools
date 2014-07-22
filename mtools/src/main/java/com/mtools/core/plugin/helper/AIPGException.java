package com.mtools.core.plugin.helper;


public class AIPGException extends Exception {
    private static final long serialVersionUID = 1L;

	private String messageKey = null;

	private String errorNum;

	public AIPGException() {
		super();
	}

	public AIPGException(String msg) {
		super(msg);
	}

	public AIPGException(int errorNum, String msg) {
		super(msg);
		this.errorNum = String.valueOf(errorNum);
	}
	
	public AIPGException(String msg,Exception ex){
		super(msg,ex);
	}
	
	public AIPGException(String errorNum, String msg) {
		super(msg);
		this.errorNum = errorNum;
	}

	public String getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = String.valueOf(errorNum);
	}

	public AIPGException(Throwable rootCause)
	{
		super(rootCause);
	}

	public void setMessageKey(String key) {
		this.messageKey = key;
	}

	public String getMessageKey() {
		return messageKey;
	}
	public void setRootCause(Throwable anException) {
		super.initCause(anException);
	}

	public Throwable getRootCause()
	{
		return super.getCause();
	}
	
	public static void throwExcp(String code,String message) throws AIPGException{
		throw new AIPGException(code,message);
	}
}
