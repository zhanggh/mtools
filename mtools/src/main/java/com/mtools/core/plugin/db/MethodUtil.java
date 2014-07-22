package com.mtools.core.plugin.db;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class MethodUtil
{
	public Method findMethod(Method[] mthds,Class pt2clz,String prefix)
	{
		for(Method m:mthds)
		{
			if(m.getName().startsWith(prefix)) continue;
			Class[] pts=m.getParameterTypes();
			if(pts.length!=2) continue;
			for(Class pt:pts)
			{
				if(pt.isAssignableFrom(pt2clz))
				{
					return m;
				}
			}
		}
		return null;
	}
	public static Method findMethodByRetType(Method[] rsMtdA,Class prm,String prefix)
	{
		int iM;
		Method rsMtd;
		Class rsPM,rsPMA[];
		for(iM=0;iM<rsMtdA.length;++iM)
		{
			rsMtd=rsMtdA[iM];
			if(!rsMtd.getName().startsWith(prefix)) continue;
			rsPMA=rsMtd.getParameterTypes();
			if(rsPMA.length!=1) continue;
			rsPM=rsPMA[0];
			if(rsPM.isAssignableFrom(String.class)&&prm.isAssignableFrom(rsMtd.getReturnType())) return rsMtd;
		}
		return null;
	}

}
