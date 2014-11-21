package com.ztools.run;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableAndFuture {  
    public static void main(String[] args) {  
    	System.err.println("start");
        ExecutorService threadPool = Executors.newFixedThreadPool(5);  
        Future<Integer> future = threadPool.submit(new Callable<Integer>() {  
            public Integer call() throws Exception { 
            	System.err.println("*******future*******");
            	 Thread.sleep(3000);// 可能做一些事情  
                return new Random().nextInt(100);  
            }  
        });  
        Future<Integer> future2 = threadPool.submit(new Callable<Integer>() {  
        	public Integer call() throws Exception {  
        		System.err.println("*******future2*******");
        		Thread.sleep(3000);// 可能做一些事情  
        		return new Random().nextInt(100);  
        	}  
        });  
        Future<Integer> future3 = threadPool.submit(new Callable<Integer>() {  
        	public Integer call() throws Exception { 
        		System.err.println("*******future3*******");
        		Thread.sleep(3000);// 可能做一些事情  
        		return new Random().nextInt(100);  
        	}  
        });  
        System.err.println("doing");
        try {  
//            Thread.sleep(2000);// 可能做一些事情  
        	
        	System.out.println(future.get());  
        	System.err.println("future over");
        	System.out.println(future2.get()); 
        	System.err.println("future2 over");
        	System.out.println(future3.get());  
        	System.err.println("future3 over");
            
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (ExecutionException e) {  
            e.printStackTrace();  
        } 
        threadPool.shutdown();
        System.err.println("main over");
    }  
}  