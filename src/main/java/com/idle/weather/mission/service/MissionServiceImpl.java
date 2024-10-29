package com.idle.weather.mission.service;

import com.idle.weather.mission.api.port.MissionService;
import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.mock.MockFastApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;
import static com.idle.weather.mission.api.response.MissionResponseDto.*;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {
    private final MissionRepository missionRepository;
    private final MockFastApiService fastApiService;
    private final MissionHistoryService missionHistoryService;
    private final Random random = new Random();
    @Override
    public SingleMission createMission(Long userId, CreateMission createMission) {
        CurrentWeatherResponse currentWeatherInfo = fastApiService
                .getCurrentWeatherInfo(createMission.getNx(),createMission.getNy());

        // currentWeatherInfo.missionType 을 통해 MissionRepository 에서 미션 리스트들을 가지고 온다.
        List<Mission> missions = missionRepository.findByMissionType(currentWeatherInfo.getMissionType());

        // 랜덤으로 미션 뽑기 (0부터 missions.size() - 1 사이의 랜덤 인덱스를 선택)
        // TODO: 10/27/24 중복된 미션이 만들어지지 않도록 중복 체크 로직 추가하기
        int randomIndex = random.nextInt(missions.size());
        Mission mission = missions.get(randomIndex);

        MissionTime missionTime = MissionTime.fromValue(createMission.getMissionTime());

        // MissionHistory 에 저장
        MissionHistory missionHistory = missionHistoryService.save(userId, mission, missionTime);

        return SingleMission.of(mission , missionHistory.getId());
    }
}
