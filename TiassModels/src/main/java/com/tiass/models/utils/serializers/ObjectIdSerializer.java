package com.tiass.models.utils.serializers;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
//import org.codehaus.jackson.JsonGenerator;
//import org.codehaus.jackson.JsonProcessingException;
//import org.codehaus.jackson.map.JsonSerializer;
//import org.codehaus.jackson.map.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tiass.models.utils.StringUtilities;

public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
	/*@Override
	public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		String id = StringUtilities.convertKeyOut(value.toString());
		System.out.println("object serializer id : "+id);
		jgen.writeString(id);
	}*/

	@Override
	public void serialize(ObjectId arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {

		String id = StringUtilities.convertKeyOut(arg0.toString());
		System.out.println("object serializer id : "+id);
		arg1.writeString(id);
		
	}
}