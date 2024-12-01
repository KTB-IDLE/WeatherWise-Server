package com.idle.weather.chatting.service;

import com.idle.weather.chatting.api.port.ChatRoomService;
import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.chatting.repository.ChatRoomEntity;
import com.idle.weather.chatting.repository.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    @Transactional
    public ChatRoomEntity getOrCreateChatRoom(String parentRegionCode, String parentRegionName){
        return chatRoomJpaRepository.findByParentRegionCode(parentRegionCode)
                .orElseGet(() -> {
                    ChatRoomEntity newChatRoom = ChatRoomEntity.createChatRoom(parentRegionCode, parentRegionName);
                    return chatRoomJpaRepository.save(newChatRoom);
                });
    }

    @Override
    @Transactional
    public void deleteChatRoomById(Long chatRoomId) {
        chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with chatRoomId" + chatRoomId));
        chatRoomJpaRepository.deleteById(chatRoomId); // hardDelete
    }

    @Override
    @Transactional
    public void saveChatRoom(ChatRoomEntity chatRoom) {
        chatRoomJpaRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public void deleteOldDeactivatedChatRooms(int day) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(day);
        List<ChatRoomEntity> oldChatRooms = chatRoomJpaRepository.findAllDeactivatedOlderThan(cutoffDate);

        for (ChatRoomEntity oldChatRoom : oldChatRooms) {
            chatRoomJpaRepository.delete(oldChatRoom);
            log.info("오래된 비활성화 채팅방 삭제: {}", oldChatRoom.getName());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with chatRoomId : " + chatRoomId));
        return ChatRoomResponse.from(chatRoomEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getAllChatRooms() {
        return chatRoomJpaRepository.findAll().stream()
                .map(ChatRoomResponse::from)
                .toList();
    }

    @Override
    public List<ChatRoomResponse> getAllActivatedChatRooms() {
        return chatRoomJpaRepository.findAllActivatedChatRooms().stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
}
