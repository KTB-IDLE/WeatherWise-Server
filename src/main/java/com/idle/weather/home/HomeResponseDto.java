package com.idle.weather.home;

import jakarta.persistence.Column;
import lombok.*;

public class HomeResponseDto {


    @AllArgsConstructor @NoArgsConstructor
    @Builder @Getter @ToString
    public static class HomeResponse {
        private boolean didSurvey;

        public static HomeResponse from(boolean didSurvey) {
            return HomeResponse.builder()
                    .didSurvey(didSurvey)
                    .build();
        }
    }
}
