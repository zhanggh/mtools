package com.mtools.core.plugin.db;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;



public class BeanTools
{
	public static boolean hasBit(int flag,int bit)
	{
		return (flag&bit)==bit;
	}
	public static Object readProp(Object o,Method m)
	{
		try
		{
			return m.invoke(o);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	public static PropertyDescriptor findPD(PropertyDescriptor[]pda,String name)
	{
		int i;
		for(i=0;i<pda.length;++i)
		{
			PropertyDescriptor pd=pda[i];
			if(pd.getName().equals(name)) return pd;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static PropertyDescriptor[] getPDs(Class clz)
	{
		BeanInfo bi=null;
		try
		{
			bi=Introspector.getBeanInfo(clz);
		}
		catch(Exception e)
		{
			return new PropertyDescriptor[0];
		}
		PropertyDescriptor[] descriptors;
		descriptors=bi.getPropertyDescriptors();
		if(descriptors==null) return new PropertyDescriptor[0];
		return descriptors;
	}
	@SuppressWarnings("unchecked")
	public static boolean isDigit(Class clz)
	{
		if(clz.isAssignableFrom(Character.class)) return true;
		else if(clz.isAssignableFrom(Byte.class)) return true;
		else if(clz.isAssignableFrom(Short.class)) return true;
		else if(clz.isAssignableFrom(Integer.class)) return true;
		else if(clz.isAssignableFrom(Long.class)) return true;
		else if(clz.isAssignableFrom(Float.class)) return true;
		else if(clz.isAssignableFrom(Double.class)) return true;
		return false;		
	}
	@SuppressWarnings("unchecked")
	public static Object toObj(Class clz,long val)
	{
		if(clz.isAssignableFrom(Character.class)) return new Character((char)val);
		else if(clz.isAssignableFrom(Byte.class)) return new Byte((byte)val);
		else if(clz.isAssignableFrom(Short.class)) return new Short((short)val);
		else if(clz.isAssignableFrom(Integer.class)) return new Integer((int)val);
		else if(clz.isAssignableFrom(Long.class)) return new Long(val);
		else if(clz.isAssignableFrom(Float.class)) return new Float(val);
		else if(clz.isAssignableFrom(Double.class)) return new Double(val);
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Object getMin(Class clz)
	{
		if(clz.isAssignableFrom(Character.class)) return new Character(Character.MIN_VALUE);
		else if(clz.isAssignableFrom(Byte.class)) return new Byte(Byte.MIN_VALUE);
		else if(clz.isAssignableFrom(Short.class)) return new Short(Short.MIN_VALUE);
		else if(clz.isAssignableFrom(Integer.class)) return new Integer(Integer.MIN_VALUE);
		else if(clz.isAssignableFrom(Long.class)) return new Long(Long.MIN_VALUE);
		else if(clz.isAssignableFrom(Float.class)) return new Float(Float.MIN_VALUE);
		else if(clz.isAssignableFrom(Double.class)) return new Double(Double.MIN_VALUE);
		//else if(clz.isAssignableFrom(Boolean.class)) return Boolean.
		return null;
	}
	public static void iteratPorp(Object bean,BeanPropHandler propHandler)
	{
		PropertyDescriptor pda[]=BeanTools.getPDs(bean.getClass());
		for(PropertyDescriptor pd:pda)
		{
			if(pd.getName().equals("class")) continue;
			Object val=BeanTools.readProp(bean,pd.getReadMethod());
			propHandler.handle(bean, pd.getName(), val);
		}
	}
	public static boolean ignore(Object o)
	{
		if(o==null) return true;
		if(o.equals(getMin(o.getClass()))) return true;
		return false;
	}
	@SuppressWarnings("unchecked")
	public void initObj(Object o)
	{
		int i;
		PropertyDescriptor[] pds=BeanTools.getPDs(o.getClass());
		PropertyDescriptor pd;
		for(i=0;i<pds.length;++i)
		{
			pd=pds[i];
			Method wm=pd.getWriteMethod();
			if(wm==null) continue;
			try
			{
				Class clz=pd.getPropertyType();
				Object val[]=new Object[1];
				if(clz.isAssignableFrom(Boolean.class)) val[0]=new Boolean(false);
				else val[0]=getMin(clz);
				wm.invoke(o, val);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}
}
