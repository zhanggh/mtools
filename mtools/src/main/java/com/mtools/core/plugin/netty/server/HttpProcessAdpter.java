package com.mtools.core.plugin.netty.server;

public interface HttpProcessAdpter {

	
	/**
	 * 交易处理器
	 * @param reqMsg
	 * @return 返回报文
	 * 编码：GBK
	 * 处理器必须有且只有一个 而且对象名必须为 processAdpter
	 */
	public String reqAdapter(String reqMsg);
	
}
