package com.tiass.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.tiass.models.entities.UserCreds;
import com.tiass.models.entities.UserCredsDAO;
import com.tiass.models.entities.UserCredsEmail;
import com.tiass.models.entities.Users;
import com.tiass.models.utils.StringUtilities;

@Service("serviceUsers")
public class ServiceUsersImpl implements ServiceUsers {

	@Override
	public UserCreds getUserCreds(String email, String psswrd) {
		if (StringUtils.isBlank(email) || StringUtils.isBlank(psswrd))
			return null;
		Query<UserCredsEmail> query = ManagerDB.constructQuery(UserCredsEmail.class);
		query.field("email").equal(email);
		query.field("passwordEncrypted").equal(StringUtilities.hashPassword(psswrd));
		UserCredsDAO<UserCredsEmail> dao = new UserCredsDAO<UserCredsEmail>(UserCredsEmail.class,
				ManagerDB.getDatastore());
		List<UserCredsEmail> list = dao.executeQuery(query, -1, -1);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public UserCreds getUserCreds(Users user) {
		if (user == null || user.getId() == null) {
			return null;
		}
		Query<UserCreds> query = ManagerDB.constructQuery(UserCreds.class);
		query.field("user").equal(user);
		UserCredsDAO<UserCreds> dao = new UserCredsDAO<UserCreds>(UserCreds.class, ManagerDB.getDatastore());
		List<UserCreds> list = dao.executeQuery(query, -1, -1);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean isUserActif(Users user) {
		return user != null && (user.getActif() != null && user.getActif().booleanValue());
	}

	@Override
	public boolean isEmailExist(String email) {

		if (StringUtils.isBlank(email))
			return false;
		Query<UserCredsEmail> query = ManagerDB.constructQuery(UserCredsEmail.class);
		query.field("email").equal(email);
		UserCredsDAO<UserCredsEmail> dao = new UserCredsDAO<UserCredsEmail>(UserCredsEmail.class,
				ManagerDB.getDatastore());
		List<UserCredsEmail> list = dao.executeQuery(query, -1, -1);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

}
