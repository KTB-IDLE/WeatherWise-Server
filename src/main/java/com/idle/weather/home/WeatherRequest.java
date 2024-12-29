package com.idle.weather.home;

public record WeatherRequest(
        double lat,
        double lon,
        String userId
) {
    public static WeatherRequest of(double latitude, double longitude, Long userId) {
        return new WeatherRequest(latitude, longitude, String.valueOf(userId));
    }
}