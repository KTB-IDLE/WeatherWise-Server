package com.idle.weather.home;

import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.dto.SurveyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.idle.weather.home.HomeResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HomeService {

    private final UserService userService;

    public HomeResponse getInfo(Long userId) {
        boolean result = userService.checkSurvey(userId);
        log.info("result = {} " , result);
        // 여기에 AI 서버와 통신하는 로직 추가 예정
        return HomeResponse.from(result);
    }

    @Transactional
    public void applySurveyResult(Long userId , SurveyDto surveyResult) {
        userService.applySurveyResult(userId,surveyResult);
    }
}
