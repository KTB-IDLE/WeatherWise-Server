package com.idle.weather.missionhistory.service.port;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;

import java.time.LocalDate;
import java.util.List;

public interface MissionHistoryRepository {
    List<MissionHistory> findMissionHistoryByDate(Long userId , LocalDate date);
    MissionHistory findById(Long id);
    MissionHistoryEntity findByIdEntity(Long id);
    MissionHistory save(MissionHistory missionHistory);
}
