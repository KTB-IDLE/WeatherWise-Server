package com.idle.weather.missionhistory.repository;

import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MissionHistoryRepositoryImpl implements MissionHistoryRepository {
    private final MissionHistoryJpaRepository missionHistoryJpaRepository;
}
