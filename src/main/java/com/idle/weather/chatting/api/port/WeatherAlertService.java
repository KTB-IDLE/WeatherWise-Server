package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.response.WeatherAlertResponse;

import java.util.List;

public interface WeatherAlertService {

    void updateWeatherAlerts();
    void deactivateExpiredAlerts();
    void deleteOldDeactivatedAlerts(int day);
    List<WeatherAlertResponse> getAllWeatherAlerts();
    List<WeatherAlertResponse> getAllActivatedWeatherAlerts();
    WeatherAlertResponse getWeatherAlertById(Long weatherAlertId);
}
