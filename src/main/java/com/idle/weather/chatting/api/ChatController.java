package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ChatMessageResponse sendMessage(@RequestBody ChatMessageRequest chatMessageRequest, @UserId Long senderId) {
        return chatMessageService.sendMessage(chatMessageRequest, senderId);
    }

    @GetMapping("/{chatRoomId}/recent")
    public List<ChatMessageResponse> getRecentMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getRecentMessages(chatRoomId);
    }

    @GetMapping("/{chatRoomId}")
    public Page<ChatMessageResponse> getMessages(@PathVariable Long chatRoomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return chatMessageService.getMessages(chatRoomId, page, size);
    }
}
