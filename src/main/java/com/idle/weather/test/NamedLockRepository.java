package com.idle.weather.test;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NamedLockRepository {

    private final JdbcTemplate jdbcTemplate;

    public void getLock(String key) {
        String sql = "SELECT GET_LOCK(?, 3000)"; // 쿼리에서 ?로 파라미터
        Integer result = jdbcTemplate.queryForObject(sql, new Object[]{key}, Integer.class);
    }


    public void releaseLock(String key) {
        String sql = "SELECT RELEASE_LOCK(?)";

        Integer result = jdbcTemplate.queryForObject(
                sql,
                new Object[]{key},
                Integer.class
        );
    }
}

