package com.idle.weather.mission.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.repository.MissionHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository{
    private final MissionJpaRepository missionJpaRepository;

    @Override
    public Mission findById(Long id) {
        return missionJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION)).toDomain();
    }
}
