package com.github.mimsic.base.persistence.config;

import java.util.Optional;

public class SchemaRoutingContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(String schema) {
        CONTEXT.set(Optional.ofNullable(schema).orElse(PersistenceConfig.DEFAULT_SCHEMA));
    }

    public static String getSchema() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
