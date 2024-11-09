package com.idle.weather.test.concurrency.redis.lettuce;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String , String> redisLockTemplate;
    public Boolean lock(Long key) {
        return redisLockTemplate.opsForValue().setIfAbsent(generateKey(key) , "lock" , Duration.ofMillis(3_000));
    }

    public Boolean unlock(Long key) {
        return redisLockTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
        // return "LOCK:"+key.toString();
    }
}
