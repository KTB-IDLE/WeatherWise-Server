package com.idle.weather.mission.api.response;

import com.idle.weather.mission.domain.Mission;

import java.util.List;

public class MissionResponseDto {

    public record SingleMission(
            Long id,
            String name,
            boolean isCompleted,
            String description,
            int point
    ) {
        public static SingleMission of(Mission mission, Long missionHistoryId) {
            return new SingleMission(
                    missionHistoryId,
                    mission.getName(),
                    mission.isCompleted(),
                    mission.getDescription(),
                    mission.getPoint()
            );
        }
    }

    public record MissionInfo(
            String userName,
            List<SingleMission> missionList
    ) {
    }
}