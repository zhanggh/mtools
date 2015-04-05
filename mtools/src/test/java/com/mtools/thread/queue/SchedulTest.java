package com.mtools.thread.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulTest {

	static SchedulTest sch;
	static ReentrantLock lock;
	/**
	 * @param args
	 */
	static int index=0;
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
//		ScheduledExecutorService sch = Executors.newSingleThreadScheduledExecutor();
//		
//		sch.scheduleAtFixedRate(new Runnable(){
//
//			@Override
//			public void run() {
//				index++;
//				System.out.println(index);
//				
//			}
//			
//		}, 1, 500, TimeUnit.MILLISECONDS);
		sch=new SchedulTest();
		lock= new ReentrantLock();
		
		Runnable rab=new Runnable() {
			
			@Override
			public void run() {
				try {
					sch.getResources();
//					sch.getResourcesExt();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		};
		
		Thread tha= new Thread(rab);
		tha.start();
		tha.wait();
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					sch.getResources();
////					sch.getResourcesExt();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//				
//			}
//		}).start();
		
	}

	
	public void getResources() throws InterruptedException{
		
		boolean ret =lock.tryLock();
		if(ret){
			lock.lock(); 
			System.out.println("**********************获得资源**********************");
			Thread.sleep(6000);
			lock.unlock();
		}else{
			System.out.println("**********************无法获得资源**********************");
		}
	}
	public synchronized void getResourcesExt() throws InterruptedException{
		
		 System.out.println("**********************获得资源a**********************");
		 Thread.sleep(6000);
//		 System.out.println("**********************无法获得资源a**********************");
		 
	}
	
	
	public class ThreadExt extends Thread{

		@Override
		public void run() {
			super.run();
		}
		
	}
}
