package com.idle.weather.chatting.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoomRequest(
        @Schema(description = "채팅방 이름", example = "서울 지진 경보 채팅방")
        String name
) {
}
