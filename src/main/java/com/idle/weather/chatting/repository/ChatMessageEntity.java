package com.idle.weather.chatting.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 보낸 사람

    @Column(name = "message", nullable = false)
    private String message; // 메시지 내용

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; // 전송시간

    @Builder
    private ChatMessageEntity(Long chatRoomId, Long senderId, String message, LocalDateTime timestamp) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    @JsonCreator
    public static ChatMessageEntity jsonCreator(
            @JsonProperty("chatRoomId") Long chatRoomId,
            @JsonProperty("sendId") Long senderId,
            @JsonProperty("message") String message,
            @JsonProperty("timestamp") LocalDateTime timestamp
            ) {
        return new ChatMessageEntity(chatRoomId, senderId, message, timestamp);
    }

    public static ChatMessageEntity createChatMessage(Long chatRoomId, Long senderId, String message) {
        return ChatMessageEntity.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public String serialize() {
        long epochSecond = timestamp.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();
        return String.format("%d|%d|%s|%s",
                chatRoomId,
                senderId,
                message,
                epochSecond);
    }

    public static ChatMessageEntity deserialize(String data, ChatRoomEntity chatRoom) {
        try {
            String[] parts = data.split("\\|");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid serialized data format");
            }

            Long chatRoomId = Long.parseLong(parts[0]);
            Long senderId = Long.parseLong(parts[1]);
            String message = parts[2];
            long epochSecond = Long.parseLong(parts[3]);

            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.of("Asia/Seoul"));

            return new ChatMessageEntity(chatRoomId, senderId, message, timestamp);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to deserialize chat message", e);
        }
    }
}
