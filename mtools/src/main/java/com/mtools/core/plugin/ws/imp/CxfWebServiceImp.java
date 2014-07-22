package com.mtools.core.plugin.ws.imp;

import com.mtools.core.plugin.constant.CoreConstans;
import com.mtools.core.plugin.ws.CxfWebService;

public class CxfWebServiceImp implements CxfWebService {

	public String getMtoolsVersion() {
		 
		return CoreConstans.MTOOLS_VERSION;
	}

}
