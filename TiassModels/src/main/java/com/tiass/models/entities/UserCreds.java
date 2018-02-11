package com.tiass.models.entities;

import org.mongodb.morphia.annotations.Entity;  

import com.tiass.models.MongoDoc;
import com.tiass.models.utils.Collections;
 
@Entity(  Collections.USERS_CREDS_COL  ) 
public class UserCreds extends MongoDoc {
	 
	private Users user;
	private String credType;
	 
	public Users getUser () {
		return user;
	}
	public void setUser (Users user) {
		this.user = user;
	}
	public String getCredType() {
		return credType;
	}
	public void setCredType(String credType) {
		this.credType = credType;
	}
	
	
}
 