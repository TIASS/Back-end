 
package com.tiass.models.entities;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Validation;

import com.tiass.models.MongoDoc;
import com.tiass.models.utils.Collections;
@EntityListeners(UserAvatarsWatcher.class)  
@Entity(Collections.USERS_AVATARS )
@Validation("{ "
		+ "user : { $exists: true, $ne: null }, "
		+ "file : { $exists: true, $ne: null }, "  
		+ "current : { $exists: true, $ne: null }, " 
		+ "actif : { $exists: true, $ne: null }"  
 		+ " }") 
public class UserAvatars extends MongoDoc { 
	

	/**
	 * the owner of the photo
	 */
	@Reference
	@Indexed(options = @IndexOptions(unique = false))
	private Users user;
	
	/**
	 * the name of the file. the folder path is defined in a property file
	 */
	private String file;
	
	/**
	 * if is the current photo
	 */
	private Boolean current;
	
	/**
	 * if is displayable
	 */
	private Boolean actif;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}
	
	
}class UserAvatarsWatcher {
	@PrePersist
	public void prePersist(UserAvatars o) { 
	 
	}
}
