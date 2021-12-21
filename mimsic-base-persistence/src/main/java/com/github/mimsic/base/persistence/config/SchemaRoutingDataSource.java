package com.github.mimsic.base.persistence.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class SchemaRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return SchemaRoutingContextHolder.getSchema();
    }
}
