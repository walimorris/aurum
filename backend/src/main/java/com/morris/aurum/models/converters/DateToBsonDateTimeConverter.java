package com.morris.aurum.models.converters;

import org.bson.BsonDateTime;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class DateToBsonDateTimeConverter implements Converter<Date, BsonDateTime> {

    @Override
    public BsonDateTime convert(Date source) {
        return new BsonDateTime(source.getTime());
    }
}
