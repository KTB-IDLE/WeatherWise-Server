package com.idle.weather.mock;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.missionhistory.service.port.AIServerClient;

public class FakeAIServerClient implements AIServerClient {
    @Override
    public boolean missionAuthentication(MissionRequestDto.MissionAuth missionAuthDto) {
        return false;
    }

    @Override
    public CurrentWeatherResponse getCurrentWeatherInfo(int nx, int ny) {
        return null;
    }
}
