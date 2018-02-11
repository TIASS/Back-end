package com.tiass.models;

//import java.lang.reflect.Field;
import java.util.List;
//import java.util.Map;
//import java.util.Set;

import org.bson.types.ObjectId;
//import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.Sort;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.WriteResult;  
public abstract class MongoDAO<T extends MongoDoc> extends BasicDAO<T, String> {
	public MongoDAO(Class<T> entityClass, Datastore ds) {
		super(entityClass, ds);

	}

	public T saveEntity(T entity, InsertOptions options) {
		Key<T> key = this.getDatastore().save(entity, options != null ? options : new InsertOptions());
		// T req = this.findById(ObjectId.class.cast(key.getId()));
		if (key == null)
			return null;

		T req = this.getDatastore().getByKey(this.getEntityClass(), key);
		return req;
	}

	public List<T> saveEntityList(Iterable<T> entities, InsertOptions options) {
		Iterable<Key<T>> keys = this.getDatastore().save(entities, options != null ? options : new InsertOptions());
		if (keys == null)
			return null;

		List<T> list = this.getDatastore().getByKeys(this.getEntityClass(), keys);
		return list;
	}

	public T updateOne(ObjectId id, T i, boolean createIfMissing) {
		try {
			i.setVersion(null);

			Query<T> query = this.getDatastore().createQuery(this.getEntityClass()).field(Mapper.ID_KEY).equal(id);
			UpdateResults results = this.getDatastore().updateFirst(query, i, createIfMissing);
			if (results.getUpdatedExisting()) {
				T req = this.findById(id);
				return req;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	public T findByValue(String field, Object value) {
		T o = null;
		List<T> list = this.getDatastore().find(this.getEntityClass()).field(field).equal(value).asList();
		if (list != null && list.size() == 1) {
			o = list.get(0);
		}
		return o;
	}

	public T findByValueFirst(String field, Object value) {
		T o = null;
		List<T> list = this.getDatastore().find(this.getEntityClass()).field(field).equal(value).asList();
		if (list != null && list.size() > 0) {
			o = list.get(0);
		}
		return o;
	}

	public T findById(ObjectId id) {
		T o = null;
		List<T> list = this.getDatastore().find(this.getEntityClass()).field(Mapper.ID_KEY).equal(id).asList();
		if (list != null && list.size() == 1) {
			o = list.get(0);
		}
		return o;
	}

	public List<T> findQuery(Query<T> query, FindOptions fo) {
		QueryResults<T> q = this.find(query);
		if (q != null) {
			return q.asList(fo != null ? fo : new FindOptions());
		}
		return null;
	}


	 
	public T update(T e) {
		if (e == null || e.getId() == null)
			return null; 
		return this.updateById(e.getId(), e, new FindAndModifyOptions().upsert(false));
	}
	
	public T set(T e) {
		if (e == null || e.getId() == null)
			return null;
		
		e = this.findById(e.getId());
		return e;
	}

	public Boolean remove(T e) {
		if (e == null || e.getId() == null)
			return null;

		WriteResult r = this.delete(e);
		return r.getN() > 0;
	}
 
	public List<T> executeQuery(Query<T> query, int limit, int skip) {
	 
		FindOptions fo = new FindOptions();
		if (limit > 0) 
			fo.limit(limit);
		
		if (skip > 0) 
			fo.skip(skip);
		
		return this.executeQuery(query, fo);
	}
	public List<T> executeQuery(Query<T> query , FindOptions fo) {
		  
		return this.findQuery(query, fo);
	}
	public List<T> findAll() {
		return this.getDatastore().find(this.getEntityClass())
				.order(Sort.descending("insertDate"), Sort.descending("modifyDate")).asList();
	}

	public T updateById(ObjectId id, T i, FindAndModifyOptions options) {
		T result = null;

		try {
			Query<T> query = this.getDatastore().createQuery(this.getEntityClass()).field(Mapper.ID_KEY).equal(id);
			result = this.getDatastore().findAndModify(query, updateOperations(i),
					options != null ? options : new FindAndModifyOptions());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return result;
	}

	public T updateByQuery(Query<T> query, T i, FindAndModifyOptions options) {
		T result = null;
		try {
			result = this.getDatastore().findAndModify(query, updateOperations(i),
					options != null ? options : new FindAndModifyOptions());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return result;
	}

	public boolean updateSetByQuery(Query<T> query, UpdateOperations<T> operations) {
		boolean updated = false;
		try {
			UpdateResults upResults = this.getDatastore().update(query, operations);
			updated = upResults.getUpdatedCount() > 0 || upResults.getUpdatedExisting()
					|| upResults.getInsertedCount() > 0;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return updated;
	}

	public UpdateOperations<T> updateOperations2(T i) {
		UpdateOperations<T> ops = this.getDatastore().createUpdateOperations(this.getEntityClass());

		if (i.getModifyDate() != null)
			ops.set("modifyDate", i.getModifyDate());

		if (i.getInsertDate() != null)
			ops.set("insertDate", i.getInsertDate());
		/*
		 * ObjectMapper objectMapper = new ObjectMapper(); //
		 * objectMapper.configure( Feature.WRITE_DATES_AS_TIMESTAMPS, false); //
		 * // it's true by default
		 * 
		 * @SuppressWarnings("unchecked") Map<String, Object> map =
		 * objectMapper.convertValue(i, Map.class); Set<Map.Entry<String,
		 * Object>> sets = map.entrySet();
		 * 
		 * for (Map.Entry<String, Object> entry : sets) { if
		 * ("id".equals(entry.getKey())) continue; if (entry.getValue() == null)
		 * continue;
		 * 
		 * ops.set(entry.getKey(), entry.getValue()); }
		 */

		/*
		 * System.out.println("second map  ::: "); for (Field field :
		 * i.getClass().getDeclaredFields()) { String name = field.getName(); if
		 * ("id".equals(name)) continue;
		 * 
		 * boolean isID =
		 * field.isAnnotationPresent(org.mongodb.morphia.annotations.Id.class);
		 * boolean isVersion =
		 * field.isAnnotationPresent(org.mongodb.morphia.annotations.Version.
		 * class); boolean isTransient =
		 * field.isAnnotationPresent(org.mongodb.morphia.annotations.Transient.
		 * class); boolean isNotSaved =
		 * field.isAnnotationPresent(org.mongodb.morphia.annotations.NotSaved.
		 * class); if (isID || isVersion || isTransient || isNotSaved) continue;
		 * Object o = map.get(name); if (o == null) continue;
		 * System.out.println("name : "+name+" - Value : "+o+" - Class : "+o.
		 * getClass().getName()); ops.set(name, o); }
		 */

		return ops;
	}

	public abstract UpdateOperations<T> updateOperations(T i);
}
