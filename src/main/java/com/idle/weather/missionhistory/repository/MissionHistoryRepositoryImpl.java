package com.idle.weather.missionhistory.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionHistoryRepositoryImpl implements MissionHistoryRepository {
    private final MissionHistoryJpaRepository missionHistoryJpaRepository;
    @Override
    public List<MissionHistory> findMissionHistoryByDate(Long userId , LocalDate date) {
        return missionHistoryJpaRepository.findMissionHistoryByDate(userId, date)
                .stream().map(MissionHistoryEntity::toDomain).toList();
    }

    @Override
    public MissionHistory findById(Long id) {
        return missionHistoryJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION_HISTORY)).toDomain();
    }

    @Override
    public MissionHistoryEntity findByIdEntity(Long id) {
        return missionHistoryJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION_HISTORY));
    }
    @Override
    public MissionHistory save(MissionHistory missionHistory) {
        return missionHistoryJpaRepository.save(MissionHistoryEntity.toEntity(missionHistory)).toDomain();
    }

    @Override
    public List<MissionHistory> findMissionHistoriesByUserId(Long userId) {
        return missionHistoryJpaRepository.findMissionHistoriesByUserId(userId)
                .stream()
                .map(MissionHistoryEntity::toDomain)
                .toList();
    }

    @Override
    public boolean hasUserCompletedAnyMission(Long userId, LocalDateTime time) {
        return missionHistoryJpaRepository.hasCompletedMissionToday(userId,time);
    }
}