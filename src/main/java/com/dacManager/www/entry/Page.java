package com.dacManager.www.entry;

/**
 * 分页对象
 * @author Administrator
 *
 */
public class Page {
	
	public Page() {
		this.number = 1;
		this.count = 20;
	}
	
	/**
	 * 页码
	 */
	private int number;
	
	/**
	 * 每页显示条数
	 */
	private int count;

	/**
	 * 信息总数
	 */
	private long allCount;
	
	/**
	 * 分页总页数
	 * @return
	 */
	private long allPage;
	
	public long getAllCount() {
		return allCount;
	}

	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getAllPage() {
		return allPage;
	}

	public void setAllPage(long allPage) {
		this.allPage = allPage;
	}
}