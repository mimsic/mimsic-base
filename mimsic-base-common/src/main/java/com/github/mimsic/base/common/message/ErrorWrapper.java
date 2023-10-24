package com.github.mimsic.base.common.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class ErrorWrapper<T> {

    @Builder.Default
    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);

    private int status;
    private String error;

    private T details;
    private String path;
}
