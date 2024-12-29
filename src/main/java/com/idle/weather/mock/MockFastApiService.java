package com.idle.weather.mock;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.MissionType;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;

/**
 * AI 서버 용 MOCK SERVICE
 */
@Service
public class MockFastApiService {
    private boolean toggle = true;

    public boolean missionAuthentication(MissionAuth missionAuthDto) {
        return false;
    }

    public CurrentWeatherResponse getCurrentWeatherInfo(int nx , int ny) {
        return new CurrentWeatherResponse(MissionType.HOT);
    }
}
