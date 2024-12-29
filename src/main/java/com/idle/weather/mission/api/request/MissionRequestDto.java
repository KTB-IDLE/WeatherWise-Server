package com.idle.weather.mission.api.request;

import com.idle.weather.mission.domain.Mission;

public class MissionRequestDto {

    public record CreateMission(
            int nx,
            int ny,
            int missionTime
    ) {
    }

    public record MissionAuth(
            String missionName,
            String missionImageUrl,
            String question
    ) {
        public static MissionAuth of(String missionImageUrl, Mission mission) {
            return new MissionAuth(
                    mission.getName(),
                    missionImageUrl,
                    mission.getQuestion()
            );
        }
    }
}