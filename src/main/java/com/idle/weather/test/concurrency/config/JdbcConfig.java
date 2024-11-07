package com.idle.weather.test.concurrency.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

// @Configuration
public class JdbcConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("jdbcDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
