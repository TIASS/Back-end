package com.tiass.services.data; 

/**
 * 
 * @author jimmy bahole
 *	The Mother of All ModelAttribute Classes after the user has logged in
 */
  public class MobileData extends PostData { 
	/**
	 * the id of the connected user
	 */
	private String id; 
	
	private String muk; 

	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	 
	public String getMuk() {
		return muk;
	}

	public void setMuk(String muk) {
		this.muk = muk;
	}
	
	 

}
