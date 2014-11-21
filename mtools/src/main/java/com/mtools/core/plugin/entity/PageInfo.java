/**
 * PageInfo.java
 * 2014-4-19
 */
package com.mtools.core.plugin.entity;


import com.mtools.core.plugin.helper.FuncUtil;

/**
 * @author zhang
 *
 * 2014-4-19
 */
public class PageInfo  extends BaseDbStruct{
	 
	/**
	 * 说明：
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5232461832574358861L;
	public String pageIndex;
	public String pageSize;
	public String pageName;
	public int itemCount;
	public int totalPage;
	public Sort sort=new Sort();
	/**
	 * @return the sort
	 */
	public Sort getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}
	/**
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		if(FuncUtil.isEmpty(pageIndex)){
			pageIndex="1";
		}
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	/**
	 * @return the pageSize
	 */
	public String getPageSize() {
		if(FuncUtil.isEmpty(pageSize)){
			pageSize="10";
		}
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the itemCount
	 */
	public int getItemCount() {
		return itemCount;
	}
	/**
	 * @param itemCount the itemCount to set
	 */
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	/**
	 * @return the totalPage
	 */
	public int getTotalPage() {
		int pageCount=this.itemCount%Integer.parseInt(this.getPageSize());
		if(pageCount==0){
			return (this.itemCount/Integer.parseInt(this.getPageSize()));
		}
		return (this.itemCount/Integer.parseInt(this.getPageSize()))+1;
	}
	/**
	 * @param totalPage the totalPage to set
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
