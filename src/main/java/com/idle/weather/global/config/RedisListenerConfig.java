package com.idle.weather.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter, ChannelTopic channelTopic) {
        log.info("Initializing RedisMessageListenerContainer");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, channelTopic);

        container.setSubscriptionExecutor(Executors.newFixedThreadPool(4));
        container.setTaskExecutor(Executors.newFixedThreadPool(4));

        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(ObjectMapper objectMapper) {
        return new MessageListenerAdapter(new RedisMessageSubscriber(objectMapper), "onMessage");
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom:notifications");
    }
}
