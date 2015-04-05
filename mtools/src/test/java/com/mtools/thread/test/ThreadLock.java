package com.mtools.thread.test;

public class ThreadLock {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		long id=Thread.currentThread().getId();

		System.out.println("main id:"+id);
		
		Thread tha= null;
		tha= new Thread(rabe);
		tha.start();
	    tha= new Thread(rabe);
		tha.start();
//		tha.wait();
		Thread.sleep(2000);
		 synchronized (rabe) {
			 rabe.notifyAll();
		 }
		
	}

	
	static Runnable rab=new Runnable() {
		
		@Override
		public void run() {
			 System.out.println("************rab start**********");
			 synchronized (this) {
				 try {
					long id=Thread.currentThread().getId();
				    System.out.println("rab id:"+id);
					this.wait();
					System.out.println("************rab end**********");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			
		}
	};
	static Runnable rabe=new Runnable() {
		
		@Override
		public void run() {
			System.out.println("************rabe start**********");
			synchronized (this) {
				try {
					long id=Thread.currentThread().getId();
					this.wait();
					System.out.println("rabe id:"+id);
					System.out.println("************rabe end**********");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
			
		}
	};
}
