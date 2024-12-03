package com.idle.weather.chatting.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatLastReadMessageResponse(
        @Schema(description = "채팅방 ID", example = "1")
        Long chatRoomId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "마지막으로 읽은 메시지 ID", example = "1")
        Long lastReadMessageId
) {
}
