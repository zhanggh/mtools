/*通联支付网络服务股份有限公司版权所有
 *未经授权严禁查看传抄
 *
 *作者:李国辉
 *日期:2010-7-29 上午10:32:30
 */
package com.mtools.core.plugin.mail;

import java.util.List;

public interface MailInf {

	/**
	 * 发送邮件
	 * @param sb
	 * @param filenames
	 * @return
	 */
	public boolean Send(StringBuffer sb, List filenames);

	/**
	 * 接收邮件
	 * @return 邮件保存路径
	 */
	public List<String> Receive();

	/**
	 * 检查邮件发送明细参数
	 * @return
	 */
	public boolean CheckSendDetailConfig();

	/**
	 * 检查邮件接受明细参数
	 * @return
	 */
	public boolean CheckReceiveDetailConfig();

	public MailDetailConfig getMailparam();

	public void setMailparam(MailDetailConfig mailparam);

}