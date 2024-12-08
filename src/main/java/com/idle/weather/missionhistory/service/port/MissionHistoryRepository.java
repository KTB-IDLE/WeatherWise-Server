package com.idle.weather.missionhistory.service.port;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.missionhistory.repository.MissionHistoryRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MissionHistoryRepository {
    List<MissionHistory> findMissionHistoryByDate(Long userId , LocalDate date);
    MissionHistory findById(Long id);
    MissionHistoryEntity findByIdEntity(Long id);
    MissionHistory save(MissionHistory missionHistory);
    List<MissionHistory> findMissionHistoriesByUserId(Long userId);
    boolean hasUserCompletedAnyMission(Long userId , LocalDateTime time);
}
