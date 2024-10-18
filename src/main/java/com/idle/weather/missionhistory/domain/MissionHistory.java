package com.idle.weather.missionhistory.domain;

import com.idle.weather.mission.domain.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionHistory {
    // 도메인은 도메인끼리 보게끔 하면 된다.
    private Long id;
    private Mission mission;
    private boolean isCompleted;

    // 완료됐다면 업로드된 Image 링크
    private String uploadFileLink;

    public void updateCompleted() {
        this.isCompleted = true;
    }
}
