package com.idle.weather.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.chatting.repository.ChatMessageEntity;
import com.idle.weather.chatting.repository.ChatMessageJpaRepository;
import com.idle.weather.chatting.repository.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final String MESSAGE_CACHE_PREFIX = "chatroom:";
    private static final int MAX_CACHE_SIZE = 100;

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final RedissonClient redissonClient;

    private final ObjectMapper objectMapper;

    private static final String LATEST_MESSAGES_KEY_FORMAT = "chatroom:%d:latest";

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest chatMessageRequest, Long senderId) {
        chatRoomJpaRepository.findById(chatMessageRequest.chatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found" + chatMessageRequest.chatRoomId()));

        ChatMessageEntity chatMessage = ChatMessageEntity.createChatMessage(
                chatMessageRequest.chatRoomId(),
                senderId,
                chatMessageRequest.message()
        );

        ChatMessageEntity savedMessage = chatMessageJpaRepository.save(chatMessage); // DB 처리

        cacheMessageInRedis(chatMessageRequest.chatRoomId(), savedMessage); // Redis 처리

        return ChatMessageResponse.from(savedMessage);
    }

    private void cacheMessageInRedis(Long chatRoomId, ChatMessageEntity chatMessage) {
        String cacheKey = MESSAGE_CACHE_PREFIX + chatRoomId;
        RScoredSortedSet<String> redisMessages = redissonClient.getScoredSortedSet(cacheKey);

        long epochSecondKST = chatMessage.getTimestamp().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();

        try {
            String serializedMessage = objectMapper.writeValueAsString(chatMessage);
            redisMessages.add(epochSecondKST, serializedMessage);

            if (redisMessages.size() > MAX_CACHE_SIZE) {
                redisMessages.removeRangeByRank(0, 0);
            }

            redisMessages.expire(Duration.ofDays(7));
        } catch (Exception e) {
            throw new RuntimeException("Failed to cache message in Redis", e);
        }
    }

    @Override
    public List<ChatMessageResponse> getRecentMessages(Long chatRoomId) {
        chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with chatRoomId : " + chatRoomId));
        String cacheKey = MESSAGE_CACHE_PREFIX + chatRoomId;
        RScoredSortedSet<String> redisMessages = redissonClient.getScoredSortedSet(cacheKey);

        return redisMessages.readAll().stream()
                .map(data -> {
                    try {
                        return objectMapper.readValue(data, ChatMessageEntity.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Redis에서 메시지 역직렬화 실패", e);
                    }
                })
                .map(ChatMessageResponse::from)
                .toList();
    }

    @Override
    public Page<ChatMessageResponse> getMessages(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return chatMessageJpaRepository.findByChatRoomId(chatRoomId, pageable);
    }
}
