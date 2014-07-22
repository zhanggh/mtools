package com.mtools.core.plugin.groovy

class GroovyTest implements GroovyConvert{

	def name='zhang'
	public String printName(){
		println name;
	}
	
	public static void main(String[] args) {
		def gvy = new GroovyTest();
		gvy.printName();
	}
}
