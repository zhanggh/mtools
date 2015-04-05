package com.mtools.thread.test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutilThreadTest {

	public static Developer dev;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		dev=new Developer();
		
		ExecutorService exc=Executors.newFixedThreadPool(10);
		
		MutilThreadTest mut=new MutilThreadTest();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		new Thread(mut.new ThreadA()).start();
		new Thread(mut.new ThreadB()).start();
		
	}
	

	
	class ThreadA implements Runnable{

		@Override
		public void run() {
			System.out.println("ThreadA name:"+dev.name);
			try {
				dev.name+="gaunghai"; 
				Thread.sleep(5000);
				System.out.println("ThreadA over");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	class ThreadB implements Runnable{
		
		@Override
		public void run() {
			System.out.println("ThreadB name:"+dev.name);
			dev.name+="meiqing"; 
			try {
				Thread.sleep(5000);
				System.out.println("ThreadB over");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
