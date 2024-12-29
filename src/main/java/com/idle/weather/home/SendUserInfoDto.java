package com.idle.weather.home;

import com.idle.weather.user.api.request.SurveyRequestDto;

import java.util.List;

public record SendUserInfoDto(
        List<Integer> answers,
        Long userId
) {
    public static SendUserInfoDto of(SurveyRequestDto surveyResult, Long userId) {
        return new SendUserInfoDto(surveyResult.answers(), userId);
    }
}