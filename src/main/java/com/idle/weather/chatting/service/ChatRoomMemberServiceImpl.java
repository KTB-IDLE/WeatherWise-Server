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

@Service
@RequiredArgsConstructor
public class ChatRoomMemberServiceImpl implements ChatRoomMemberService {

    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoomMemberResponse joinChatRoom(Long chatRoomId, Long userId) {
        ChatRoomEntity chatRoom = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
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
