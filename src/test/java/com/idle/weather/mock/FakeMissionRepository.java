package com.idle.weather.mock;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.repository.MissionEntity;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeMissionRepository implements MissionRepository {
    private static Long id = 0L;
    private final List<Mission> data = new ArrayList<>();

    public FakeMissionRepository() {
        Mission hotMission = Mission.builder()
                .id(1L)
                .missionType(MissionType.HOT)
                .point(50)
                .name("HOT 테스트")
                .description("HOT 테스트")
                .build();
        data.add(hotMission);

        Mission coldMission = Mission.builder()
                .id(2L)
                .missionType(MissionType.COLD)
                .point(50)
                .name("COLD 테스트")
                .description("COLD 테스트")
                .build();
        data.add(coldMission);

        Mission rainMission = Mission.builder()
                .id(3L)
                .missionType(MissionType.RAIN)
                .point(50)
                .name("RAIN 테스트")
                .description("RAIN 테스트")
                .build();
        data.add(rainMission);

        Mission snowMission = Mission.builder()
                .id(4L)
                .missionType(MissionType.SNOW)
                .point(50)
                .name("SNOW 테스트")
                .description("SNOW 테스트")
                .build();
        data.add(snowMission);
    }

    @Override
    public Mission findById(Long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MISSION));
    }

    @Override
    public void save(Mission mission) {
        data.add(mission);
    }

    @Override
    public List<Mission> findByMissionType(MissionType missionType) {
        return data.stream().filter(item -> item.getMissionType().equals(missionType)).collect(toList());
    }

    @Override
    public List<Mission> findAll() {
        return null;
    }
}
