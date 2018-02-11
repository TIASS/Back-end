package com.tiass.services.data;

import javax.xml.bind.annotation.XmlElement;

public class PaginationSorting {

	public static final int descending = -1;
	public static final int ascending = 1;

	private String field;
	private int direction;

	@XmlElement
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@XmlElement
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}