package com.dacManager.www.entry;

/**
 * ��ҳ����
 * @author Administrator
 *
 */
public class Page {
	
	public Page() {
		this.number = 1;
		this.count = 20;
	}
	
	/**
	 * ҳ��
	 */
	private int number;
	
	/**
	 * ÿҳ��ʾ����
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