package com.snapdeal.scm.web.core.sro;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author Harsh, Vinay
 */
public class PointSerializer extends JsonSerializer<PointSRO> {


    @Override
    public void serialize(PointSRO pointSRO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(null != pointSRO){
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(pointSRO.getX());
            jsonGenerator.writeNumber(roundTwoDecimals(pointSRO.getY()));
            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeNull();
        }
    }

    private static double roundTwoDecimals(double d){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(d));
    }

}
