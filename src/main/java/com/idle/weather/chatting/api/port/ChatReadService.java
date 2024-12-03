package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.response.ChatLastReadMessageResponse;
import com.idle.weather.chatting.api.response.ChatReadStatusResponse;

public interface ChatReadService {
    ChatReadStatusResponse markAsRead(Long chatRoomId, Long userId, Long chatMessageId);
    ChatLastReadMessageResponse getLastReadMessageId(Long chatRoomId, Long userId);
}
