package com.tiass.models.utils.serializers;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tiass.models.utils.StringUtilities;

public class ObjectIdListSerializer extends JsonSerializer<List<ObjectId>> {
	@Override
	public void serialize(List<ObjectId> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartArray();
		for (ObjectId id : value) {
			if (id == null)
				return;
			 
			 
			jgen.writeString( StringUtilities.convertKeyOut(id.toString()));
			 
		}
		jgen.writeEndArray();
	}
}