package com.tiass.services.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UploadImgData extends MobileData {   
	private int minWidth;
	private int minHeight;
	private int x;
	private int y;
	private int w;
	private int h; 
	@XmlElement
	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	@XmlElement
	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	@XmlElement
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@XmlElement
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@XmlElement
	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	@XmlElement
	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

}
