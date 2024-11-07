package com.idle.weather.test.concurrency.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

// @Configuration
public class JdbcDataSourceConfig {
    @Bean(name = "jdbcDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jdbc")
    public DataSource jdbcDataSource() {
        return DataSourceBuilder.create().build();
    }
}
