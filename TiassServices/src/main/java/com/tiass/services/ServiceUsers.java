package com.tiass.services;

import com.tiass.models.entities.UserAvatars;
import com.tiass.models.entities.UserCreds;
import com.tiass.models.entities.UserMuks;
import com.tiass.models.entities.Users;

public interface ServiceUsers {
	UserCreds getUserCreds(String email, String psswrd);
	UserCreds getUserCreds(Users user);
	 
	boolean isUserActif(Users user);
	boolean isEmailExist(String email);
	
	
	
	public class UserEntities{
		
		Users user;
		UserMuks muk;
		UserCreds creds;
		UserAvatars avatar;
		public Users getUser() {
			return user;
		}
		public void setUser(Users user) {
			this.user = user;
		}
		public UserMuks getMuk() {
			return muk;
		}
		public void setMuk(UserMuks muk) {
			this.muk = muk;
		}
		public UserCreds getCreds() {
			return creds;
		}
		public void setCreds(UserCreds creds) {
			this.creds = creds;
		}
		public UserAvatars getAvatar() {
			return avatar;
		}
		public void setAvatar(UserAvatars avatar) {
			this.avatar = avatar;
		}
		
		
	}
}
