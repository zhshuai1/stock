package com.zebrait.common;

public enum Month {
	Jan(0), Feb(1), Mar(2), Apr(3), May(4), Jun(5), Jul(6), Aug(7), Sep(8), Oct(10), Nov(10), Dec(11);
	private int index;

	private Month(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
