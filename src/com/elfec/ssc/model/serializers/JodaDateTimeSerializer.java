package com.elfec.ssc.model.serializers;

import org.joda.time.DateTime;

import com.activeandroid.serializer.TypeSerializer;

public class JodaDateTimeSerializer extends TypeSerializer  {

	@Override
	public Object deserialize(Object data) {
		if (data == null) {
			return null;
		}

		return new DateTime((Long) data);
	}

	@Override
	public Class<?> getDeserializedType() {
		return DateTime.class;
	}

	@Override
	public Class<?> getSerializedType() {
		return long.class;
	}

	@Override
	public Object serialize(Object data) {
		if (data == null) {
			return null;
		}

		return ((DateTime) data).toDate().getTime();
	}

}
