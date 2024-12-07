package com.idle.weather.websocket.controller;

import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageRequest chatMessageRequest, @UserId Long senderId) {
        ChatMessageResponse chatMessageResponse = chatMessageService.sendMessage(chatRoomId, chatMessageRequest, senderId);
        log.info("Sunny : WebSocketController에서 받은 USER_ID: " + senderId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, chatMessageResponse);
    }

}
