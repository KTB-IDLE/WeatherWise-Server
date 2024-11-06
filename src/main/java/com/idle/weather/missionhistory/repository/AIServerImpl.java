package com.idle.weather.missionhistory.repository;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AIServerImpl implements AIServerClient {

    // 만약 RestTemplate 을 쓴다면?
    // private RestTemplate restTemplate = new RestTemplate();
    /**
     * 우선은 번갈아 가면서 성공 , 실패가 나오도록 한다.
     */
    private boolean toggle = true;
    @Override
    public boolean missionAuthentication(MissionRequestDto.MissionAuth missionAuthDto) {
        boolean result = toggle;
        toggle = !toggle;
        return result;
    }

    @Override
    public CurrentWeatherResponse getCurrentWeatherInfo(int nx, int ny) {
        return null;
    }
}
