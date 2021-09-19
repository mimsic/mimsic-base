package com.github.mimsic.base.common.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mimsic.base.common.json.serializer.IsoTimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErrorWrapper<T> {

    @JsonSerialize(using = IsoTimestampSerializer.class)
    private ZonedDateTime timestamp;

    private int status;
    private String error;

    private T details;
    private String path;

    public ErrorWrapper(int status, String error, T details) {

        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        this.status = status;
        this.error = error;
        this.details = details;
    }

    public ErrorWrapper(int status, String error, T details, String path) {

        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        this.status = status;
        this.error = error;
        this.details = details;
        this.path = path;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        private int status;
        private String error;
        private T details;
        private String path;

        Builder() {
        }

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> error(String error) {
            this.error = error;
            return this;
        }

        public Builder<T> details(T details) {
            this.details = details;
            return this;
        }

        public Builder<T> path(String path) {
            this.path = path;
            return this;
        }

        public ErrorWrapper<T> build() {
            return new ErrorWrapper<>(timestamp, status, error, details, path);
        }
    }
}
