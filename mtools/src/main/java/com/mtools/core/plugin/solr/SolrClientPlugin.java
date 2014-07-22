/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-7-7
 * version 1.0
 * 说明：solr客户端工具
 */
package com.mtools.core.plugin.solr;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.properties.CoreParams;

/**
 * 功能：
 * 
 * @date 2014-7-7
 */
@Component("solrClientPlugin")
public class SolrClientPlugin<T> extends BasePlugin {

	private String solrUrl = "http://172.16.1.11:8983/solr";
	private HttpSolrServer server = null;
	@Resource(name = "coreParams")
	CoreParams coreParams;

	public void initSolr() {
		server = new HttpSolrServer(coreParams.solrUrl);
	}

	public boolean addOrUpdateIndex(T t) {
		try {
			server.addBean(t);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean batchCreateIndexs(List<T> objs) {
		try {
			server.addBeans(objs);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<T> solrQuery(String keyword, String belong, PageInfo page,
			Class<T> clz) {
		List<T> list = Lists.newArrayList();
		try {
			int start = (Integer.parseInt(page.getPageIndex()) - 1)
					* Integer.parseInt(page.getPageSize());
			SolrQuery query = new SolrQuery(keyword);
			query.setParam("fq", "belong:" + belong);
			query.setStart(start).setRows(Integer.parseInt(page.getPageSize()));
			QueryResponse resp = server.query(query);
			// 查询出来的结果都保存在SolrDocumentList中
			list = resp.getBeans(clz);
			page.setItemCount(Integer.parseInt(String.valueOf(resp.getResults()
					.getNumFound())));
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public SolrDocumentList solrQueryExt(String keyword, String belong,
			PageInfo page) {
		SolrDocumentList sdl = null;
		try {
			int start = (Integer.parseInt(page.getPageIndex()) - 1)
					* Integer.parseInt(page.getPageSize());
			SolrQuery query = new SolrQuery(keyword);
			query.setParam("fq", "belong:" + belong);
			query.setStart(start).setRows(Integer.parseInt(page.getPageSize()));
			QueryResponse resp = server.query(query);
			// 查询出来的结果都保存在SolrDocumentList中
			sdl = resp.getResults();
			System.out.println(sdl.getNumFound());
			page.setItemCount(Integer.parseInt(String.valueOf(sdl.getNumFound())));
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return sdl;
	}
}
