package com.tiass.models.utils.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tiass.models.utils.StringUtilities;

public class ObjectIdListDeserializer extends JsonDeserializer<List<ObjectId>> {
	@Override
	public List<ObjectId> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		List<ObjectId> real_ids = new ArrayList<ObjectId>();

		Iterator<JsonNode> IDs = node.elements() ;
		while(IDs.hasNext()){
			String ID = StringUtilities.convertKeyIn(IDs.next().textValue());
			if (ObjectId.isValid(ID)) { 
				real_ids.add(new ObjectId(ID));
			} 
		} 
		return real_ids;
	}
}