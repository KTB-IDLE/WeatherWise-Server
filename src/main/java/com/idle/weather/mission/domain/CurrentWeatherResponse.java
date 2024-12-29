package com.idle.weather.mission.domain;

import com.idle.weather.mission.domain.WeatherResponseType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CurrentWeatherResponse {
    private Map<WeatherResponseType , Integer> result = new HashMap<>();

    private MissionType missionType;

    public CurrentWeatherResponse(MissionType missionType) {
        this.missionType = missionType;
    }
}
