/**
 * @author 张广海
 * @date 2014-7-19
 */
package com.mtools.core.plugin.jms.activemq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.Auxs;

/**
 *  功能：
 */
@Component("jmsSender")
public class JmsSender {
	@Resource(name="jmsTemplate")
	private JmsTemplate jmsTemplate; 
	@Resource(name="singleConFatry")
	private ConnectionFactory connectionFactory; 
	@Resource(name="adapterQueue")
	private Destination defaultDest;
	
	Log log=LogFactory.getLog(this.getClass());
	
	public JmsTemplate getJmsTemplate() {  
        return jmsTemplate;  
    }   
  
      
    public void setJmsTemplate(JmsTemplate jmsTemplate) {  
        this.jmsTemplate = jmsTemplate;  
    }  
    
    /**
     * 异步消息
     * @param destination
     * @param mtmsg
     * @throws JMSException
     */
    public void asynsendMessage(Destination destination,MTMessage mtmsg) throws JMSException {  
    	if(mtmsg==null){
			mtmsg=new MTMessage();
			mtmsg.setMessage("空消息");
		}
        jmsTemplate.send(destination,mtmsg);  
    }   
    
    
    
    /**
     * 同步消息
     * @param mtmsg
     * @return
     * @throws JMSException
     */
    public List<Message> synSendMessage(Destination destination,MTMessage mtmsg) throws JMSException{
    	Session session =null;
    	List<Message> lmsg=null;
    	MessageProducer producer=null;
    	Connection conn=null;
    	try {
			conn=connectionFactory.createConnection();
			conn.start();
			session = conn.createSession(true,  Session.SESSION_TRANSACTED);
			TemporaryQueue replyTo=session.createTemporaryQueue();
			producer = session.createProducer(destination);
			if(mtmsg==null){
				mtmsg=new MTMessage();
				mtmsg.setMessage("空消息");
			}
			mtmsg.createMessage(session);
			mtmsg.getMsg().setJMSReplyTo(replyTo);
			producer.send(mtmsg.getMsg());
			commit(session);
			MessageConsumer msgconsumer=session.createConsumer(replyTo);
			lmsg=recv(msgconsumer, 3000, 1);
		} catch (Exception e) {
			session.rollback();
			log.error("发送消息发生异常，进行回滚", e);
			e.printStackTrace();
		}finally{
			producer.close();
			conn.stop();
		}
		return lmsg;
    }
	private void commit(Session session) throws JMSException {
		if(session.getTransacted()) session.commit();
	}

	/**
	 * 接收消息
	 * @param msgconsumer
	 * @param timeout 毫秒
	 * @param n
	 * @return
	 * @throws JMSException
	 */
	public static List<Message> recv(MessageConsumer msgconsumer,long timeout,int n) throws JMSException
	{
		int i;
		List<Message> lsmsg=new ArrayList<Message>();
		Message msgrsp=null;
		try
		{
			for(i=0;i<n;++i)
			{
				msgrsp=msgconsumer.receive(timeout);
				if(msgrsp!=null) lsmsg.add(msgrsp);
			}
		}
		catch(Throwable t)
		{
			Auxs.lg.error("receive message error",t);
		}
		return lsmsg;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
