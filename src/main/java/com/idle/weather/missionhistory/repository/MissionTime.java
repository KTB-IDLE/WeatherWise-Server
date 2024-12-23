package com.idle.weather.missionhistory.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;


public enum MissionTime {
    MORNING(1, "아침"),
    AFTERNOON(2, "점심"),
    EVENING(3, "저녁");

    private final int value;
    private final String displayName;

    MissionTime(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MissionTime fromValue(int missionTime) {
        for (MissionTime time : MissionTime.values()) {
            if (time.getValue() == missionTime) {
                return time;
            }
        }
        throw new BaseException(ErrorCode.INVALID_MISSION_TIME);
    }
}