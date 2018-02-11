package com.tiass.models.utils.serializers;

import java.io.IOException;

import org.bson.types.ObjectId;
 
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tiass.models.utils.StringUtilities;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
	 

	@Override
	public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		String ID = StringUtilities.convertKeyIn(node.textValue());
		System.out.println("object deserialize ID : " + ID);
		ObjectId real_id = null;
		if (ObjectId.isValid(ID)) {
			real_id = new ObjectId(ID);
		}
		System.out.println("object deserialize real_id : " + real_id);
		return real_id;
		 
	}
}