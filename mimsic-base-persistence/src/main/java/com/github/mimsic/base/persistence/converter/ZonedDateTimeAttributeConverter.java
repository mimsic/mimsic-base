package com.github.mimsic.base.persistence.converter;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : Timestamp.from(zonedDateTime.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
    }
}
