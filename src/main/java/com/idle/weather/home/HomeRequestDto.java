package com.idle.weather.home;

import lombok.*;

public class HomeRequestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder @Getter
    public static class HomeRequest {
        // 위도
        private double latitude;
        // 경도
        private double longitude;
    }
}
