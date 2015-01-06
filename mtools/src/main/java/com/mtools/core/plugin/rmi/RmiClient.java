package com.mtools.core.plugin.rmi;

import java.rmi.Naming;

import com.mtools.core.plugin.entity.UserInfo;

public class RmiClient {

	public static void main(String[] args){  
        try{  
            //调用远程对象，注意RMI路径与接口必须与服务器配置一致  
        	RmiService service=(RmiService)Naming.lookup("rmi://127.0.0.1:6600/PersonService");  
        	UserInfo user=service.getUser();  
            System.out.println("username:"+user.getUsername());
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
    }  
}
