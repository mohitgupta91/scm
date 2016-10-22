package com.snapdeal.scm.alerts.utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


import com.snapdeal.scm.mongo.doc.NotificationActivity;
import com.snapdeal.scm.utils.DateUtils;

/**
 * 
 * @author mohit
 *
 */
public class NotificationActivitySerializer extends JsonSerializer<NotificationActivity>{

	@Override
	public void serialize(NotificationActivity value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,JsonProcessingException {
		if(value !=null){
			gen.writeStartObject();
			gen.writeStringField("status", value.getStatus().name());
			gen.writeStringField("comment", value.getComment());
			gen.writeStringField("updatedBy", value.getUpdatedBy());
			gen.writeStringField("updatedOn", DateUtils.convertDateToString(value.getUpdatedOn()));
			gen.writeEndObject();
		}
		else
			gen.writeNull();
	}

}
