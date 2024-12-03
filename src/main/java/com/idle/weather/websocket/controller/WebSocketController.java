package com.idle.weather.websocket.controller;

import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    public void sendMessage(ChatMessageRequest chatMessageRequest, @UserId Long senderId) {
        ChatMessageResponse chatMessageResponse = chatMessageService.sendMessage(chatMessageRequest, senderId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatMessageRequest.chatRoomId(), chatMessageResponse);
    }

}
