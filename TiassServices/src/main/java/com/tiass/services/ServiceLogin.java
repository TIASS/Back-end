package com.tiass.services;
 
import com.tiass.services.data.LoginData;
import com.tiass.services.data.MobileData;
import com.tiass.services.data.SignInData;
import com.tiass.services.data.served.LoginDataClient;

public interface ServiceLogin  {
	
	LoginDataClient login(LoginData data);
	LoginDataClient mukin(MobileData data);
	LoginDataClient signin(SignInData data);
	
}
