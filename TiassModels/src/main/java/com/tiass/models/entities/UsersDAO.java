package com.tiass.models.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.tiass.models.MongoDAO;

public class UsersDAO  extends MongoDAO<Users> {
	public UsersDAO(Class<Users> entityClass, Datastore ds) {
		super(entityClass, ds);

	}
	
	@Override
	public Users update(Users e) {
		if (e == null || e.getId() == null)
			return null; 
		return this.updateById(e.getId(), e, new FindAndModifyOptions().upsert(false));
	}
	@Override
	public Users updateById(ObjectId id, Users i, FindAndModifyOptions options) {
		UsersWatcher watcher = new UsersWatcher();
		watcher.prePersist(i);
		return super.updateById(id, i, options);
	}

	@Override
	public Users updateByQuery(Query<Users> query, Users i, FindAndModifyOptions options) {

		UsersWatcher watcher = new UsersWatcher();
		watcher.prePersist(i);
		return super.updateByQuery(query, i, options);
	}

	@Override
	public UpdateOperations<Users> updateOperations(Users i) {
		UpdateOperations<Users> ops = this.updateOperations2(i);
		UsersWatcher watcher = new UsersWatcher();
		watcher.prePersist(i);
		

		ops.set("gender", i.getGender()); 
	 
		if (i.getActif() != null)
			ops.set("actif", i.getActif()); 
		if (i.getSurName()!= null)
			ops.set("surName", i.getSurName());

		if (i.getName()!= null)
			ops.set("name", i.getName());

		return ops;
	}
}
