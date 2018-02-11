package com.tiass.models.entities;

import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Validation;
 
@EntityListeners(UserCredsSocialsFBWatcher.class)  
@Validation("{ " + 
		"user : { $exists: true, $ne: null  }, " + 
		"credType : { $exists: true, $ne: null  }" + 
		" }")
public class UserCredsSocialsFB extends UserCreds{

	public static final String CREDS_TYPE="FB_CREDS";
}
class UserCredsSocialsFBWatcher {
	@PrePersist
	public void prePersist(UserCredsSocialsFB  o) {
		 
		o.setCredType(UserCredsSocialsFB.CREDS_TYPE);
		 
	} 
}