package com.idle.weather.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMemberEntity, Long> {

    @Modifying
    @Query("""
            DELETE
            FROM ChatRoomMemberEntity m
            WHERE m.chatRoom.id = :chatRoomId AND m.userId = :userId
            """)
    void deleteByChatRoomIdAndUserId(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    @Query("""
            SELECT COUNT(m) > 0
            FROM ChatRoomMemberEntity m
            WHERE m.chatRoom.id = :chatRoomId AND m.userId = :userId
            """)
    boolean existsByChatRoomIdAndUserId(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    @Query("""
            SELECT m.userId
            FROM ChatRoomMemberEntity m
            WHERE m.chatRoom.id = :chatRoomId
            """)
    List<Long> findUserIdsByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Query("""
        SELECT crm
        FROM ChatRoomMemberEntity crm
        JOIN FETCH crm.chatRoom cr
        JOIN FETCH UserEntity u ON crm.userId = u.id
        WHERE cr.id = :chatRoomId AND crm.userId = :userId
        """)
    Optional<ChatRoomMemberEntity> findWithUserByChatRoomIdAndUserId(
            @Param("chatRoomId") Long chatRoomId,
            @Param("userId") Long userId
    );
}
