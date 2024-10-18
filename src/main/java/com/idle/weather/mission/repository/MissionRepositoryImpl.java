package com.idle.weather.mission.repository;

import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.repository.MissionHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl  implements MissionRepository {
    private final MissionJpaRepository missionJpaRepository;
}
