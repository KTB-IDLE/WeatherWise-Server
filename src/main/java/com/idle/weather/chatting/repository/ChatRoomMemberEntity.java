package com.idle.weather.chatting.repository;

import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_room_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChatRoomMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public static ChatRoomMemberEntity createChatRoomMember(ChatRoomEntity chatRoom, Long userId) {
        return ChatRoomMemberEntity.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .build();
    }

}
