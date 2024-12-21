package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.chatting.api.response.ChatMessageViewResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatMessageService {
    ChatMessageViewResponse sendMessage(Long chatRoomId, ChatMessageRequest chatMessageRequest, Long senderId);
    List<ChatMessageViewResponse> getRecentMessages(Long chatRoomId);
    Page<ChatMessageResponse> getMessages(Long chatRoomId, int page, int size);
}
