package com.idle.weather.mission.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MissionRequestDto {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMission {
        // 예보지점의 X 좌표값
        private int nx;
        // 예보지점의 Y 좌표값
        private int ny;
    }
}
