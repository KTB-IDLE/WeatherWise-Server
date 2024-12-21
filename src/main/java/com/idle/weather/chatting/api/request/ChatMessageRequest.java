package com.idle.weather.chatting.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatMessageRequest(
        @Schema(description = "메시지 내용", example = "비가 너무 많이오네요.. 고양이들 괜찮을까요?")
        String message,
        Long chatRoomId,
        String nickname
) {
}
