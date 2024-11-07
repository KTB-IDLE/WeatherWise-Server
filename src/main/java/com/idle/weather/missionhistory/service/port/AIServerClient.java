package com.idle.weather.missionhistory.service.port;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;

/**
 * AI 서버와 통신하는 로직을 추상화
 * WebClient , RestTemplate , FeignClient , gRPC?
 */
public interface AIServerClient {
    // 미션 인증 API
    boolean missionAuthentication(MissionAuth missionAuthDto);

    // 날씨 가져오는 API
    CurrentWeatherResponse getCurrentWeatherInfo(int nx , int ny);
}