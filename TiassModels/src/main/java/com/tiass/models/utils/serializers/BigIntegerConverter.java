package com.tiass.models.utils.serializers;

import java.math.BigInteger;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

public class BigIntegerConverter extends TypeConverter implements SimpleValueConverter {

	public BigIntegerConverter() {
		super(BigInteger.class);
	}
	//

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo) {
		 
		if (value == null) {
			return null;
		}
		String encoded = null;
		try {
			if (value instanceof BigInteger) {
				BigInteger val = BigInteger.class.cast(value);
				encoded = val.toString();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace(System.out);
		} catch (ClassCastException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return encoded;
	}

	@SuppressWarnings("rawtypes")  
	public Object decode(Class targetClass, Object fromDBObject, MappedField optionalExtraInfo) throws MappingException{
		try {
			 
			if (fromDBObject == null)
				return null;
		 
			BigInteger dec = new BigInteger(fromDBObject.toString());
			
			 
			return dec;
		} catch (NumberFormatException e) {
			e.printStackTrace(System.out);
		} catch (ClassCastException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}

 