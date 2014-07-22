package com.mtools.core.plugin.db;
import java.util.Map;

import com.mtools.core.plugin.helper.Auxs;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@SuppressWarnings("rawtypes")
public class KVMFilterWriter implements ExtendedHierarchicalStreamWriter 
{
	public KVMFilterWriter(HierarchicalStreamWriter underhsw,Map<String,String> fixedNV)
	{
		underlyingWriter=underhsw;
		fixedNameValue=fixedNV;
	}
	 
	public void startNode(String name, Class clazz)
	{
		if(Auxs.isSimpleClz(clazz)) lastNode=name; else lastNode="#@";
		if(underlyingWriter instanceof ExtendedHierarchicalStreamWriter)
			((ExtendedHierarchicalStreamWriter) underlyingWriter).startNode(name, clazz);
		else
			underlyingWriter.startNode(name);
	}
	 
	public void startNode(String name)
	{
		lastNode=name;
		underlyingWriter.startNode(name);
	}

	 
	public void addAttribute(String name, String value)
	{
		String fn=null;
		if(fixedNameValue!=null) fn=fixedNameValue.get(name);
		if(Auxs.empty(fn)) underlyingWriter.addAttribute(name, value);
		else underlyingWriter.addAttribute(name, fn);
	}

	 
	public void setValue(String text)
	{
		String fn=null;
		if(fixedNameValue!=null) fn=fixedNameValue.get(lastNode);
		if(Auxs.empty(fn)) underlyingWriter.setValue(text);
		else underlyingWriter.setValue(fn);
	}

	 
	public void endNode()
	{
		underlyingWriter.endNode();
	}
	
	public void flush()
	{
		underlyingWriter.flush();
	}
	
	public void close()
	{
		underlyingWriter.close();
	}
	
	public HierarchicalStreamWriter underlyingWriter()
	{
		return underlyingWriter;
	}
	private Map<String,String> fixedNameValue;
	private String lastNode;
	private HierarchicalStreamWriter underlyingWriter;
}
