package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.chatting.repository.ChatRoomEntity;

import java.util.List;

public interface ChatRoomService {
    ChatRoomEntity getOrCreateChatRoom(String parentRegionCode, String parentRegionName);
    void deleteChatRoomById(Long chatRoomId);
    void deleteOldDeactivatedChatRooms(int day);
    void saveChatRoom(ChatRoomEntity chatRoom);
    ChatRoomResponse getChatRoomById(Long chatRoomId);
    List<ChatRoomResponse> getAllChatRooms();
    List<ChatRoomResponse> getAllActivatedChatRooms();
}
