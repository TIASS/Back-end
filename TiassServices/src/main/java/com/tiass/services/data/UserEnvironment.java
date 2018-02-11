package com.tiass.services.data;

import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSessionBindingEvent;

/**
 * 
 * @author Jimmy Bahole
 *
 */
public class UserEnvironment extends SessionObjectUser {

	private PostData	postData	= null;
	private Locale		locale		= null;

	public UserEnvironment() {

	}

	public UserEnvironment(PostData postData, List<String> pltfrmLangs) { 
		this.createPostData(postData, pltfrmLangs); 
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public PostData getPostData() {
		return postData;
	}

	
	private void createPostData (PostData postData, List<String> pltfrmLangs){
		this.setPostData(postData);
		try {
			this.getLocaleFromLanguage(pltfrmLangs);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public void setPostData(PostData postData) {
		this.postData = postData;
		
	}

	/**
	 * TODO : CHECK FOR TAKEN IN CHARGE LANGUAGE ONLY
	 * 
	 * @throws Exception
	 */
	private void getLocaleFromLanguage(List<String> pltfrmLangs) throws Exception {
		Locale locale = null;
		try {
			String l[] = new String[2];
				l[0] = "fr";
				l[1] = "FR"; 
			
			if (
					this.getPostData().getLang() != null && 
					pltfrmLangs != null &&  
					pltfrmLangs.contains(this.getPostData().getLang() ) && 
					this.getPostData().getLang().contains("-")) {
				l[0] = this.getPostData().getLang().split("-")[0];
				l[1] = this.getPostData().getLang().split("-")[1]; 
			} else if ( 
					pltfrmLangs != null &&  
					!pltfrmLangs.isEmpty() &&  
					pltfrmLangs.get(0) != null && 
					pltfrmLangs.get(0).contains("-")) { 
				l[0] = pltfrmLangs.get(0).split("-")[0];
				l[1] = pltfrmLangs.get(0).split("-")[1]; 
			} 
			
			locale = new Locale.Builder().setLanguage(l[0]).setRegion(l[1]).build();
		} catch (IllformedLocaleException e) {
			locale = new Locale.Builder().setLanguage("fr").setRegion("FR").build();
		} catch (Exception e) {
			locale = new Locale.Builder().setLanguage("fr").setRegion("FR").build();
			e.printStackTrace(System.out);
		}
		this.setLocale(locale);

	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		super.valueBound(event);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		super.valueUnbound(event);
	}

}