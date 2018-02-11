package com.tiass.models.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.tiass.models.MongoDAO;

public class UserCredsDAO<T extends UserCreds> extends MongoDAO<T> {
	public UserCredsDAO(Class<T> entityClass, Datastore ds) {
		super(entityClass, ds);
		
		
	}

	@Override
	public T updateById(ObjectId id, T i, FindAndModifyOptions options) {
 		// UserCredsSocialsFB.java
		// UserCredsSocialsGoogle.java
		/// UserCredsSocialsLinkedIn.java
		if (i instanceof UserCredsEmail) {
			UserCredsEmailWatcher watcher = new UserCredsEmailWatcher();
			watcher.prePersist(UserCredsEmail.class.cast(i));
		} else {

		}

		return super.updateById(id, i, options);
	}

	@Override
	public T updateByQuery(Query<T> query, T i, FindAndModifyOptions options) {

		if (i instanceof UserCredsEmail) {
			UserCredsEmailWatcher watcher = new UserCredsEmailWatcher();
			watcher.prePersist(UserCredsEmail.class.cast(i));
		} else {

		}
		return super.updateByQuery(query, i, options);
	}

	@Override
	public UpdateOperations<T> updateOperations(T i) {
		UpdateOperations<T> ops = this.updateOperations2(i);
		if (i instanceof UserCredsEmail) {
			UserCredsEmailWatcher watcher = new UserCredsEmailWatcher();
			watcher.prePersist(UserCredsEmail.class.cast(i));

			if (UserCredsEmail.class.cast(i).getEmail() != null)
				ops.set("email", UserCredsEmail.class.cast(i).getEmail());

			if (UserCredsEmail.class.cast(i).getPassword() != null)
				ops.set("password", UserCredsEmail.class.cast(i).getPassword());

			if (UserCredsEmail.class.cast(i).getPasswordEncrypted() != null)
				ops.set("passwordEncrypted", UserCredsEmail.class.cast(i).getPasswordEncrypted());

		} else {

		}
		if (i.getUser() != null)
			ops.set("user", i.getUser());

		return ops;
	}
}