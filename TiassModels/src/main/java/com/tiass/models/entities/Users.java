package com.tiass.models.entities;

import java.util.Arrays;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Validation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiass.models.MongoDoc;
import com.tiass.models.utils.Collections;

@EntityListeners(UsersWatcher.class)
@Entity(Collections.USERS_COL)
@Validation("{ " +  
		"name : { $exists: true, $ne: null }, " + 
		"surName : { $exists: true, $ne: null }, "	+  
		"actif : { $exists: true, $ne: null }, " + 
		"gender : { $exists: true, $ne: null }" + 
		" }")
public class Users extends MongoDoc {
	private String name;
	private String surName;
	@JsonIgnore
	private Boolean actif;
	@JsonIgnore
	private int gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public static final int UNDEFINED = -1;
	public static final int MALE = 1;
	public static final int FEMALE = 0;
}

class UsersWatcher {
	@PrePersist
	public void prePersist(Users o) {
		
		if(!Arrays.asList(Users.FEMALE, Users.MALE).contains(o.getGender()))
			o.setGender(Users.UNDEFINED);

	}
}
