package com.idle.weather.home;

import jakarta.persistence.Column;
import lombok.*;

public class HomeResponseDto {


    @AllArgsConstructor @NoArgsConstructor
    @Builder @Getter @ToString
    public static class HomeResponse {
        private boolean didSurvey;
        private WeatherResponse weatherResponse;

        public static HomeResponse from(boolean didSurvey) {
            return HomeResponse.builder()
                    .didSurvey(didSurvey)
                    .build();
        }

        public static HomeResponse of(boolean didSurvey , WeatherResponse weatherResponse) {
            return HomeResponse.builder()
                    .didSurvey(didSurvey)
                    .weatherResponse(weatherResponse)
                    .build();
        }
    }
}
