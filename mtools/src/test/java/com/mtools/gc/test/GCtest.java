package com.mtools.gc.test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.mtools.core.plugin.entity.MenuInfo;

public class GCtest {
	ExecutorService threadPool = Executors.newFixedThreadPool(105); 
	
	Semaphore sem=new Semaphore(5);
	/**
	 * 尽管在不断的创建新的对象，但是不会导致内存溢出，原因：jvm会进行垃圾回收
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int count=0;
			
//			while (count<1999999) {
//				sem.acquire();
//			 new ArrayList<String>();
				GCtest gc=new GCtest();
				gc.runMutiThreads();
//				TransThread trd =gc.new  TransThread(gmenu);
//				threadPool.execute(trd);
				 
//			new Thread(trs).start();
//				sem.release();
				
//			}
			System.err.println("over");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runMutiThreads() throws InterruptedException{
		MenuInfo gmenu=new MenuInfo();
		gmenu.setMenuname("test");
		for(int a=0;a<1999999;a++){
//			if(sem.availablePermits() < 0){
//				System.err.println("not ava");
//			}
//			 
//			sem.acquire();
			TransThread trd =new  TransThread(null,a);
//			new Thread(trd).start(); 
			threadPool.execute(trd);
//			sem.release();
			
		}
		threadPool.shutdown();
		System.err.println("threadPool over");
	}
	
	

	class TransThread implements Runnable{
		 
			MenuInfo menu;
			
			int a;

			public TransThread(MenuInfo menu, int a){
				super();
				this.menu=menu;
				this.a=a;
			}
			
			@Override
			public void run() {
				try {
//				Thread.sleep(000);
					System.out.println("-"+this.a+"-"+Thread.currentThread().getName()+" is running...");
//					System.out.println(menu.getMenuname()+"-"+this.a+"-"+Thread.currentThread().getName()+" is running...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	}
}
