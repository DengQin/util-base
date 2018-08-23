package com.duowan.lobby.util.base.page;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.duowan.lobby.util.base.StringUtil;

public class PageQuery {
	private static final int PAGE_SIZE = 10;
	// 当前页
	private int curPageNum;

	private Map<String, Object> queryParams = new HashMap<String, Object>();

	private String orderBy = "";

	// 每页记录数
	private int pageSize = PAGE_SIZE;

	public PageQuery() {
		super();
	}

	public PageQuery(int curPageNum) {
		super();
		this.curPageNum = curPageNum;
	}

	@JsonIgnore
	public boolean containsQueryParam(String key) {
		return this.queryParams.containsKey(key);
	}

	@JsonIgnore
	public void addQueryParam(String key, Object value) {
		if (value == null) {
			return;
		}
		this.queryParams.put(key, value);
	}

	@JsonIgnore
	public void addQueryParam(String key, String value) {
		if (StringUtil.isBlank(value)) {
			return;
		}
		this.queryParams.put(key, value);
	}

	public int getCurPageNum() {
		return curPageNum;
	}

	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	@Deprecated
	public Map<String, Object> getParamMap() {
		return queryParams;
	}

	@Deprecated
	public void setParamMap(Map<String, Object> paramMap) {
		this.queryParams = paramMap;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "PageQuery [curPageNum=" + curPageNum + ", queryParams=" + queryParams + ", orderBy=" + orderBy
				+ ", pageSize=" + pageSize + "]";
	}

}
