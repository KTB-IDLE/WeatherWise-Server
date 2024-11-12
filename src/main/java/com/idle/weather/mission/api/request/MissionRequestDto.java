package com.idle.weather.mission.api.request;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.user.api.request.UserRequestDto;
import com.idle.weather.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.idle.weather.user.api.request.UserRequestDto.*;

public class MissionRequestDto {
    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class CreateMission {
        // 예보지점의 X 좌표값
        private int nx;
        // 예보지점의 Y 좌표값
        private int ny;
        // 미션 타임
        private String missionTime;
    }


    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class MissionAuth {
        private String missionName;
        private String missionImageUrl;
        private String question;

        public static MissionAuth of(String missionImageUrl,Mission mission) {
            return MissionAuth.builder()
                    .missionImageUrl(missionImageUrl)
                    .missionName(mission.getName())
                    .question(mission.getQuestion())
                    .build();
        }
    }


}
