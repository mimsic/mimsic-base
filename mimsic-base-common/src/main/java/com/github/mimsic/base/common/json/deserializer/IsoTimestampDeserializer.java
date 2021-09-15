package com.github.mimsic.base.common.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class IsoTimestampDeserializer extends StdDeserializer<ZonedDateTime> {

    public IsoTimestampDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        return ZonedDateTime.parse(parser.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
