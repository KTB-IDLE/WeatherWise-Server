package com.idle.weather.mission.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MissionResponseDto {

    @Builder @Getter
    public static class SingleMission {
        private String description;
        private boolean isCompleted;
        private int point;
    }

    @Builder @Getter
    public static class MissionInfo {
        private String userName;
        private List<SingleMission> missionList;
    }
}
