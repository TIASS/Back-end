package com.tiass.services.data;
 
import javax.xml.bind.annotation.XmlElement;

/**
 * OBJECT FOR PAGINATION Sorting
 * 
 * @author JIMMY BAHOLE
 *
 */
public class PaginationFilter  {
	public static final String clssInteger = "i"; 
	public static final String clssString = "s"; 
	public static final String clssLong = "l"; 
	public static final String clssDouble = "d"; 
	public static final String clssObjectId = "oi"; 
	public static final String clssBoolean = "boo"; 
	public static final String clssNull = "n"; 

	private String	field;
	private String	value; 
	private String	clss; 

	@XmlElement
	public String getField() {
		
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@XmlElement
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement
	public String getClss() {
		return clss;
	}

	public void setClss(String clss) {
		this.clss = clss;
	}

 

}