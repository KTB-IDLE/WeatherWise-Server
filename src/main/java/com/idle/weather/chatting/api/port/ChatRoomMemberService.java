package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.response.ChatRoomMemberResponse;

import java.util.List;

public interface ChatRoomMemberService {
    ChatRoomMemberResponse joinChatRoom(Long chatRoomId, Long userId);
    void leaveChatRoom(Long chatRoomId, Long userId);
    List<Long> getChatRoomUsers(Long chatRoomId);
}
