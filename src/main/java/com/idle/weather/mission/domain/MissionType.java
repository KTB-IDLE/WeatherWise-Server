package com.idle.weather.mission.domain;

public enum MissionType {
    HOT("더운 날씨"),
    COLD("추운 날씨"),
    RAIN("비 오는 날씨"),
    SNOW("눈 오는 날씨"),
    SUNNY("화창한 날씨");
    MissionType(String description) {
    }
}
