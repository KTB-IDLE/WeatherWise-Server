package com.idle.weather.missionhistory.domain;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionHistory {
    // 도메인은 도메인끼리 보게끔 하면 된다.
    private Long id;
    private Mission mission;
    private boolean isCompleted;
    private MissionTime missionTime;
    private User user;
    private String uploadFileName;
    private String storeFileName;
    public void updateCompleted() {
        this.isCompleted = true;
    }

    public static MissionHistory of(User user , Mission mission , MissionTime missionTime) {
        return MissionHistory.builder()
                .mission(mission)
                .user(user)
                .isCompleted(false)
                .missionTime(missionTime)
                .build();
    }
}
