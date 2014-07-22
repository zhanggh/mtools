/**
 * 
 */
package com.mtools.core.plugin.notify;

import java.io.IOException;

import javax.servlet.ServletRequest;

/**
 * @author zhang
 *  通知接口
 */
public interface AsyncNotify extends Runnable {

	public void initData(ServletRequest request,Throwable e,String appName) throws IOException;
}
