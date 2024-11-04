package com.idle.weather.mock;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeMissionHistoryRepository implements MissionHistoryRepository {
    private static Long id = 0L;
    private final List<MissionHistory> data = new ArrayList<>();

    @Override
    public MissionHistory findById(Long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION_HISTORY));
    }

    @Override
    public MissionHistory save(MissionHistory missionHistory) {
        if (missionHistory.getId() == null || missionHistory.getId() == 0) {
            MissionHistory newMissionHistory = MissionHistory.builder()
                    .id(id++)
                    .mission(missionHistory.getMission())
                    .missionTime(missionHistory.getMissionTime())
                    .user(missionHistory.getUser())
                    .build();

            data.add(newMissionHistory);
            return newMissionHistory;
        } else {
            // 같은 User 라면 기존에 것을 삭제하고 새로 추가
            data.removeIf(item -> Objects.equals(item.getId() , missionHistory.getId()));
            data.add(missionHistory);
            return missionHistory;
        }
    }

    @Override
    public List<MissionHistory> findMissionHistoryByDate(Long userId, LocalDate date) {
        return null;
    }


    @Override
    public MissionHistoryEntity findByIdEntity(Long id) {
        return null;
    }


}
