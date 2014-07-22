package com.mtools.core.plugin.helper;

 
import com.thoughtworks.xstream.XStream;

public class XmlConverter extends XStream{
	private void initForm(Class...clszz){
		this.autodetectAnnotations(true);
		this.setMode(XStream.NO_REFERENCES);
		if(clszz!=null&&clszz.length>0){
			for(Class cls:clszz){
				this.processAnnotations(cls);
				this.addDefaultImplementation(cls, Object.class);
			}
		}
	}
	
//	public String toRspXml(MsgRsp rsp){
//		if(rsp==null) return null;
//		if(rsp.getBody()!=null)
//			this.init(true, XStream.NO_REFERENCES, rsp.getBody().getClass());
//		this.alias("RECORD", DetecRecords.class);
//		this.alias("AD", AdvertRsp.class);
//		this.alias("HELP", HelpInfo.class);
//		this.processAnnotations(MsgRsp.class);
//		return this.toXML(rsp);
//	}
//	
//	public String toReqXml(MsgReq req){
//		if(req==null) return null;
//		this.initForm();
//		return this.toXML(req);
//	}	
//	
//	public String toXMLExt(Object obj){
//		if(obj==null) return null;
//		this.initForm();
//		return this.toXML(obj);
//	}
//	
//	public MsgReq fromReqXml(String xml,Class...bodyclszz){
//		if(xml==null||xml.trim().length()==0) return null;
//		this.initForm(bodyclszz);
//		this.processAnnotations(MsgReq.class);
//		return (MsgReq)this.fromXML(xml);
//	}
//	
//	public MsgRsp fromRspXml(String xml,Class...bodyclszz){
//		if(xml==null||xml.trim().length()==0) return null;
//		this.initForm(bodyclszz);
////		if(bodyclszz.length==1){
////			this.alias("BODY", bodyclszz[0]);
////		}
//		this.processAnnotations(MsgRsp.class);
//		MsgRsp rsp=(MsgRsp)this.fromXML(xml);
//		return rsp;
//	}
//	
//	public Object fromXml(String xml,Class...bodyclszz){
//		if(xml==null||xml.trim().length()==0) return null;
//		this.initForm(bodyclszz);
//		return this.fromXML(xml);
//	}
//	private void init(boolean autodetectann,int mode,Class...clszz){
//		this.autodetectAnnotations(true);
//		this.setMode(XStream.NO_REFERENCES);
//		if(clszz!=null&&clszz.length>0){
//			for(Class cls:clszz){
//				this.processAnnotations(cls);
//				this.addDefaultImplementation(cls, Object.class);
//			}
//		}
//	}
}