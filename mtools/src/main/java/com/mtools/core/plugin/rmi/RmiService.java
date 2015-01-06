package com.mtools.core.plugin.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.mtools.core.plugin.entity.UserInfo;

public interface  RmiService extends Remote {  

	public UserInfo getUser() throws RemoteException ;
}
