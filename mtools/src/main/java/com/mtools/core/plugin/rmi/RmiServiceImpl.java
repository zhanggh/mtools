package com.mtools.core.plugin.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.mtools.core.plugin.entity.UserInfo;

public class RmiServiceImpl extends UnicastRemoteObject implements RmiService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7594124400252756498L;

	protected RmiServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public UserInfo getUser() throws RemoteException{
		UserInfo user=new UserInfo();
		user.setUsername("张广海");
		user.setUserid("zhanggh");
		return user;
	}

}
