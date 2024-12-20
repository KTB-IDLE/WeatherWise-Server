package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomService;
import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.chatting.repository.ChatRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 활성화된 모든 채팅방 조회
    @GetMapping("/activated")
    public ResponseEntity<List<ChatRoomResponse>> getAllActivatedChatRooms() {
        List<ChatRoomResponse> chatRooms = chatRoomService.getAllActivatedChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomEntity> createChatRoom(@RequestBody String parentRegionCode, @RequestBody String parentRegionName) {
        return ResponseEntity.ok(chatRoomService.getOrCreateChatRoom(parentRegionCode, parentRegionName));
    }

    // 특정 채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoomById(chatRoomId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(chatRoomId));
    }

    // 모든 채팅방 조회
    @GetMapping("/all")
    public ResponseEntity<List<ChatRoomResponse>> getAllChatRooms() {
        List<ChatRoomResponse> chatRooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    // 오래된 비활성화된 채팅  삭제 수동 트리거 (테스트용)
    @DeleteMapping("/delete-old")
    public ResponseEntity<Void> deleteOldDeactivatedChatRooms(@RequestParam(defaultValue = "7") int day) {
        chatRoomService.deleteOldDeactivatedChatRooms(day);
        return ResponseEntity.noContent().build();
    }

}
