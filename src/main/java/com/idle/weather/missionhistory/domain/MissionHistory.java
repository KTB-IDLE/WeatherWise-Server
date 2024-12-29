package com.idle.weather.missionhistory.domain;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MissionHistory {
    private Long id;
    private Mission mission;
    private boolean isCompleted;
    private MissionTime missionTime;
    private String uploadFileName;
    private String storeFileName;
    private User user;
    private LocalDateTime completedAt;
    public void updateCompleted() {
        this.isCompleted = true;
        completedAt = LocalDateTime.now();
    }

    public static MissionHistory of(User user,Mission mission , MissionTime missionTime) {
        return MissionHistory.builder()
                .mission(mission)
                .isCompleted(false)
                .missionTime(missionTime)
                .user(user)
                .build();
    }

    public void updateImageUrl(String uploadFileName , String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
