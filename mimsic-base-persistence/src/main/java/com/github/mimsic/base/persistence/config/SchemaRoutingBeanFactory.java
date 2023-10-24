package com.github.mimsic.base.persistence.config;

import com.github.mimsic.base.common.utility.StringDelimiter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
        schemaRoutingDataSource.setDefaultTargetDataSource(defaultTargetDataSource());
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

    private DataSource defaultTargetDataSource() {
        PersistenceConfig.DataSourceUnit dataSourceUnit = persistenceConfig.defaultDataSourceUnit();
        DataSource dataSource = dataSource(dataSourceUnit);
        if (dataSourceUnit.isCreateSequence()) {
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                createSequence(statement);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return dataSource;
    }

    private Map<Object, Object> targetDataSources() {

        Map<Object, Object> dataSources = new HashMap<>();
        persistenceConfig.dataSourceUnits().forEach(dataSourceUnit -> {
            DataSource dataSource = dataSource(dataSourceUnit);
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                if (dataSourceUnit.isCreateSchema()) {
                    createSchema(statement, dataSourceUnit.getSchema(), dataSourceUnit.getUsername());
                }
                if (dataSourceUnit.isCreateSequence()) {
                    createSequence(statement);
                }
                String persistenceUnitName = String.format("%s-PersistenceUnit", dataSourceUnit.getSchema());
                entityManagerFactoryBean(dataSource, persistenceUnitName).afterPropertiesSet();
                dataSources.put(dataSourceUnit.getSchema(), dataSource);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return dataSources;
    }

    /**
     * @param dataSourceUnit
     * @return
     * @see <a href="https://github.com/brettwooldridge/HikariCP">HikariCP Documentation</a>
     */
    private HikariDataSource dataSource(PersistenceConfig.DataSourceUnit dataSourceUnit) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceUnit.getDriverClassName());
        hikariConfig.setJdbcUrl(dataSourceUnit.getUrl());
        hikariConfig.setUsername(dataSourceUnit.getUsername());
        hikariConfig.setPassword(dataSourceUnit.getPassword());
        hikariConfig.setSchema(dataSourceUnit.getSchema());
        hikariConfig.setMaximumPoolSize(dataSourceUnit.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(dataSourceUnit.getMinimumIdle());
        return new HikariDataSource(hikariConfig);
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
