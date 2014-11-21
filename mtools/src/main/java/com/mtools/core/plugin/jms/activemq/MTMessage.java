package com.mtools.core.plugin.jms.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MTMessage implements MessageCreator{

	protected Session session;
	private String message;
	private Message msg;
	@Override
	 public Message createMessage(Session session) throws JMSException {  
    	msg=session.createTextMessage(message);
    	this.session=session;
        return msg;  
    }
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}
	
}
