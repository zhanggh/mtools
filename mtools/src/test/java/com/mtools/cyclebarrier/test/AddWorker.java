package com.mtools.cyclebarrier.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AddWorker implements Runnable{
	ArrayBlockingQueue<Integer> q;
	CyclicBarrier bar;
	int [] a;
	int s, e;
	
	public AddWorker(ArrayBlockingQueue<Integer> q, CyclicBarrier b, int [] a, int s, int e){
		this.q = q;
		this.bar = b;
		this.a = a;
		this.s = s;
		this.e = e;
	}
	
	@Override
	public void run() {
		int sum = 0;
		for(int i=s; i<=e; i++){
			sum += a[i];
		}
		System.out.println("AddWorker sum = "+sum);
		q.add(sum);
		try {
			bar.await();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		final ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(3);
		int [] a = {1,2,3,4,5,6,7,8,9,20};
		CyclicBarrier bar = new CyclicBarrier(2, new Runnable(){
			public void run(){
				int sum = 0;
				while(!q.isEmpty()){
					int i = (int) q.remove();
					System.out.println("get element = "+i);
					sum += i;
				}
				System.out.println("final result = "+sum);
			}
		});
		for(int i=0; i<2; i++){
			new Thread(new AddWorker(q, bar, a, i*5, (i+1)*5-1)).start();
		}
		
	}
}