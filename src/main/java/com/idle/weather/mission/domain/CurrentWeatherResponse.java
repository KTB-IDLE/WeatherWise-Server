package com.idle.weather.mission.domain;

import com.idle.weather.mission.domain.WeatherResponseType;

import java.util.HashMap;
import java.util.Map;

public class CurrentWeatherResponse {
    // 1. 강수량
    // 2. 온도
    // 3. ...
    private Map<WeatherResponseType , Integer> result = new HashMap<>();
}