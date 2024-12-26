package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomService;
import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.global.BaseResponse;
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

    @GetMapping("/activated")
    public ResponseEntity<BaseResponse<List<ChatRoomResponse>>> getAllActivatedChatRooms() {
        return ResponseEntity.ok(new BaseResponse<>(chatRoomService.getAllActivatedChatRooms()));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ChatRoomEntity>> createChatRoom(
            @RequestParam String parentRegionCode,
            @RequestParam String parentRegionName) {
        ChatRoomEntity response = chatRoomService.getOrCreateChatRoom(parentRegionCode, parentRegionName);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoomById(chatRoomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<BaseResponse<ChatRoomResponse>> getChatRoom(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(new BaseResponse<>(chatRoomService.getChatRoomById(chatRoomId)));
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<ChatRoomResponse>>> getAllChatRooms() {
        return ResponseEntity.ok(new BaseResponse<>(chatRoomService.getAllChatRooms()));
    }

    @DeleteMapping("/delete-old")
    public ResponseEntity<Void> deleteOldDeactivatedChatRooms(@RequestParam(defaultValue = "7") int day) {
        chatRoomService.deleteOldDeactivatedChatRooms(day);
        return ResponseEntity.noContent().build();
    }
}
