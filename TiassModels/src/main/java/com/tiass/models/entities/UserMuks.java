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
@EntityListeners(UserMuksWatcher.class)  

@Entity( Collections.USERS_MUK_COL )
@Validation("{ "
		+ "user : { $exists: true, $ne: null  }, "
		+ "muk : { $exists: true, $ne: null  }, "
		+ "userAgent : { $exists: true, $ne: null  }, "
		+ "userIp : { $exists: true, $ne: null  }" 
		+ " }") 
/**
 * 
 * @author JIMMY BHL
 * dont need to declare multiple indexes cause the muk is already uniq
 */
//@Indexes(@Index( fields = { @Field("muk"), @Field("userAgent"), @Field("userIp")}, options = @IndexOptions(unique = true))) 
public class UserMuks extends MongoDoc {
	@Reference 
	@Indexed(options = @IndexOptions(unique = false))
	private Users user;

	@Indexed(options = @IndexOptions(unique = true))
	private String muk; 
	private String userAgent;
	private String userIp;
	private int times;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getMuk() {
		return muk;
	}
	public void setMuk(String muk) {
		this.muk = muk;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	} 


}
class UserMuksWatcher   {
	@PrePersist
	public void prePersist( UserMuks  o) {
		 
	} 
}