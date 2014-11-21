package com.mtools.core.plugin.jms.listener;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.mtools.core.plugin.jms.activemq.JmsSender;

public class ConsumerMessageListener implements SessionAwareMessageListener<TextMessage> {

	@Resource(name="jmsSender")
	JmsSender jmsSender;
	Log log=LogFactory.getLog(this.getClass());
	@Override
	public void onMessage(TextMessage message, Session session)  {
		log.debug("监听消息");
		TextMessage text=message;
		try {
			log.debug(text.getText());
			Destination replyTo = text.getJMSReplyTo();
//			jmsTools.sendMessageExt(replyTo, "我已经收到了");
			//返回
			Thread.sleep(2000);
			MessageProducer producer = session.createProducer(replyTo);  
	        Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");  
	        producer.send(textMessage); 
//	        throw new JMSException("Error"); 
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
