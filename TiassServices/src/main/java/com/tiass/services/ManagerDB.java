package com.tiass.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/*
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;*/
import org.mongodb.morphia.Datastore;
//import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.Morphia;
//import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.tiass.models.MongoDoc;
import com.tiass.models.utils.serializers.BigIntegerConverter;

public class ManagerDB { 
	 
	private static List<String> DATASTORE_PACKAGES = null;
	private static String HOST = null;
	private static int PORT = -1;
	private static String USER = null;
	private static String PASSWORD = null;
	private static String DATABASENAME =  null;
	
	private static MongoClient client;
	private static MongoDatabase db;
	private static Datastore store;

	public static boolean setMongoDatabase(String HOST, int PORT, String USER, String PASSWORD, String DATABASENAME, List<String> DATASTORE_PACKAGES) {
		ManagerDB.HOST = HOST;
		ManagerDB.PORT = PORT;
		ManagerDB.USER = USER;
		ManagerDB.PASSWORD = PASSWORD;
		ManagerDB.DATABASENAME = DATABASENAME;
		ManagerDB.DATASTORE_PACKAGES = DATASTORE_PACKAGES;
		if( 
				StringUtils.isBlank( ManagerDB.HOST)  || 
				ManagerDB.PORT < 0  || 
				StringUtils.isBlank( ManagerDB.USER)  || 
				StringUtils.isBlank( ManagerDB.PASSWORD)  || 
				StringUtils.isBlank( ManagerDB.DATABASENAME)  || 
				ManagerDB.DATASTORE_PACKAGES == null || 
				ManagerDB.DATASTORE_PACKAGES.isEmpty())
			return false;
		
		boolean setted = false;
		try {

			if (client == null) {
				List<ServerAddress> servers = new ArrayList<ServerAddress>();
				servers.add(new ServerAddress(ManagerDB.HOST, ManagerDB.PORT));
				List<MongoCredential> credentials = new ArrayList<MongoCredential>();
				credentials.add(
						MongoCredential.createCredential(ManagerDB.USER, ManagerDB.DATABASENAME, ManagerDB.PASSWORD.toCharArray()));

				/*
				 * CodecRegistry codecRegistry =
				 * CodecRegistries.fromRegistries(CodecRegistries.fromProviders(
				 * ), MongoClient.getDefaultCodecRegistry());
				 */
				 MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			        //build the connection options  
				   //set the max wait time in (ms)
			        builder
			        .socketKeepAlive(true)
			        .maxConnectionIdleTime(60000)
			        .codecRegistry(MongoClient.getDefaultCodecRegistry());
				MongoClientOptions options = builder.build();
				client = new MongoClient(servers, credentials, options);
			}
			if (client != null && db == null) {
				db = client.getDatabase(ManagerDB.DATABASENAME);

				Morphia morphia = new Morphia();
				for(String pack : ManagerDB.DATASTORE_PACKAGES){
					/*System.out.println("pack : "+pack);
					try{
						Reflections reflections = new Reflections(pack);
						Set<Class<? extends MongoDoc>> allClasses = reflections.getSubTypesOf(MongoDoc.class);
						for(Class<? extends MongoDoc> c : allClasses)
							System.out.println("Class : "+c.getName());
					}catch(Exception e){
						e.printStackTrace(System.out);
					}*/
					
					
					morphia.mapPackage(pack); 
				}
						
						//.mapPackage(DBManager._DATASTORE_PACKAGE_EENTERPRISE)
						//.mapPackage(DBManager._DATASTORE_PACKAGE_EPRODUCTS)
					 
				morphia.getMapper().getOptions().setStoreNulls(true);
				morphia.getMapper().getOptions().setStoreEmpties(true);
				morphia.getMapper().getOptions().setIgnoreFinals(false);
				morphia.getMapper().getConverters().addConverter(BigIntegerConverter.class);
				ManagerDB.store = morphia.createDatastore(client, ManagerDB.DATABASENAME);
				ManagerDB.store.ensureIndexes();
				ManagerDB.store.ensureCaps();
				setted = true;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return setted;
	}

	 
	public static <T extends MongoDoc> Query<T> constructQuery(Class<T> type) {
		Query<T> query = ManagerDB.getDatastore().createQuery(type);
		return query;
	}
	private static boolean hasUsedGetPublicDatastoreAlready = false;
	protected static MongoDatabase getDB() {
		return db;
	}

	protected static MongoClient getClient() {
		return client;
	}

	protected static Datastore getDatastore() {
		return ManagerDB.store;
	}

	public static Datastore getPublicDatastore() {
		if (ManagerDB.hasUsedGetPublicDatastoreAlready)
			return null;
		ManagerDB.hasUsedGetPublicDatastoreAlready = true;
		return ManagerDB.getDatastore();
	}
}
