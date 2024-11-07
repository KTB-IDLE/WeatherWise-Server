package com.idle.weather.global;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisInitializer(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @PostConstruct
    public void clearRedisOnStartup() {
        redisConnectionFactory.getConnection().flushDb(); // Redis DB 전체 초기화
    }
}
