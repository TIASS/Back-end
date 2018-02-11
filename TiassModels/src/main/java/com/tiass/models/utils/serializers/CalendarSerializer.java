package com.tiass.models.utils.serializers;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
 
 

public class CalendarSerializer extends JsonSerializer<Calendar> {
	@Override
	public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		
		long id = value.getTimeInMillis();
		
		jgen.writeNumber(id);
	}
}