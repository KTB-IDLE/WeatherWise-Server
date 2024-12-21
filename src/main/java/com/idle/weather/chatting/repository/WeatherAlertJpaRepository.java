package com.idle.weather.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherAlertJpaRepository extends JpaRepository<WeatherAlertEntity, Long> {

    @Query("""
            SELECT w
            FROM WeatherAlertEntity w
            WHERE w.endTime IS NOT NULL AND w.endTime < CURRENT_TIMESTAMP
            """)
    List<WeatherAlertEntity> findExpiredAlerts();

    @Query("""
            SELECT w
            FROM WeatherAlertEntity w
            WHERE w.isActivated = false AND w.deactivatedAt IS NOT NULL AND w.deactivatedAt < :time""")
    List<WeatherAlertEntity> findAllDeactivatedOlderThan(@Param("time") LocalDateTime time);

    @Query("""
            SELECT w
            FROM WeatherAlertEntity w
            WHERE w.isActivated = true
            """)
    List<WeatherAlertEntity> findAllActivatedAlerts(); // ChatRoomEntity 안의 'chatRoomName' 필드가 있다고 가정

    @Query("""
                SELECT w
                FROM WeatherAlertEntity w
                JOIN FETCH w.chatRoom c
                WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(w.parentRegionName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<WeatherAlertEntity> findByKeywordInChatRoomOrParentRegion(@Param("keyword") String keyword);
}
