package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomMemberService;
import com.idle.weather.chatting.api.response.ChatRoomMemberResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/{chatRoomId}/members")
@RequiredArgsConstructor
public class ChatRoomMemberController {
    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/join")
    public ChatRoomMemberResponse joinChatRoom(@PathVariable Long chatRoomId, @UserId Long userId) {
        return chatRoomMemberService.joinChatRoom(chatRoomId, userId);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long chatRoomId, @UserId Long userId) {
        chatRoomMemberService.leaveChatRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Long> getChatRoomUsers(@PathVariable Long chatRoomId) {
        return chatRoomMemberService.getChatRoomUsers(chatRoomId);
    }
}
