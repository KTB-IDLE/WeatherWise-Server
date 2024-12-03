package com.idle.weather.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String body = new String(message.getBody());
            log.info("Received message from channel {}: {}", channel, body);
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}
