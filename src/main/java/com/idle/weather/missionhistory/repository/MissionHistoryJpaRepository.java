package com.idle.weather.missionhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MissionHistoryJpaRepository extends JpaRepository<MissionHistoryEntity , Long> {
    @Query("SELECT MH FROM MissionHistoryEntity AS MH WHERE MH.user.id = :userId " +
            "AND MH.createdAt BETWEEN :#{#date.atStartOfDay()} AND :#{#date.plusDays(1).atStartOfDay()}")
    List<MissionHistoryEntity> findMissionHistoryByDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date);

    @Query("SELECT m FROM MissionHistoryEntity m " +
            "JOIN m.user u " +
            "WHERE u.id = :userId")
    List<MissionHistoryEntity> findMissionHistoriesByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(mh) > 0 THEN TRUE ELSE FALSE END " +
            "FROM  MissionHistoryEntity AS mh " +
            "WHERE mh.user.id = :userId " +
            "AND mh.isCompleted = TRUE " +
            "AND mh.createdAt >= :#{#date.toLocalDate().atStartOfDay()} " +
            "AND mh.createdAt < :#{#date.toLocalDate().plusDays(1).atStartOfDay()}")
    boolean hasCompletedMissionToday(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    @Query("SELECT mh.mission.id FROM MissionHistoryEntity mh WHERE mh.user.id = :userId AND mh.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<Long> findMissionIdsByUserAndDateRange(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
