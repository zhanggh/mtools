/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-6-10
 * version 1.0
 * 说明：
 */
package com.mtools.core.plugin.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.notify.SystemRunningNotify;
 
/**
 *  功能：定时任务
 * @date 2014-6-10
 */
@Component("comTask")
public class ComTaskPlugin extends BasePlugin{

	@Resource(name="sysRunningNotify")
	SystemRunningNotify notify;
	@Resource(name="taskExecutor")
	TaskExecutor taskExecutor;
//	@Scheduled(cron="0 0 9 * * ?") 
//	@Scheduled(fixedDelay=16000)
	public void singing(){  
        Date date=new Date();  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        log.info("定时任务："+sdf.format(date));  
    }  
//	@Scheduled(cron="0 0 1 * * ?") 
//	@Scheduled(fixedDelay=16000)
	public void doSomething(){  
		log.info("定时任务：正在执行某些业务");  
	}  
	 
//	@Scheduled(cron="0 0 0 * * ?") 
	public void perDayMonitor(){
		log.info("系统每日正常运行通知!");
		
		notify.setSubject(SpringUtil.getCxt().getApplicationName()+"系统每日正常运行通知!");
		notify.setContext(SpringUtil.getCxt().getApplicationName()+"系统每日正常运行通知!");
		taskExecutor.execute(notify);
	}
}
