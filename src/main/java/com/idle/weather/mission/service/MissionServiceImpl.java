package com.idle.weather.mission.service;

import com.idle.weather.mission.api.port.MissionService;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.mock.MockFastApiService;
import org.springframework.stereotype.Service;

@Service
public class MissionServiceImpl implements MissionService {
    private MissionRepository missionRepository;
    private MockFastApiService fastApiService;
    @Override
    public Mission createMission(Long userId, int nx, int ny) {
        CurrentWeatherResponse currentWeatherInfo = fastApiService.getCurrentWeatherInfo(nx, ny);

        // currentWeatherInfo 를 기반으로 MissionRepository 에서 적절한 미션을 가지고 온다.

    }
}
