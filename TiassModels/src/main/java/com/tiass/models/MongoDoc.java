package com.tiass.models; 
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.bson.types.ObjectId;
  
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Validation;
import org.mongodb.morphia.annotations.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tiass.models.utils.serializers.ObjectIdDeserializer;
import com.tiass.models.utils.serializers.ObjectIdSerializer;

/**
 * Mother of All Document
 * 
 * @author JIMMY BAHOLE
 *
 */ 
  
@Validation("{"  
		+ "insertDate : { $exists: true, $ne: null }, "
		+ "modifyDate : { $exists: true, $ne: null }"
		+ " }")
public class MongoDoc {
	@XmlJavaTypeAdapter(value =com.tiass.models.utils.jaxbadapters.JAXBObjectIdAdapter.class, type = ObjectId.class)
	@JsonDeserialize(using = ObjectIdDeserializer.class)
	@JsonSerialize(using = ObjectIdSerializer.class)
	@Id
	@Property("_id")
	private ObjectId id;

	@JsonIgnore
	@Version
	@Property("version")
	private Long version;

	
	@XmlJavaTypeAdapter(value = com.tiass.models.utils.jaxbadapters.JAXBDateAdapter.class, type = Date.class)
	@JsonDeserialize(using = com.tiass.models.utils.serializers.DateDeserializer.class)
	@JsonSerialize(using = com.tiass.models.utils.serializers.DateSerializer.class)
	@Property("insertDate")
	private Date insertDate;

	@XmlJavaTypeAdapter(value = com.tiass.models.utils.jaxbadapters.JAXBDateAdapter.class, type = Date.class)
	@JsonDeserialize(using = com.tiass.models.utils.serializers.DateDeserializer.class)
	@JsonSerialize(using = com.tiass.models.utils.serializers.DateSerializer.class)
	@Property("modifyDate")
	private Date modifyDate;

	public MongoDoc() {
		super();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}