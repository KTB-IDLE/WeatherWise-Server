package com.idle.weather.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherAlertJpaRepository extends JpaRepository<WeatherAlertEntity ,Long> {

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
    List<WeatherAlertEntity> findAllActivatedAlerts();
}
