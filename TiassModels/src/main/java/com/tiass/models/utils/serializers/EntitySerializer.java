package com.tiass.models.utils.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tiass.models.MongoDoc;

public class EntitySerializer extends JsonSerializer<MongoDoc> {

	private JsonSerializer<Object> defaultSerializer;

	public EntitySerializer(JsonSerializer<Object> serializer) {
		defaultSerializer = serializer;
	}

	@Override
	public void serialize(MongoDoc value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		defaultSerializer.serialize(value, jgen, provider);
	}
}