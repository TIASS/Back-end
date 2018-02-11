package com.tiass.models.entities;

import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Validation;

@EntityListeners(UserCredsSocialsLinkedInWatcher.class)
@Validation("{ " + 
		"user : { $exists: true, $ne: null  }, " + 
		"credType : { $exists: true, $ne: null  }" + 
		" }")
public class UserCredsSocialsLinkedIn extends UserCreds {
	public static final String CREDS_TYPE = "LINKEDIN_CREDS";
}

class UserCredsSocialsLinkedInWatcher {
	@PrePersist
	public void prePersist(UserCredsSocialsLinkedIn o) {

		o.setCredType(UserCredsSocialsLinkedIn.CREDS_TYPE);

	}
}