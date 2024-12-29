package com.idle.weather.home;

import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;

public record HomeResponseDto(
        boolean didSurvey,
        WeatherResponse weatherResponse,
        int size
) {
    public static HomeResponseDto from(boolean didSurvey) {
        return new HomeResponseDto(didSurvey, null, 0);
    }

    public static HomeResponseDto of(boolean didSurvey, WeatherResponse weatherResponse, int size) {
        return new HomeResponseDto(didSurvey, weatherResponse, size);
    }
}