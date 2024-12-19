package com.idle.weather.websocket.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record WebSocketChatMessageRequest(
        @Schema(description = "채팅방 ID", example = "123")
        Long chatRoomId,

        @Schema(description = "보내는 메시지 내용", example = "비가 너무 많이오네요.. 고양이들 괜찮을까요?")
        String message
) {
}
