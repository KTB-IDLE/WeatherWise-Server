package com.idle.weather.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @Getter
@AllArgsConstructor @NoArgsConstructor
public class WeatherRequest {
    private double lat;
    private double lon;
    private String user_id;

    public static WeatherRequest of(double latitude, double longitude, Long userId) {
        return WeatherRequest.builder()
                .lat(latitude)
                .lon(longitude)
                .user_id(String.valueOf(userId))
                .build();
    }
}
