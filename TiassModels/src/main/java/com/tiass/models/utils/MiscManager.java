package com.tiass.models.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBRef;
import com.tiass.models.MongoDoc;

public class MiscManager {
	public static ObjectId deserializeObjectId(String id) {
		if (StringUtils.isBlank(id))
			return null;

		ObjectId real_id = null;
		try {
			String ID = StringUtilities.convertKeyIn(id);

			if (StringUtils.isBlank(ID))
				return null;

			if (ObjectId.isValid(ID)) {
				real_id = new ObjectId(ID);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return real_id;
	}

	public static String serializeObjectId(ObjectId id) {
		String real_id = null;
		try {
			if (id != null) {
				real_id = StringUtilities.convertKeyOut(id.toString());
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return real_id;
	}

	public static Map<String, Object> convertDocToMapJava(MongoDoc doc)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo info = Introspector.getBeanInfo(doc.getClass());
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null)
				map.put(pd.getName(), reader.invoke(doc));
		}
		return map;
	}

	public static Map<String, Object> convertDocToMapApacheCommons(MongoDoc doc)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		@SuppressWarnings("unchecked")
		Map<String, Object> map = BeanUtils.describe(doc);
		return map;
	}

	public static Map<String, Object> convertDocToMapJackson(MongoDoc doc) {
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = objectMapper.convertValue(doc, Map.class);
		/*try {
			System.out.println(" map :: " + objectMapper.writeValueAsString(map));
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}*/
		return map;
	}

	public static <T extends MongoDoc> Query<T> getQuery(Class<T> type, Datastore datastore) {
		Query<T> query = datastore.createQuery(type);
		return query;
	}

	public static void prettyprint(Object pdata) {
		try {
			ObjectMapper oMap = new ObjectMapper();
			System.out.println("prettyprint : " + oMap.writerWithDefaultPrettyPrinter().writeValueAsString(pdata));

		} catch (NullPointerException e1) {
			e1.printStackTrace(System.out);
		} catch (IOException e1) {
			e1.printStackTrace(System.out);
		}
	}

	public static boolean stringValidation(String s, Pattern pattern) {
		boolean isValid = false;
		if (s == null) {
			return isValid;
		}
		//System.out.println("stringValidation String : "+s);
		//System.out.println("stringValidation pattern.pattern() : "+pattern.pattern());
		//System.out.println("stringValidation pattern.toString() : "+pattern.toString());
		isValid = pattern.matcher(s).matches();
		//System.out.println("stringValidation isValid : "+isValid);
		return isValid;
	}
	
	public <T extends MongoDoc> T fillMongoObject(Datastore DS, T object, Class<T> type) {
		if (object.getId() == null) {
			return null;
		}
		List<T> list = DS.find(type).field(Mapper.ID_KEY).equal(object.getId()).asList();
		if (list != null && list.size() == 1)
			return list.get(0);

		return null;
	}
	
	public static <T extends MongoDoc> Query<T> constructQuery(Class<T> type, Datastore DS) {
		Query<T> query = DS.createQuery(type);
		return query;
	}

	public static boolean canSetRef(DBRef ref, MongoDoc doc) {
 		boolean can = 
 				ref != null && 
 				ref.getId() !=null && 
 				ref.getId() instanceof ObjectId &&
 				(doc == null || doc.getId().equals(ObjectId.class.cast(ref.getId())));
				
				 
		return can;
	}
	public static boolean canSetRef(Key<?> ref, MongoDoc doc) {
 		boolean can = 
 				ref != null && 
 				ref.getId() !=null && 
 				ref.getId() instanceof ObjectId &&
 				(doc == null || doc.getId().equals(ObjectId.class.cast(ref.getId())));
				
				 
		return can;
	}

}
