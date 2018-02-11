package com.tiass.models.entities;

import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Validation;

import com.tiass.models.utils.StringUtilities;
@EntityListeners(UserCredsEmailWatcher.class)  
@Validation("{ " 
		+ "email : { $exists: true, $ne: null  }, "
		+ "password : { $exists: true, $ne: null  }, " 
		+ "passwordEncrypted : { $exists: true, $ne: null  }, " 
		+ "user : { $exists: true, $ne: null  }, "
		+ "credType : { $exists: true, $ne: null  }"
		+ " }")
 
public class UserCredsEmail extends UserCreds {
	@Indexed(options = @IndexOptions(unique = true))
	private String email;
	private String password;
	@Indexed(options = @IndexOptions(unique = true)) 
	private String passwordEncrypted;
	 
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordEncrypted() {
		return passwordEncrypted;
	}
	public void setPasswordEncrypted(String passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
	} 
	public static final String CREDS_TYPE="EMAIL_CREDS";
}
class UserCredsEmailWatcher {
	@PrePersist
	public void prePersist(UserCredsEmail  o) {
		 String encrypted = null;
		if (o.getPassword() != null /* && StringUtilities.passwordValidation(o.getPassword() ) */  ) {
			encrypted = StringUtilities.hashPassword(o.getPassword());
 		}
		o.setCredType(UserCredsEmail.CREDS_TYPE);
		o.setPasswordEncrypted(encrypted);   
	} 
}