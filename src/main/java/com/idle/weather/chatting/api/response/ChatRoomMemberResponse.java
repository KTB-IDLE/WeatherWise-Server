package com.idle.weather.chatting.api.response;

import com.idle.weather.chatting.repository.ChatRoomMemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoomMemberResponse(
        @Schema(description = "채팅방-사용자 관계 ID", example = "1")
        Long id,

        @Schema(description = "채팅방 ID", example = "1")
        Long chatRoomId,

        @Schema(description = "채팅방 이름", example = "서울 폭우 경보 채팅방")
        String chatRoomName,

        @Schema(description = "사용자 ID", example = "1")
        Long userId
) {
    public static ChatRoomMemberResponse from(ChatRoomMemberEntity chatRoomMember) {
        return new ChatRoomMemberResponse(
                chatRoomMember.getId(),
                chatRoomMember.getChatRoom().getId(),
                chatRoomMember.getChatRoom().getName(),
                chatRoomMember.getUserId()
        );
    }
}
