package com.ztools.run;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.mtools.core.plugin.entity.MenuInfo;

public class SemaphoreTest {
	final ExecutorService threadPool = Executors.newFixedThreadPool(105); 
	final Semaphore sem=new Semaphore(5);
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		SemaphoreTest semtest=new SemaphoreTest();
		semtest.runMutiThreads();
	}

	
	public void runMutiThreads() throws InterruptedException{
		 
		for(int a=0;a<1000;a++){
			TransThread trd =new  TransThread(a);
			new Thread(trd).start(); 
//			threadPool.execute(trd);
		}
		System.err.println("threadPool over");
//		threadPool.shutdown();
		
	}
	

	class TransThread implements Runnable{
		 
			
			int a;

			public TransThread( int a){
				super();
				this.a=a;
			}
			
			@Override
			public void run() {
				try {
					sem.acquire();
					System.out.println("-"+this.a+"-"+Thread.currentThread().getName()+" is running...");
//					System.out.println(menu.getMenuname()+"-"+this.a+"-"+Thread.currentThread().getName()+" is running...");
					Thread.sleep(9999000);
					System.out.println("-"+this.a+"-"+Thread.currentThread().getName()+" is over...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sem.release();
			}
			
	}
	
}
