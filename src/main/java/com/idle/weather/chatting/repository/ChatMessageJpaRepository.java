package com.idle.weather.chatting.repository;

import com.idle.weather.chatting.api.response.ChatMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    @Query("""
            SELECT new com.idle.weather.chatting.api.response.ChatMessageResponse(
                                c.id, c.chatRoomId, c.senderId, c.message, c.timestamp
            )
            FROM ChatMessageEntity c
            WHERE c.chatRoomId = :chatRoomId
            ORDER BY c.timestamp DESC
            """)
    Page<ChatMessageResponse> findByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}
