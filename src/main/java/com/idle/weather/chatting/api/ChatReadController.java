package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatReadService;
import com.idle.weather.chatting.api.response.ChatLastReadMessageResponse;
import com.idle.weather.chatting.api.response.ChatReadStatusResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/read")
public class ChatReadController {

    private final ChatReadService chatReadService;

    @PostMapping("/{chatRoomId}/message/{chatMessageId}")
    public ChatReadStatusResponse markAsRead(
            @PathVariable Long chatRoomId,
            @UserId Long userId,
            @PathVariable Long chatMessageId) {
        return chatReadService.markAsRead(chatRoomId, userId, chatMessageId);
    }

    @GetMapping("/{chatRoomId}/last-read")
    public ChatLastReadMessageResponse getLastReadMessage(@PathVariable Long chatRoomId, @UserId Long userId) {
        return chatReadService.getLastReadMessageId(chatRoomId, userId);
    }
}
