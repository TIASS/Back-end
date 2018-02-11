package com.tiass.models.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.tiass.models.MongoDAO;

public class UserLocationsDAO  extends MongoDAO<UserLocations> {
	public UserLocationsDAO(Class<UserLocations> entityClass, Datastore ds) {
		super(entityClass, ds);

	}

	@Override
	public UserLocations updateById(ObjectId id, UserLocations i, FindAndModifyOptions options) {
		UserLocationsWatcher watcher = new UserLocationsWatcher();
		watcher.prePersist(i);
		return super.updateById(id, i, options);
	}

	@Override
	public UserLocations updateByQuery(Query<UserLocations> query, UserLocations i, FindAndModifyOptions options) {

		UserLocationsWatcher watcher = new UserLocationsWatcher();
		watcher.prePersist(i);
		return super.updateByQuery(query, i, options);
	}

	@Override
	public UpdateOperations<UserLocations> updateOperations(UserLocations i) {
		UpdateOperations<UserLocations> ops = this.updateOperations2(i);
		UserLocationsWatcher watcher = new UserLocationsWatcher();
		watcher.prePersist(i);
		

		 

		if (i.getUser()!= null)
			ops.set("user", i.getUser());
		
		if (i.getMobilePhone() != null)
			ops.set("mobilePhone", i.getMobilePhone());

		if (i.getLat()!= null)
			ops.set("lat", i.getLat());

		if (i.getLng()!= null)
			ops.set("lng", i.getLng());

		if (i.getAddress()!= null)
			ops.set("address", i.getAddress()); 

		return ops;
	}
}
