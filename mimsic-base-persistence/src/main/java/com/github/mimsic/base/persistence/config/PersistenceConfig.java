package com.github.mimsic.base.persistence.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "persistence-config")
@Getter
@Setter
@NoArgsConstructor
public class PersistenceConfig {

    public static final String DEFAULT_SCHEMA = "public";
    public static final String DEFAULT_SCHEMA_KEY = "default";

    private Map<String, Object> persistenceProperties;
    private Map<String, DataSourceUnit> targetDataSources;

    public DataSourceUnit defaultTargetDataSourceUnit() throws IllegalArgumentException {
        return Optional.ofNullable(targetDataSources.get(DEFAULT_SCHEMA_KEY))
                .orElseThrow(() -> new IllegalArgumentException("no default target data source"));
    }

    public Set<DataSourceUnit> targetDataSourceUnits() {
        return Collections.unmodifiableSet(targetDataSources.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(DEFAULT_SCHEMA_KEY))
                .map(entry -> entry.getValue()).collect(Collectors.toSet()));
    }

    public Set<String> schemas() {
        return Collections.unmodifiableSet(targetDataSources.entrySet().stream()
                .filter(entry -> entry.getValue().getSchema() != null)
                .map(entry -> entry.getValue().getSchema()).collect(Collectors.toSet()));
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Persistence Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append(persistenceProperties).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append(targetDataSources).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataSourceUnit {

        private String url;
        private String driverClassName;
        private String userName;
        private String password;
        private String schema;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("url: ").append(url).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("driverClassName: ").append(driverClassName).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("userName: ").append(userName).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("password: ").append(password).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("schema: ").append(schema).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 3));
            return stringBuilder.toString();
        }
    }
}
