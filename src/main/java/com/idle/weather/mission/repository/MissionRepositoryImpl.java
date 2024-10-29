package com.idle.weather.mission.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.repository.MissionHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository{
    private final MissionJpaRepository missionJpaRepository;

    @Override
    public Mission findById(Long id) {
        return missionJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION)).toDomain();
    }
    @Override
    public void save(MissionEntity mission) {
        missionJpaRepository.save(mission);
    }

    @Override
    public List<Mission> findByMissionType(MissionType missionType) {
        return missionJpaRepository.findByMissionType(missionType)
                .stream().map(MissionEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<MissionEntity> findAll() {
        return missionJpaRepository.findAll();
    }

}
