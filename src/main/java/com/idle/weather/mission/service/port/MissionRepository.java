package com.idle.weather.mission.service.port;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.repository.MissionEntity;

import java.util.List;

public interface MissionRepository {
    Mission findById(Long id);
    void save(Mission mission);
    List<Mission> findByMissionType(MissionType missionType);
    List<Mission> findAll();
}
