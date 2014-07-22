/**
 * 通联支付-研发中心
 * @author zhanggh
 * 2014-5-20
 * version 1.0
 * 说明：
 */
package com.mtools.core.plugin.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.common.collect.Lists;
import com.mtools.core.plugin.BasePlugin;
import com.mtools.core.plugin.entity.PageInfo;

/**
 * 功能：全文搜索工具
 * 
 * @date 2014-5-20
 */
public class LucencePlugin extends BasePlugin {
	private static Directory directory = null;
	private Map<String, Float> scores = new HashMap<String, Float>();
	private Analyzer anal = new IKAnalyzer(true);//中文分词
	private String indexpth;
	private static IndexReader reader=null;
	public LucencePlugin() {
		super();
		
	}

	/**
	 * 功能：分词 2014-5-27
	 */
	public String[] parserStr(String orgStr) throws IOException {
		log.info("将短语：【"+orgStr+"】 进行分词");
		List<String> keywords = Lists.newArrayList();

		// 创建分词对象
		StringReader reader = new StringReader(orgStr);
		// 分词
		TokenStream ts = this.anal.tokenStream("", reader);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		// String[] keywords=new String[term.length()];
		// 遍历分词数据
		StringBuilder sb=new StringBuilder();
		while (ts.incrementToken()) {
			sb.append(term.toString()).append("|");
			keywords.add(term.toString());
		}
		reader.close();
		log.info("短语：【"+orgStr+"】 分词结果:"+sb.toString());
		final int size = keywords.size();
		String[] arr = (String[]) keywords.toArray(new String[size]);
		if (arr != null && arr.length == 0)
			return null;
		else
			return arr;
	}

	/**
	 * 功能：初始化 2014-5-27
	 */
	public void initDirectory() {
		log.info("初始化全文搜索目录环境FSDirectory");
		try {
			directory = FSDirectory.open(new File(this.getIndexpth()));
			log.info("初始化全文搜索目录环境FSDirectory完毕");
			// directory = new RAMDirectory();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 功能：初始化 2014-5-27
	 */
	public void initReader() {
		log.info("初始化全文搜索目录环境IndexReader");
		try {
			this.reader = IndexReader.open(directory, false);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能：建立索引 2014-5-27
	 */
	public void createIndex(List<Document> docs) {
		
		log.info("建立索引");
		IndexWriter writer=null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_35, this.anal));
			writer.deleteAll();

			for (Document doc : docs) {
				writer.addDocument(doc);
			}
			writer.commit();
			writer.forceMerge(1);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：更新索引 2014-5-27
	 */
	public void update(Document doc, Term term) {
		log.info("更新索引" + term.text());
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_35, this.anal));
			writer.updateDocument(term, doc);
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：强制删除索引 2014-5-27
	 */
	public void deleteIndex(Term term) {
		try {
			log.info("强制删除索引");
			IndexReader reader = IndexReader.open(directory, false);
			reader.deleteDocuments(term);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能：单元搜索 2014-5-27
	 */
	public List<Document> termQuery(Term term, int num) {
		List<Document> docs = Lists.newArrayList();
		try {
			log.info("单条件查询");
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(term);
			TopDocs tds = searcher.search(query, num);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				docs.add(doc);
				log.info("(" + sd.doc + "-" + doc.getBoost() + "-" + sd.score
						+ ")");
			}
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs;
	}

	/**
	 * 功能：多条件搜索 2014-5-27
	 */
	public List<Document> booleanQuery(String title,String[] feilds,String keyword,PageInfo page) {
		List<Document> bdocs = Lists.newArrayList();
		try {
			log.info("多个条件联合查询");
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			String str = QueryParser.escape(keyword);
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, title, new IKAnalyzer());
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query baseQuery = queryParser.parse(str);
			FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(title, str), 0.5F);
			BooleanQuery bquery1 = new BooleanQuery();
			BooleanQuery bquery2 = new BooleanQuery();
			for(String key:feilds){
			   TermQuery termquery = new TermQuery(new Term(key, str));
			   bquery1.add(termquery, BooleanClause.Occur.SHOULD);
			}
			bquery1.add(baseQuery, BooleanClause.Occur.SHOULD);
			bquery1.add(fuzzyQuery, BooleanClause.Occur.MUST);
			bquery2.add(bquery1, BooleanClause.Occur.MUST);
			bdocs=pageDeal(searcher, bquery2, page);
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bdocs;
	}

	/**
	 * 功能：多条件搜索 2014-5-27
	 */
	public List<Document> multiFieldQuery(String[] feilds,String keyword,PageInfo page) {
		List<Document> docs = Lists.newArrayList();
		try {
			log.info("multiFieldQuery多个条件联合查询");
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			// 构造布尔查询（可根据你的要求随意组合）
			QueryParser qp = new MultiFieldQueryParser(Version.LUCENE_35, feilds, this.anal);
			qp.setDefaultOperator(QueryParser.AND_OPERATOR);// 设置检索的条件.OR_OPERATOR表示"或"
			Query query = qp.parse(keyword);
			//分页
			docs=pageDeal(searcher, query, page);
			if(docs.size()==0){
				qp.setDefaultOperator(QueryParser.OR_OPERATOR);// 设置检索的条件.OR_OPERATOR表示"或"
				query = qp.parse(keyword);
				docs=pageDeal(searcher, query, page);
			}
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docs;
	}

	/**
     * 删除整个索引库
     * 
     * @return
     */
    public boolean deleteAllIndex() {
    	IndexWriter writer = null;
		Analyzer anal = new IKAnalyzer(true);//中文分词
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_35, this.anal));
			writer.deleteAll();
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
    }
	/**
	 * @return the indexpth
	 */
	public String getIndexpth() {
		return indexpth;
	}

	/**
	 * @param indexpth
	 */
	public void setIndexpth(String indexpth) {
		this.indexpth = indexpth;
	}
	
	private List<Document> pageDeal(IndexSearcher searcher,Query query,PageInfo page) throws NumberFormatException, IOException{
		List<Document> bdocs = Lists.newArrayList();
		TopDocs tds = searcher.search(query, Integer.parseInt(page.getPageIndex())*Integer.parseInt(page.getPageSize()));
		//取出结束位置的数据
		page.setItemCount(tds.totalHits);
		int start=(Integer.parseInt(page.getPageIndex())-1)*Integer.parseInt(page.getPageSize());
		//分页
		for(int i=start;i<tds.scoreDocs.length;i++){
			ScoreDoc sd = tds.scoreDocs[i];
			Document doc = searcher.doc(sd.doc);
			bdocs.add(doc);
			log.info("【命中次数:" + sd.doc + "- 权重:" + doc.getBoost() + "- 相似度:" + sd.score+ "】");
		}
		return bdocs;
	}
	
}
