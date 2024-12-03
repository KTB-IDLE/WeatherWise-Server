package com.idle.weather.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    boolean existsByName(String name);

    boolean existsByParentRegionCode(String parentRegionCode);

    Optional<ChatRoomEntity> findByParentRegionCode(String parentRegionCode);

    @Query("""
            SELECT c
            FROM ChatRoomEntity c
            JOIN FETCH c.weatherAlerts
            WHERE c.isActivated = true
            """)
    List<ChatRoomEntity> findActivatedChatRoomsByWeatherAlert();

    @Query("""
            SELECT c
            FROM ChatRoomEntity c
            WHERE c.isActivated = true
            """)
    List<ChatRoomEntity> findAllActivatedChatRooms();

    @Query("""
            SELECT c
            FROM ChatRoomEntity c
            WHERE c.isActivated = false AND c.deactivatedAt IS NOT NULL AND c.deactivatedAt < :time
            """)
    List<ChatRoomEntity> findAllDeactivatedOlderThan(LocalDateTime time);
}
