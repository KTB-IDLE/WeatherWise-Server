package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomMemberService;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/{chatRoomId}/members")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/join")
    public ResponseEntity<BaseResponse<Void>> joinChatRoom(
            @PathVariable Long chatRoomId, @UserId Long userId) {
        log.info("JOIN 요청");
        chatRoomMemberService.joinChatRoom(chatRoomId, userId);
        return ResponseEntity.ok(new BaseResponse<>(null));
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveChatRoom(
            @PathVariable Long chatRoomId, @UserId Long userId) {
        chatRoomMemberService.leaveChatRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Long>>> getChatRoomUsers(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(new BaseResponse<>(chatRoomMemberService.getChatRoomUsers(chatRoomId)));
    }
}
