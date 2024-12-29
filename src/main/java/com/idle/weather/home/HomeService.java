package com.idle.weather.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.request.SurveyRequestDto;

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

    public HomeResponseDto getInfo(Long userId, double latitude , double longitude) throws JsonProcessingException {
        boolean result = userService.checkSurvey(userId);

        if (!result) {
            return HomeResponseDto.from(result);
        }

        WeatherResponse currentWeatherInfo = aiServerClient.getCurrentWeatherInfo(latitude, longitude, userId);

        List<MissionHistory> missionHistoryList = missionHistoryRepository.findMissionHistoriesByUserId(userId);

        // 미션 유무
        return HomeResponseDto.of(result , currentWeatherInfo,missionHistoryList.size());
    }

    @Transactional
    public void applySurveyResult(Long userId , SurveyRequestDto surveyResult) {
        User user = userRepository.findById(userId);
        user.applySurveyResult(surveyResult);
        userRepository.save(user);
    }

    public boolean checkSurvey(Long userId) {
        return userRepository.checkSurvey(userId);
    }
}
