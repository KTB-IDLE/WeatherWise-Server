package com.idle.weather.mission.domain;

import com.idle.weather.missionhistory.domain.MissionHistory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Mission {
    private Long id;
    private String name;
    private String description;
    private MissionType missionType;
    private int point;
    private boolean isCompleted;
    // AI 서버에 보낼 영문 질문
    private String question;
}
