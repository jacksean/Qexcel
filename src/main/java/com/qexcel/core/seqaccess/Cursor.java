package com.qexcel.core.seqaccess;

/**
 * 类Cursor.java的实现描述：游标 
 * @author sean 2018年10月31日 上午11:03:03
 */
public class Cursor {
	private int index = -1;

	public Cursor(int index) {
		super();
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void jumpNums(int nums) {
		setIndex(getIndex()+nums);
	}
	
	public void next() {
		setIndex(getIndex()+1);
	}

    @Override
    public String toString() {
        return "Cursor [index=" + index + "]";
    }
}
