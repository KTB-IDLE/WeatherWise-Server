package com.idle.weather.home;

import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import com.idle.weather.missionhistory.domain.MissionHistory;
import jakarta.persistence.Column;
import lombok.*;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

public class HomeResponseDto {


    @AllArgsConstructor @NoArgsConstructor
    @Builder @Getter @ToString
    public static class HomeResponse {
        private boolean didSurvey;
        private WeatherResponse weatherResponse;
        private int size;

        public static HomeResponse from(boolean didSurvey) {
            return HomeResponse.builder()
                    .didSurvey(didSurvey)
                    .build();
        }

        public static HomeResponse of(boolean didSurvey , WeatherResponse weatherResponse , int size) {
            return HomeResponse.builder()
                    .didSurvey(didSurvey)
                    .weatherResponse(weatherResponse)
                    .size(size)
                    .build();
        }
    }
}
