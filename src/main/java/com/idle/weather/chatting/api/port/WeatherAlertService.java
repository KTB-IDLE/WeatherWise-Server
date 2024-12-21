package com.idle.weather.chatting.api.port;

import com.idle.weather.chatting.api.response.WeatherAlertResponse;
import com.idle.weather.chatting.repository.WeatherAlertEntity;

import java.util.List;

public interface WeatherAlertService {

    void updateWeatherAlerts();
    void deactivateExpiredAlerts();
    void deleteOldDeactivatedAlerts(int day);
    List<WeatherAlertResponse> getAllWeatherAlerts();
    List<WeatherAlertResponse> getAllActivatedWeatherAlerts();
    WeatherAlertResponse getWeatherAlertById(Long weatherAlertId);

    List<WeatherAlertEntity> getRegionList(String region);
}
