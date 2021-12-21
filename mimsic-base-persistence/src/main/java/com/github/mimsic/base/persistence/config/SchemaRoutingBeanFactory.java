package com.github.mimsic.base.persistence.config;

import com.github.mimsic.base.common.utility.StringDelimiter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SchemaRoutingBeanFactory {

    private final PersistenceConfig persistenceConfig;
    private final String[] entityPackages;

    public SchemaRoutingBeanFactory(PersistenceConfig persistenceConfig, String[] entityPackages) {

        if (persistenceConfig == null) throw new IllegalArgumentException("persistenceConfig is null");
        if (entityPackages == null) throw new IllegalArgumentException("entityPackages is null");
        this.persistenceConfig = persistenceConfig;
        this.entityPackages = entityPackages;
    }

    public DataSource schemaRoutingDataSource() {

        SchemaRoutingDataSource schemaRoutingDataSource = new SchemaRoutingDataSource();
        schemaRoutingDataSource.setDefaultTargetDataSource(defaultDataSource());
        schemaRoutingDataSource.setTargetDataSources(targetDataSources());
        return schemaRoutingDataSource;
    }

    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource, String persistenceUnitName) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(entityPackages);
        entityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaPropertyMap(persistenceConfig.getPersistenceProperties());
        return entityManagerFactoryBean;
    }

    private DataSource defaultDataSource() {
        DataSource dataSource = dataSource(persistenceConfig.defaultTargetDataSourceUnit());
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            createSequence(statement);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return dataSource;
    }

    private Map<Object, Object> targetDataSources() {

        Map<Object, Object> targetDataSources = new HashMap<>();

        persistenceConfig.targetDataSourceUnits().forEach(targetDataSourceUnit -> {
            DataSource dataSource = dataSource(targetDataSourceUnit);
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                createSchema(statement, targetDataSourceUnit.getSchema(), targetDataSourceUnit.getUserName());
                createSequence(statement);
                String persistenceUnitName = String.format("%s-PersistenceUnit", targetDataSourceUnit.getSchema());
                entityManagerFactoryBean(dataSource, persistenceUnitName).afterPropertiesSet();
                targetDataSources.put(targetDataSourceUnit.getSchema(), dataSource);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return targetDataSources;
    }

    private DriverManagerDataSource dataSource(PersistenceConfig.DataSourceUnit dataSourceUnit) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceUnit.getDriverClassName());
        dataSource.setUrl(dataSourceUnit.getUrl());
        dataSource.setUsername(dataSourceUnit.getUserName());
        dataSource.setPassword(dataSourceUnit.getPassword());
        dataSource.setSchema(dataSourceUnit.getSchema());
        return dataSource;
    }

    private void createSchema(Statement statement, String schema, String authorization) throws SQLException {

        statement.execute(new StringBuilder()
                .append("CREATE SCHEMA IF NOT EXISTS").append(StringDelimiter.SPACE)
                .append(schema).append(StringDelimiter.SPACE)
                .append("AUTHORIZATION").append(StringDelimiter.SPACE)
                .append(authorization).toString());
    }

    private void createSequence(Statement statement) throws SQLException {

        statement.execute(new StringBuilder()
                .append("CREATE SEQUENCE IF NOT EXISTS").append(StringDelimiter.SPACE)
                .append("hibernate_sequence").append(StringDelimiter.SPACE)
                .append("INCREMENT").append(StringDelimiter.SPACE)
                .append(1L).append(StringDelimiter.SPACE)
                .append("MINVALUE").append(StringDelimiter.SPACE)
                .append(1L).append(StringDelimiter.SPACE)
                .append("MAXVALUE").append(StringDelimiter.SPACE)
                .append(9223372036854775807L).append(StringDelimiter.SPACE)
                .append("START").append(StringDelimiter.SPACE)
                .append(1L).append(StringDelimiter.SPACE)
                .append("CACHE").append(StringDelimiter.SPACE)
                .append(1L).toString());
    }
}
