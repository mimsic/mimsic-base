package com.github.mimsic.base.persistence.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;
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

    private Map<String, DataSourceUnit> dataSourceUnits;
    private Map<String, Object> persistenceProperties;

    public DataSourceUnit defaultDataSourceUnit() throws IllegalArgumentException {
        return Optional.ofNullable(dataSourceUnits.get(DEFAULT_SCHEMA_KEY))
                .orElseThrow(() -> new IllegalArgumentException("no default target data source"));
    }

    public Set<DataSourceUnit> dataSourceUnits() {
        return dataSourceUnits.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(DEFAULT_SCHEMA_KEY))
                .map(Map.Entry::getValue).collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> schemas() {
        return dataSourceUnits.values().stream()
                .map(DataSourceUnit::getSchema)
                .filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Persistence Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3));
        stringBuilder.append("Persistence Units: {").append('\n');
        dataSourceUnits.forEach((String key, DataSourceUnit unit) -> {
            stringBuilder.append(StringUtils.leftPad("", 6));
            stringBuilder.append(key).append(": ").append('\n');
            stringBuilder.append(unit);
        });
        stringBuilder.append(StringUtils.leftPad("", 3));
        stringBuilder.append('}').append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3));
        stringBuilder.append("persistence Properties: {").append('\n');
        persistenceProperties.forEach((String key, Object object) -> {
            stringBuilder.append(StringUtils.leftPad("", 6));
            stringBuilder.append(key).append(": ").append(object);
            stringBuilder.append('\n');
        });
        stringBuilder.append(StringUtils.leftPad("", 3));
        stringBuilder.append('}').append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataSourceUnit {

        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String schema;
        private int maximumPoolSize;
        private int minimumIdle;
        private boolean createSchema;
        private boolean createSequence;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("driverClassName: ").append(driverClassName).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("url: ").append(url).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("userName: ").append(username).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("password: ").append(password).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("schema: ").append(schema).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("createSchema: ").append(createSchema).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9));
            stringBuilder.append("createSequence: ").append(createSequence).append('\n');
            return stringBuilder.toString();
        }
    }
}
