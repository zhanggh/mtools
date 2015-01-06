package com.mtools.cyclebarrier.test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class TestCyclicBarrier {

    private static final int THREAD_NUM = 5;

    public static class WorkerThread implements Runnable{

        CyclicBarrier barrier;
        String name;
        int count;
        public WorkerThread(CyclicBarrier b, String n, int i){
            this.barrier = b;
            this.name = n;
            this.count = i;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
            	if(this.count<=2){
            		System.out.println(this.name+" sleep...");
            		Thread.sleep(3000);
            	}
                System.out.println(this.name + " Worker's waiting");
                //线程在这里等待，直到所有线程都到达barrier。
                barrier.await(1, TimeUnit.SECONDS);
                System.out.println(this.name + " ID:"+Thread.currentThread().getId()+" Working");
            }catch(Exception e){
//                e.printStackTrace();
            }
        }

    }

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        CyclicBarrier cb = new CyclicBarrier(THREAD_NUM, new Runnable() {
            //当所有线程到达barrier时执行
            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println("\nInside Barrier\n");

            }
        });

        for(int i=0;i<THREAD_NUM;i++){
            new Thread(new WorkerThread(cb, "before"+i, i)).start();
        }
   
//        Thread.sleep(1000);
//        for(int i=0;i<THREAD_NUM;i++){
//            new Thread(new WorkerThread(cb, "after"+i, i)).start();
//        }
    }

}
