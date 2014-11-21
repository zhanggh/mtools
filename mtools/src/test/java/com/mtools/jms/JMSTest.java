package com.mtools.jms;

 
	 
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mtools.core.plugin.jms.activemq.JmsSender;
import com.mtools.core.plugin.jms.activemq.MTMessage;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ztools.testbeans.xml"})
public class JMSTest {
 
	@Resource(name="jmsSender")
	JmsSender jmsSender;
     
    @Autowired
    @Qualifier("adapterQueue")
    private Destination destination;
    
    @Test
    public void testSend() throws JMSException {
        for (int i=0; i<1; i++) {
        	MTMessage mtmsg =new MTMessage();
        	mtmsg.setMessage("这个是生产者发来的消息");
        	List<Message> lmsg = jmsSender.synSendMessage(destination,mtmsg);
        	if(lmsg!=null&&lmsg.size()>0){
        		TextMessage text=(TextMessage) lmsg.get(0);
            	System.err.println("消费者返回的消息：");
            	System.err.println(text.getText());
        	}else{
        		System.err.println("无返回：");
        	}
        	
        }
    }
    
}
