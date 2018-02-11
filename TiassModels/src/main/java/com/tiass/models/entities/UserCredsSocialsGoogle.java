package com.tiass.models.entities;

import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Validation;

@EntityListeners(UserCredsSocialsGoogleWatcher.class)
@Validation("{ " + 
		"user : { $exists: true, $ne: null  }, " + 
		"credType : { $exists: true, $ne: null  }" + 
		" }")
public class UserCredsSocialsGoogle extends UserCreds {
	public static final String CREDS_TYPE = "GOOGLE_CREDS";
}

class UserCredsSocialsGoogleWatcher {
	@PrePersist
	public void prePersist(UserCredsSocialsGoogle o) {

		o.setCredType(UserCredsSocialsGoogle.CREDS_TYPE);

	}
}
