package com.idle.weather.missionhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
}
