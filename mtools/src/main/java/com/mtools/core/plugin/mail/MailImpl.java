/* 
 * @author 张广海
 */
package com.mtools.core.plugin.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtools.core.plugin.helper.FuncUtil;
import com.sun.mail.pop3.POP3Folder;

/**
 * 邮件发送实现类
 * @author 张广海
 *
 */
public class MailImpl implements MailInf{
	static Log log = LogFactory.getLog(MailImpl.class);
	
	private MailDetailConfig mailparam = null;
	
	 
	/**
	 * 发送邮件
	 * @param sb
	 * @param filenames
	 * @return
	 */
	public boolean Send(StringBuffer sb,List filenames){
		boolean result = false;
		try{
			if(!CheckSendDetailConfig()){
				log.error("检查邮件明细参数错误");
				return result;
			}
			
			log.info(mailparam.isUsessl());
			mailparam.setContent(sb.toString());
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.host", mailparam.getSmtphost());
			props.setProperty("mail.smtp.port", mailparam.getSmtpport());
			props.setProperty("mail.smtp.auth", "true");
			if (mailparam.isUsessl())
			{
				log.info("use ssl:true");
				props.setProperty("mail.smtp.starttls.enable", "true");
			}
			Session session = Session.getInstance(props);
			session.setDebug(mailparam.isDebug());
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailparam.getFrom()));
			message.setSentDate(new Date());
			if (mailparam.getTo() != null && mailparam.getTo().size() > 0)
			{
				InternetAddress toAdds[] = new InternetAddress[mailparam.getTo().size()];
				Iterator it_to = mailparam.getTo().iterator();
				int to_index = 0;
				while (it_to.hasNext()) 
					toAdds[to_index++] = new InternetAddress((String)it_to.next());
				message.addRecipients(Message.RecipientType.TO, toAdds);
			}
			if (mailparam.getCc() != null && mailparam.getCc().size() > 0)
			{
				InternetAddress ccAdds[] = new InternetAddress[mailparam.getCc().size()];
				Iterator it_cc = mailparam.getCc().iterator();
				int cc_index = 0;
				while (it_cc.hasNext()) 
					ccAdds[cc_index++] = new InternetAddress((String)it_cc.next());
				message.addRecipients(Message.RecipientType.CC, ccAdds);
			}
			if (mailparam.getBcc() != null && mailparam.getBcc().size() > 0)
			{
				InternetAddress bccAdds[] = new InternetAddress[mailparam.getBcc().size()];
				Iterator it_bcc = mailparam.getBcc().iterator();
				int bcc_index = 0;
				while (it_bcc.hasNext()) 
					bccAdds[bcc_index++] = new InternetAddress((String)it_bcc.next());
				message.addRecipients(Message.RecipientType.CC, bccAdds);
			}
			message.setSubject(mailparam.getSubject());
			Multipart multipart = new MimeMultipart();
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(mailparam.getContent() != null? mailparam.getContent() : "","text/html;charset=GB2312");
			multipart.addBodyPart(contentPart);
			if (filenames!= null && filenames.size() > 0)
			{
				for (Iterator it_file = filenames.iterator(); it_file.hasNext();)
				{
					File file = new File((String)it_file.next());
					System.out.println(file.getAbsolutePath());
					if (file.exists())
					{
						BodyPart attachmentPart = new MimeBodyPart();
						javax.activation.DataSource ds = new FileDataSource(file);
						attachmentPart.setDataHandler(new DataHandler(ds));
						attachmentPart.setFileName(MimeUtility.encodeWord(file.getName()));
						multipart.addBodyPart(attachmentPart);
					}
				}

			}
			message.setContent(multipart);
			message.saveChanges();
			log.info("开始发送邮件");
			Transport transport = session.getTransport("smtp");
			transport.connect(mailparam.getSmtphost(), mailparam.getUsername(), mailparam.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			log.info("发送成功");
			result = true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			log.error("发送邮件异常",ex);
			result = false;
		}
		return result;
	}
	
	/**
	 * 接收邮件
	 * @return 邮件保存路径
	 */
	public List<String> Receive(){
		List<String> save_path = null;
		try{
			if(!CheckReceiveDetailConfig()){
				log.error("检查邮件明细参数错误");
				return null;
			}
			Properties props = new Properties();
			props.setProperty("mail.pop3.host", mailparam.getPop3host());
			props.setProperty("mail.pop3.port", mailparam.getPop3port());
			props.put("mail.pop3.socketFactory.port", mailparam.getPop3port());
			props.put("mail.pop3.socketFactory.fallback", "false");

			if (mailparam.isUsessl())
			{
				props.setProperty("mail.pop3.starttls.enable", "true");
				props.put("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			}

			Session session = Session.getDefaultInstance(props);
			session.setDebug(mailparam.isDebug());
			Store store = session.getStore("pop3");
			store.connect(mailparam.getPop3host(),mailparam.getUsername(), mailparam.getPassword());

			POP3Folder inbox = (POP3Folder) store.getDefaultFolder().getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Message[] msgs = inbox.getMessages();

			FetchProfile profile = new FetchProfile();
			profile.add(FetchProfile.Item.ENVELOPE);
			inbox.fetch(msgs, profile);
			
			log.info("新邮件数量:"+msgs.length);
			save_path = new ArrayList();
			for (int i = 0; i < msgs.length; i++){
				String disposition;
				BodyPart part;
				// 获取信息对象
				Part messagePart = msgs[i];
				try
				{
					Object content = messagePart.getContent();
					String subject = FuncUtil.SpecStrFilter(msgs[i].getSubject());
					log.info("接收邮件:"+subject);
					
					String savedir = mailparam.getSavedir()+"/"+subject+"/";
					FuncUtil.mkDir(savedir);
					//附件
					if (content instanceof Multipart){
						Multipart mp = (Multipart) content;
						int mpCount = mp.getCount();
						for (int m = 0; m < mpCount; m++){
							part = mp.getBodyPart(m);
							disposition = part.getDisposition();
							if(part.getContent()!=null&&part.getContent().toString().length()>0){
								File file_content = new File(savedir+"content.html");
								FileOutputStream fos2 = new FileOutputStream(file_content);
								System.out.println(part.getContent().toString());
								fos2.write(part.getContent().toString().getBytes());
								fos2.close();
							}							
							//判断是否有附件
							if (disposition != null && disposition.equals(Part.ATTACHMENT)){
								File file = new File(savedir+MimeUtility.decodeText(part.getFileName()));
									FileOutputStream fos = new FileOutputStream(file);
									InputStreamReader reader=new InputStreamReader(part.getInputStream());
									int num;
									while((num = reader.read())!=-1){
										fos.write(num);
									}
									fos.close();
							}

						}
					}
					save_path.add(savedir);
				}
				catch(Exception e)
				{
					log.debug("Deal EMail Error", e);
				}
			}
			//inbox.close(false);
			log.info("接收邮件结束");
		}
		catch(Exception ex){
			ex.printStackTrace();
			log.error("接收邮件异常",ex);
			return null;
		}
		return save_path;
	}	
	
	/**
	 * 检查邮件发送明细参数
	 * @return
	 */
	public boolean CheckSendDetailConfig(){
		if(mailparam == null){
			log.error("MAIL配置参数未初始化");
			return false;
		}
		if(mailparam.getSmtphost()==null){
			log.error("MAIL服务器地址设置错误");
			return false;
		}
		if(mailparam.getSmtpport()==null){
			log.error("MAIL服务器端口设置错误");
			return false;
		}
		if(mailparam.getFrom()==null){
			log.error("MAIL参数中的发件人地址不能为空");
			return false;
		}
		if(mailparam.getUsername()==null){
			log.error("MAIL参数中的用户名不能为空");
			return false;
		}
		if(mailparam.getPassword()==null){
			log.error("MAIL参数中的密码不能为空");
			return false;
		}
		if(mailparam.getTo()==null||mailparam.getTo().size()==0){
			log.error("至少需要一个收件人地址");
			return false;
		}
		if(mailparam.getSubject()==null||mailparam.getSubject().trim().length()==0){
			log.error("邮件主题不能为空");
			return false;
		}
		return true;
	}	
	
	/**
	 * 检查邮件接受明细参数
	 * @return
	 */
	public boolean CheckReceiveDetailConfig(){
		if(mailparam == null){
			log.error("MAIL配置参数未初始化");
			return false;
		}
		if(mailparam.getPop3host()==null){
			log.error("MAIL服务器地址设置错误");
			return false;
		}
		if(mailparam.getPop3port()==null){
			log.error("MAIL服务器端口设置错误");
			return false;
		}
		if(mailparam.getFrom()==null){
			log.error("MAIL参数中的发件人地址不能为空");
			return false;
		}
		if(mailparam.getUsername()==null){
			log.error("MAIL参数中的用户名不能为空");
			return false;
		}
		if(mailparam.getPassword()==null){
			log.error("MAIL参数中的密码不能为空");
			return false;
		}
		if(mailparam.getSavedir()==null||mailparam.getSavedir().trim().length()==0){
			log.error("MAIL参数中保存路径不能为空");
			return false;
		}
		return true;
	}

	public MailDetailConfig getMailparam() {
		return mailparam;
	}

	public void setMailparam(MailDetailConfig mailparam) {
		this.mailparam = mailparam;
	}		
}
