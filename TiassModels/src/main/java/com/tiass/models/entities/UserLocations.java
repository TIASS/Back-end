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



@EntityListeners(UserLocationsWatcher.class)
@Entity(Collections.USERS_LOCATIONS)
@Validation("{ " +  
		"user : { $exists: true, $ne: null }, " + 
		"mobilePhone : { $exists: true, $ne: null } "	+   
		" }")
public class UserLocations  extends MongoDoc {

	/**
	 * the owner of the photo
	 */
	@Reference
	@Indexed(options = @IndexOptions(unique = false))
	private Users user;
	private String mobilePhone;
	private Long lat;
	private Long lng;
	private String address;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public Long getLat() {
		return lat;
	}
	public void setLat(Long lat) {
		this.lat = lat;
	}
	public Long getLng() {
		return lng;
	}
	public void setLng(Long lng) {
		this.lng = lng;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
 
}

class UserLocationsWatcher {
	@PrePersist
	public void prePersist(UserLocations o) { 
	}
}
