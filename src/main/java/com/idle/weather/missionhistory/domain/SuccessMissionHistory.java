package com.idle.weather.missionhistory.domain;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SuccessMissionHistory {
    private Long id;
    private Mission mission;
    private MissionTime missionTime;
    private String uploadFileName;
    private String storeFileName;
    private User user;
    private LocalDateTime successTime;

    public static SuccessMissionHistory from(MissionHistory mh) {
        return SuccessMissionHistory.builder()
                .id(mh.getId())
                .mission(mh.getMission())
                .missionTime(mh.getMissionTime())
                .storeFileName(mh.getStoreFileName())
                .uploadFileName(mh.getUploadFileName())
                .user(mh.getUser())
                .successTime(LocalDateTime.now())
                .build();
    }


}
