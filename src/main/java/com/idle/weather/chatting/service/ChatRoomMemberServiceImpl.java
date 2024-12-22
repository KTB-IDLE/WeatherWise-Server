package com.idle.weather.chatting.service;

import com.idle.weather.chatting.api.port.ChatRoomMemberService;
import com.idle.weather.chatting.api.response.ChatRoomMemberResponse;
import com.idle.weather.chatting.repository.ChatRoomEntity;
import com.idle.weather.chatting.repository.ChatRoomJpaRepository;
import com.idle.weather.chatting.repository.ChatRoomMemberEntity;
import com.idle.weather.chatting.repository.ChatRoomMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberServiceImpl implements ChatRoomMemberService {

    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoomMemberResponse joinChatRoom(Long chatRoomId, Long userId) {
        ChatRoomEntity chatRoom = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
        // 이미 존재하는 ChatRoomMemberEntity 확인
        Optional<ChatRoomMemberEntity> existingMember = chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(chatRoomId, userId);
        // 이미 입장 처리된 사용자인지 확인
        if (existingMember.isPresent()) {
            // 이미 입장한 사용자일 경우 반환
            return ChatRoomMemberResponse.from(existingMember.get());
        }
        // 새로 입장 처리
        ChatRoomMemberEntity chatRoomMember = ChatRoomMemberEntity.createChatRoomMember(chatRoom, userId);
        return ChatRoomMemberResponse.from(chatRoomMemberJpaRepository.save(chatRoomMember));
    }

    @Override
    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        if (!chatRoomMemberJpaRepository.existsByChatRoomIdAndUserId(chatRoomId, userId)) {
            throw new IllegalArgumentException("Matching with chatRoomId : " + chatRoomId + " and userId : " + userId + " does not exist");
        }
        chatRoomMemberJpaRepository.deleteByChatRoomIdAndUserId(chatRoomId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getChatRoomUsers(Long chatRoomId) {
        return chatRoomMemberJpaRepository.findUserIdsByChatRoomId(chatRoomId);
    }
}
