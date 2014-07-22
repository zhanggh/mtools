/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-6-30
 * version 1.0
 * 说明：
 */
package com.mtools.core.plugin.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * 
 * @date 2014-6-30
 */
@Component("coreParams")
public class CoreParams {

	@Value("${reloadIndex}")
	public String reloadIndex;
	@Value("${staticFile}")
	public String staticFile;
	@Value("${solrUrl}")
	public String solrUrl;
	@Value("${ssl}")
	public String ssl;
	@Value("${tranxUri}")
	public String tranxUri;
	@Value("${tranxUrl}")
	public String tranxUrl;

	
	public String getTranxUrl() {
		return tranxUrl;
	}

	public void setTranxUrl(String tranxUrl) {
		this.tranxUrl = tranxUrl;
	}

	public String getTranxUri() {
		return tranxUri;
	}

	public void setTranxUri(String tranxUri) {
		this.tranxUri = tranxUri;
	}

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
	}

	public String getSsl() {
		return ssl;
	}

	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the staticFile
	 */
	public String getStaticFile() {
		return staticFile;
	}

	/**
	 * @param staticFile
	 *            the staticFile to set
	 */
	public void setStaticFile(String staticFile) {
		this.staticFile = staticFile;
	}

	/**
	 * @return the reloadIndex
	 */
	public String getReloadIndex() {
		return reloadIndex;
	}

	/**
	 * @param reloadIndex
	 *            the reloadIndex to set
	 */
	public void setReloadIndex(String reloadIndex) {
		this.reloadIndex = reloadIndex;
	}
}
