package com.idle.weather.chatting.service;

import com.idle.weather.chatting.api.port.ChatReadService;
import com.idle.weather.chatting.api.response.ChatLastReadMessageResponse;
import com.idle.weather.chatting.api.response.ChatReadStatusResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChatReadServiceImpl implements ChatReadService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String READ_STATUS_KEY_FORMAT = "chatroom:%d:read_status:user:%d";

    public ChatReadServiceImpl(@Qualifier("redisLockTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public ChatReadStatusResponse markAsRead(Long chatRoomId, Long userId, Long chatMessageId) {
        if (chatRoomId == null || userId == null || chatMessageId == null) {
            throw new IllegalArgumentException("ChatRoomId, UserId, and MessageId must not be null");
        }

        String key = String.format(READ_STATUS_KEY_FORMAT, chatRoomId, userId);
        redisTemplate.opsForValue().set(key, String.valueOf(chatMessageId));

        return new ChatReadStatusResponse(chatRoomId, userId, chatMessageId, LocalDateTime.now(), "Marked as read successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ChatLastReadMessageResponse getLastReadMessageId(Long chatRoomId, Long userId) {
        if (chatRoomId == null || userId == null) {
            throw new IllegalArgumentException("ChatRoomId and UserId must not be null");
        }

        String key = String.format("chatroom:%d:read_status:user:%d", chatRoomId, userId);
        String lastReadMessageId = redisTemplate.opsForValue().get(key);

        if (lastReadMessageId == null) {
            throw new IllegalArgumentException("No read status found for the given user in the chat room");
        }

        return new ChatLastReadMessageResponse(chatRoomId, userId, Long.parseLong(lastReadMessageId));
    }

}
