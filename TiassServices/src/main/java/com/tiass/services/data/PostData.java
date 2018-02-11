package com.tiass.services.data;

/**
 * 
 * @author Jimmy Bahole
 * The Mother of All ModelAttribute Classes
 *
 */
 
public class PostData { 
 
	
	public static final String USERAGENT_UNDEFINED = "UNDEFINED";
	public static final String IPVISITOR_UNDEFINED = "UNDEFINED";
	public static final String TZOFFSET_UNDEFINED = "UNDEFINED";
	
	/**
	 * the user language
	 */
	private String lang;
	
 
	
	/**
	 * the user Locale ID I18N
	 * 
	 */
	private String localeId; 
	/**
	 * the user Machine
	 */
	private String ipVisitor;
	private String userAgent; 
	private String tzoffset = "";

	 
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	 

 
	public String getLocaleId() {
		return localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}
	 

	 
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
 
 
	public String getTzoffset() {
		return tzoffset;
	}

	public void setTzoffset(String tzoffset) {
		this.tzoffset = tzoffset;
	}

	 
	public String getIpVisitor() {
		return ipVisitor;
	}

	public void setIpVisitor(String ipVisitor) {
		this.ipVisitor = ipVisitor;
	}
}
