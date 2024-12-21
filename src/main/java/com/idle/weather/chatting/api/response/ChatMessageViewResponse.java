package com.idle.weather.chatting.api.response;

import com.idle.weather.chatting.repository.ChatMessageEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ChatMessageViewResponse(
        @Schema(description = "메시지 ID", example = "1")
        Long id,

        @Schema(description = "채팅방 ID", example = "1")
        Long chatRoomId,

        @Schema(description = "수신자 ID", example = "1")
        Long senderId,

        @Schema(description = "메시지 내용", example = "비가 너무 많이오네요.. 고양이들 괜찮을까요?")
        String message,
        String nickname,

        @Schema(description = "메시지 발송 시간", example = "")
        LocalDateTime timestamp
) {
    public static ChatMessageViewResponse from(ChatMessageEntity chatMessage , String nickName) {
        return new ChatMessageViewResponse(
                chatMessage.getId(),
                chatMessage.getChatRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getMessage(),
                nickName,
                chatMessage.getTimestamp()
        );
    }
    
}
