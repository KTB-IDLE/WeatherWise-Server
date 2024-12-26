package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatReadService;
import com.idle.weather.chatting.api.response.ChatLastReadMessageResponse;
import com.idle.weather.chatting.api.response.ChatReadStatusResponse;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/read")
public class ChatReadController {

    private final ChatReadService chatReadService;

    @PostMapping("/{chatRoomId}/message/{chatMessageId}")
    public ResponseEntity<BaseResponse<ChatReadStatusResponse>> markAsRead(
            @PathVariable Long chatRoomId,
            @UserId Long userId,
            @PathVariable Long chatMessageId) {
        return ResponseEntity.ok(new BaseResponse<>(chatReadService.markAsRead(chatRoomId, userId, chatMessageId)));
    }

    @GetMapping("/{chatRoomId}/last-read")
    public ResponseEntity<BaseResponse<ChatLastReadMessageResponse>> getLastReadMessage(
            @PathVariable Long chatRoomId, @UserId Long userId) {
        return ResponseEntity.ok(new BaseResponse<>(chatReadService.getLastReadMessageId(chatRoomId, userId)));
    }
}
