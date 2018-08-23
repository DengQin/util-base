package com.duowan.lobby.util.base.page;

import java.util.List;

public class Page<T> {

	// 总页数
	private int allPageNum;

	// 当前页
	private int curPageNum;

	// 每页记录数
	private int pageSize;

	// 总记录数
	private int allRecordNum;

	// 当前页的记录
	private List<T> records;

	public Page(Page<?> page) {
		this(page.getCurPageNum(), page.getAllRecordNum(), page.getPageSize());
	}

	public Page(int curPageNum, int allRecordNum, int pageSize) {
		this.pageSize = pageSize;
		this.allRecordNum = allRecordNum;
		this.allPageNum = (allRecordNum - 1) / this.pageSize + 1;
		curPageNum = curPageNum < 1 ? 1 : curPageNum;
		this.curPageNum = this.allPageNum < curPageNum ? this.allPageNum : curPageNum;
	}

	public int getStartIndex() {
		return (curPageNum - 1) * pageSize;
	}

	public int[] slider() {
		return slider(7);
	}

	public int[] slider(int slidersCount) {
		if (allPageNum < slidersCount) {
			slidersCount = allPageNum;
		}
		int[] arr = new int[slidersCount];
		if (curPageNum < slidersCount / 2) {
			// 前面
			for (int i = 0, j = 1; i < slidersCount; i++) {
				arr[i] = j++;
			}
		} else if (curPageNum + slidersCount / 2 > allPageNum) {
			// 后面
			for (int i = 0, j = allPageNum - slidersCount + 1; i < slidersCount; i++) {
				arr[i] = j++;
			}
		} else {
			// 中间
			int j = curPageNum - slidersCount / 2;
			j = j < 1 ? 1 : j;
			for (int i = 0; i < slidersCount; i++) {
				arr[i] = j++;
			}
		}
		return arr;
	}

	public int getCurPageNum() {
		return curPageNum;
	}

	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getAllPageNum() {
		return allPageNum;
	}

	public void setAllPageNum(int allPageNum) {
		this.allPageNum = allPageNum;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public int getAllRecordNum() {
		return allRecordNum;
	}

	public void setAllRecordNum(int allRecordNum) {
		this.allRecordNum = allRecordNum;
	}

	@Override
	public String toString() {
		return "Page [allPageNum=" + allPageNum + ", curPageNum=" + curPageNum + ", pageSize=" + pageSize
				+ ", allRecordNum=" + allRecordNum + ", records=" + records + "]";
	}

}
