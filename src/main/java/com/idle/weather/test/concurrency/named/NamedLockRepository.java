package com.idle.weather.test.concurrency.named;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NamedLockRepository {

    private final JdbcTemplate jdbcTemplate;

    public void getLock(String key) {
        String sql = "SELECT GET_LOCK(?, 3000)"; // 쿼리에서 ?로 파라미터

        // 락 잠금 여부를 확인할 때 result 값을 사용
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

