package com.idle.weather.chatting.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ChatReadStatusResponse(
        @Schema(description = "채팅방 ID", example = "1")
        Long chatRoomId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "메시지 ID", example = "1")
        Long chatMessageId,

        @Schema(description = "시간", example = "")
        LocalDateTime timestamp,

        @Schema(description = "작업 결과에 대한 상태 메시지", example = "Marked as read successfully")
        String message
) {
}
