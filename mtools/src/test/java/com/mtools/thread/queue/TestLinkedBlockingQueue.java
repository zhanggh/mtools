package com.mtools.thread.queue;

import java.util.concurrent.LinkedBlockingQueue;

public class TestLinkedBlockingQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>();
		String name=Thread.currentThread().getThreadGroup().getName();
		String name2=Thread.currentThread().getName();
		boolean isDaemon=Thread.currentThread().isDaemon();
		System.out.println("thread group name:"+name);
		System.out.println("thread name:"+name2);
		System.out.println("thread isDaemon:"+isDaemon);
		for(int a=0;a<9999;a++){
			queue.add("zhang"+a);
			 
		}
		 
		System.out.println("over");
	}

}
