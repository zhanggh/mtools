package com.mtools.core.plugin.constant;

public class CoreConstans {

	public static final String MTOOLS_VERSION = "1.0V";//版本号
	public static final String RANDOMCODEKEY = "randomkey";//随机数
	public static final String ONLINEUSERS = "onlineUsers";//在线用户
	/**
	 * 操作日志参数
	 */
	public static final String ERROR_MESSAGE = "error_msg";//错误信息
	public static final String LOGIN_COUNT = "login_count";//登陆人数
	public static final String LOGINGUSER = "loginguser";//登陆用户
	public static final String OPTRESULT = "opt_result";//操作结果 1成功，0失败
	public static final String ORGPARAMS = "org_params";//原参数
	public static final String SUCCESSMESSAGE = "successMessage";//成功信息
	
	//权限类型：
	public static final String TECSP_WG = "tecsp_wg";//技术支持系统 网关系统
	public static final String TECSP_TLT = "tecsp_tlt";//技术支持系统 账户支付系统
	
	/**
	 * 请求链接
	 */
	public static final String REQUEST_URL = "responseURL";//返回连接
	public static final String REQESTURI = "reqesturi";//原请求uri
	
	public static final String PATH_REG = "[a-z|A-Z|0-9|\\/]*";//权限正则

	/**
	 * 异常码
	 */
	public static final String EXCEPTON_01 = "01";//异常类型 其他错误
	public static final String EXCEPTON_02 = "02";//异常类型 session超时或者尚未登陆
	public static final String EXCEPTON_03 = "03";//异常类型 权限不足
	
	
	 /**
     * 操作名称
     */
    public static final String OP_NAME = "op";


    /**
     * 消息key
     */
    public static final String MESSAGE = "message";

    /**
     * 错误key
     */
    public static final String ERROR = "error";

    /**
     * 上个页面地址
     */
    public static final String BACK_URL = "BackURL";

    public static final String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    public static final String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    public static final  String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";

    public static final String CONTEXT_PATH = "ctx";

    /**
     * 当前登录的用户
     */
    public static final String CURRENT_USER = "user";
    public static final String CURRENT_USERNAME = "username";

    public static final String ENCODING = "UTF-8";
}
