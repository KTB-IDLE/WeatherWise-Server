package com.idle.weather.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.idle.weather.home.HomeRequestDto.*;
import static com.idle.weather.home.HomeResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HomeService {

    private final UserService userService;
    private final AIServerClient aiServerClient;
    private final UserRepository userRepository;
    private final MissionHistoryRepository missionHistoryRepository;

    public HomeResponse getInfo(Long userId, double latitude , double longitude) throws JsonProcessingException {
        boolean result = userService.checkSurvey(userId);

        if (!result) {
            return HomeResponse.from(result);
        }

        WeatherResponse currentWeatherInfo = aiServerClient.getCurrentWeatherInfo(latitude, longitude, userId);

        List<MissionHistory> missionHistoryList = missionHistoryRepository.findMissionHistoriesByUserId(userId);

        log.info("size = {} " , missionHistoryList.size());

        // 미션 유무
        return HomeResponse.of(result , currentWeatherInfo,missionHistoryList.size());
    }

    @Transactional
    public void applySurveyResult(Long userId , SurveyDto surveyResult) {
        userService.applySurveyResult(userId,surveyResult);
    }
}
