package com.mtools.log.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MyLogTest {

	static Log log2=null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		log= new LogExt("logtest");
		log2= LogFactory.getLog("logtest");
		String org="4525254<CVV2>123</CVV2>EYE<cvv2>sdf</cvv2>RYfsdjf<acctno>4984511213669896</acctno>lksdfwerw<validdate>0719</validdate>ersE&key=jljfsdfkjfwerwer ";
		log2.info(org);
		
		System.err.println("完毕");
	}

}
