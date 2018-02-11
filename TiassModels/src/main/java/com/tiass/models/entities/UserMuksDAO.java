package com.tiass.models.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.tiass.models.MongoDAO;

public class UserMuksDAO extends MongoDAO<UserMuks> {
	public UserMuksDAO(Class<UserMuks> entityClass, Datastore ds) {
		super(entityClass, ds);

	}
	public UserMuks update(UserMuks e) {
		if (e == null || e.getId() == null)
			return null; 
		return this.updateById(e.getId(), e, new FindAndModifyOptions().upsert(false));
	}
	@Override
	public UserMuks updateById(ObjectId id, UserMuks i, FindAndModifyOptions options) {
		UserMuksWatcher watcher = new UserMuksWatcher();
		watcher.prePersist(i);
		return super.updateById(id, i, options);
	}

	@Override
	public UserMuks updateByQuery(Query<UserMuks> query, UserMuks i, FindAndModifyOptions options) {

		UserMuksWatcher watcher = new UserMuksWatcher();
		watcher.prePersist(i);
		return super.updateByQuery(query, i, options);
	}

	@Override
	public UpdateOperations<UserMuks> updateOperations(UserMuks i) {
		UpdateOperations<UserMuks> ops = this.updateOperations2(i);
		UserMuksWatcher watcher = new UserMuksWatcher();
		watcher.prePersist(i);

		 
		ops.set("times", i.getTimes());

		if (i.getUserIp() != null)
			ops.set("userIp", i.getUserIp());

		if (i.getUserAgent() != null)
			ops.set("userAgent", i.getUserAgent());

		if (i.getUser() != null)
			ops.set("user", i.getUser());

		if (i.getMuk() != null)
			ops.set("muk", i.getMuk());

		return ops;
	}
}
