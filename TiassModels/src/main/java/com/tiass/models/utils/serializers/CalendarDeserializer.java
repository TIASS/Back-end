package com.tiass.models.utils.serializers;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
 

public class CalendarDeserializer extends JsonDeserializer<Calendar> {
	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		Calendar real_id = null;
		if (node.isNumber()) {
			real_id = Calendar.getInstance();
			real_id.setTimeInMillis(node.longValue());
		}
		return real_id;
	}
}