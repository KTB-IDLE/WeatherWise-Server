package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.chatting.api.response.ChatMessageViewResponse;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/send/{chatRoomId}")
    public ResponseEntity<BaseResponse<ChatMessageViewResponse>> sendMessage(
            @PathVariable Long chatRoomId, @RequestBody ChatMessageRequest chatMessageRequest,
            @UserId Long senderId) {
        return ResponseEntity.ok(
                new BaseResponse<>(chatMessageService.sendMessage(chatRoomId, chatMessageRequest, senderId)));
    }

    @GetMapping("/{chatRoomId}/recent")
    public ResponseEntity<BaseResponse<List<ChatMessageViewResponse>>> getRecentMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(new BaseResponse<>(chatMessageService.getRecentMessages(chatRoomId)));
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<BaseResponse<Page<ChatMessageResponse>>> getMessages(
            @PathVariable Long chatRoomId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new BaseResponse<>(chatMessageService.getMessages(chatRoomId, page, size)));
    }
}
