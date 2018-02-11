package com.tiass.services.data.served;

import org.apache.commons.lang3.StringUtils;

import com.tiass.models.entities.Users;
import com.tiass.services.data.MobileData;

public class LoginDataClient   {
	
	public LoginDataClient( ){ 
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MobileData getMobileData() {
		return mobileData;
	}

	public void setMobileData(MobileData mobileData) {
		this.mobileData = mobileData;
	}

	public static LoginDataClient constructLoginDataClient(Users user, MobileData mobileData, String email) {
		 
	 
		user.setInsertDate(null);
		user.setModifyDate(null);
		 
		if (StringUtils.isNotBlank(email)) {
			int at = StringUtils.indexOf(email, "@");
			email = email.replaceAll("(^[^@]{" + (at < 4 ? 0 : 1) + "}|(?!^)\\G)[^@]", "$1*");
		}  
		LoginDataClient LCD = new LoginDataClient(); 
		LCD.setEmail(email); 
		LCD.setUser(user); 
		LCD.setMobileData(mobileData); 
		return LCD;
	}
	private String email;  
	private MobileData mobileData;  
	private Users user;  
}
