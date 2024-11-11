package com.idle.weather.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.dto.SurveyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.idle.weather.home.HomeRequestDto.*;
import static com.idle.weather.home.HomeResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HomeService {

    private final UserService userService;
    private final AIServerClient aiServerClient;

    public HomeResponse getInfo(Long userId, double latitude , double longitude) throws JsonProcessingException {
        boolean result = userService.checkSurvey(userId);

        if (!result) {
            return HomeResponse.from(result);
        }

        WeatherResponse currentWeatherInfo = aiServerClient.getCurrentWeatherInfo(latitude, longitude, userId);
        log.info("getInfo 응답 완료");
        return HomeResponse.of(result , currentWeatherInfo);

    }

    @Transactional
    public void applySurveyResult(Long userId , SurveyDto surveyResult) {
        userService.applySurveyResult(userId,surveyResult);
    }
}
