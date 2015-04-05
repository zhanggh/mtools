package com.mtools.thread.test;


public class DamaenTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		 Runnable run = new  Runnable() {
			public void run() {
				try {
					System.out.println("进入子线程");
//					Thread.sleep(5000);
					this.wait(3000);
					System.out.println("完成子线程");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
		Thread th=new Thread(run);
		th.start();
//		th.wait(3000);
		System.out.println("主线程完成");
	}

}
