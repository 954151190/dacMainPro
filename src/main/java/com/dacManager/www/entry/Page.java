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
}