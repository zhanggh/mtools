package com.mtools.core.plugin.helper;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import com.mtools.core.plugin.db.KVMFilterWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SuppressWarnings("rawtypes")
public class XStreamIg extends XStream
{
	public void aliasEx(Object...oa)
	{
		for(int i=0;i<oa.length/2;++i)
		{
			alias((String)oa[i*2],(Class)oa[i*2+1]);
		}
	}
	public XStreamIg()
	{
	}
    public XStreamIg(HierarchicalStreamDriver hierarchicalStreamDriver)
    {
        super(hierarchicalStreamDriver);
    }
    protected MapperWrapper wrapMapper(MapperWrapper next)
    {
        return new MapperWrapper(next)
        {
            public boolean shouldSerializeMember(Class definedIn, String fieldName)
            {
            	//System.out.println(fieldName);
                if (super.shouldSerializeMember(definedIn, fieldName))
                { 
                    Object field = null;
                    while (definedIn != Object.class)
                    {
                        try
                        {
                            field = definedIn.getDeclaredField(fieldName);
                            break;
                        }
                        catch (NoSuchFieldException e)
                        {
                            definedIn = definedIn.getSuperclass();
                        }
                        catch (Exception e)
                        {
                        	break;
                        }
                    }
                    if(field==null)
                    {
                    	try
                    	{
                    		field=super.realClass(fieldName);
                    	}
                    	catch(Exception e)
                    	{
                    		
                    	}
                    }
                    return field != null;
                } 
                return false;
            } 
        }; 
    } 
	public static String toXml(Object o)
	{
		return xs.toXML(o);
	}
	public static Object fromXml(String xml)
	{
		return xs.fromXML(xml);
	}
	public static String toXml(Object o,HashMap<String,String> fixedkvm)
	{
		Writer writer = new StringWriter();
		HierarchicalStreamWriter hsw=hsd.createWriter(writer);
		KVMFilterWriter fw=new KVMFilterWriter(hsw,fixedkvm);
		xs.marshal(o, fw);
		return writer.toString();		
	}
	public static String toLogX(Object o,String...masks)
	{
		HashMap<String,String> hm=new HashMap<String,String>();
		for(String ms:masks) hm.put(ms, "********");
		return toXml(o,hm);
	}
	public static String toLog(Object o)
	{
		return toXml(o,maskMap);
	}
	public static void addLogMask(String...masks)
	{
		for(String ms:masks) maskMap.put(ms, "********");
	}
	public static void main(String[] args)
	{
//		TrxRecord tr=new TrxRecord();
//		tr.setBusiType("xxx");
//		tr.setPin("1020230232");
//		String xx="<pin>aa</pin><xx>adx</xx><track2>adx</track2><track3></track3>".replaceAll("(<(pin)>[^<>]*</pin>)|(<(track2)>[^<>]*</track2>)|(<(track3)>[^<>]*</track3>)", "<$2$4$6>****</$2$4$6>");
		
//		System.out.println(xx);
//		XStreamIg.addLogMask("pin");
//		System.out.println(Auxs.isBasicClz(int.class));
//		System.out.println(XStreamIg.toLog(tr));
	}
	private static HashMap<String,String> sysMask()
	{
		HashMap<String,String> map=new HashMap<String,String>();
		String masklist=System.getProperty("SYSMASK");
		if(Auxs.empty(masklist)) return map;
		String[] masks=masklist.split(":");
		for(String ms:masks) map.put(ms, "********");
		map.put("pin", "********");
		map.put("track2", "********");
		map.put("track3", "********");
		map.put("track3", "********");
		return map;
	}
	public static XStreamIg xs =new XStreamIg();
	private static HierarchicalStreamDriver hsd=new XppDriver();
	private static HashMap<String,String> maskMap=sysMask();
}
