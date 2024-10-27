package com.idle.weather.mission.api.response;

import com.idle.weather.mission.domain.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MissionResponseDto {

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class SingleMission {
        private Long id;
        private String name;
        private boolean isCompleted;
        private int point;

        public static SingleMission from(Mission mission) {
            return SingleMission.builder()
                    .id(mission.getId())
                    .name(mission.getName())
                    .point(mission.getPoint())
                    .isCompleted(mission.isCompleted())
                    .build();
        }
    }

    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class MissionInfo {
        private String userName;
        private List<SingleMission> missionList;
    }
}
