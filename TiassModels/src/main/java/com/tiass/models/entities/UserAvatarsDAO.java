 
package com.tiass.models.entities;


import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions; 
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.tiass.models.MongoDAO; 
 
public class UserAvatarsDAO extends MongoDAO<UserAvatars> {
	public UserAvatarsDAO(Class<UserAvatars> entityClass, Datastore ds) {
		super(entityClass, ds);

	}

	@Override
	public UserAvatars updateById(ObjectId id, UserAvatars i, FindAndModifyOptions options) {
		UserAvatarsWatcher watcher = new UserAvatarsWatcher();
		watcher.prePersist(i);
		return super.updateById(id, i, options);
	}

	@Override
	public UserAvatars updateByQuery(Query<UserAvatars> query, UserAvatars i, FindAndModifyOptions options) {

		UserAvatarsWatcher watcher = new UserAvatarsWatcher();
		watcher.prePersist(i);
		return super.updateByQuery(query, i, options);
	}

	@Override
	public UpdateOperations<UserAvatars> updateOperations(UserAvatars i) {
		UpdateOperations<UserAvatars> ops = this.updateOperations2(i);
		UserAvatarsWatcher watcher = new UserAvatarsWatcher();
		watcher.prePersist(i); 

		if (i.getFile() != null)
			ops.set("file", i.getFile());

		if (i.getCurrent() != null)
			ops.set("current", i.getCurrent());

		if (i.getUser() != null)
			ops.set("user", i.getUser());

		if (i.getActif() != null)
			ops.set("actif", i.getActif());

		return ops;
	}
}
