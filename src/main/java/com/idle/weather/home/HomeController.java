package com.idle.weather.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idle.weather.common.annotation.UserId;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.dto.SurveyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.idle.weather.home.HomeRequestDto.*;
import static com.idle.weather.home.HomeResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {
    private final HomeService homeService;

    /**
     * Home API
     */
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<HomeResponse>> home(@UserId Long userId , @RequestParam("latitude") double latitude,
                                                           @RequestParam("longitude") double longitude) throws JsonProcessingException {
        return ResponseEntity.ok().body(new BaseResponse<>(homeService.getInfo(userId,latitude,longitude)));
    }

    /**
     * 설문조사 반영 API
     */
    @PatchMapping("/survey")
    public ResponseEntity<String> applySurveyResult(@UserId Long userId, @RequestBody SurveyDto surveyResult) {
        homeService.applySurveyResult(userId, surveyResult);
        System.out.println("applySurveyResult 반영");
        return ResponseEntity.ok("설문조사 완료");
    }
}
