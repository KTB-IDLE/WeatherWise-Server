package com.idle.weather.mission.repository;

import com.idle.weather.mission.domain.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionJpaRepository extends JpaRepository<MissionEntity , Long> {
    List<MissionEntity> findByMissionType(MissionType missionType);
}
