package com.mtools.core.plugin.rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
 
 
public class RmiServer{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
        	System.setSecurityManager(new RMISecurityManager());
        	RmiService service=new RmiServiceImpl();
			//注册通讯端口
			LocateRegistry.createRegistry(6600);
			//注册通讯路径
			Naming.rebind("rmi://127.0.0.1:6600/PersonService", service);
			System.out.println("Service Start!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}