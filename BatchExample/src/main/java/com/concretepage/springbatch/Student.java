package com.concretepage.springbatch;

import java.io.Serializable;

public class Student implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5457300629659166426L;
	private String stdId;
	private int subMarkOne;
	private int subMarkTwo;

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public int getSubMarkOne() {
		return subMarkOne;
	}

	public void setSubMarkOne(int subMarkOne) {
		this.subMarkOne = subMarkOne;
	}

	public int getSubMarkTwo() {
		return subMarkTwo;
	}

	public void setSubMarkTwo(int subMarkTwo) {
		this.subMarkTwo = subMarkTwo;
	}
}
