package com.idle.weather.mission.service;

import com.idle.weather.mission.api.port.MissionService;
import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.mock.MockFastApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.idle.weather.mission.api.response.MissionResponseDto.*;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {
    private final MissionRepository missionRepository;
    private final MockFastApiService fastApiService;
    private final Random random = new Random();
    @Override
    public SingleMission createMission(Long userId, int nx, int ny) {
        // FastAPI 에서 받은 결과를 적절하게 파싱
        CurrentWeatherResponse currentWeatherInfo = fastApiService.getCurrentWeatherInfo(nx, ny);

        // currentWeatherInfo 를 기반으로 MissionRepository 에서 적절한 미션을 가지고 온다.
        List<Mission> missions = missionRepository.findByMissionType(currentWeatherInfo.getMissionType());

        // 랜덤으로 미션 뽑기 (0부터 missions.size() - 1 사이의 랜덤 인덱스를 선택)
        // TODO: 10/27/24 중복된 미션이 만들어지지 않도록 중복 체크 로직 추가하기
        int randomIndex = random.nextInt(missions.size());
        return SingleMission.from(missions.get(randomIndex));
    }
}
