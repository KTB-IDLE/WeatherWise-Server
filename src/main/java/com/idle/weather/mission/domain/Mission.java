package com.idle.weather.mission.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Mission {
    private Long id;
    private String name;
    private String description;
    private int point;
    private List<MissionHistory> missionHistories;
    private String uploadFileName;
    private String storeFileName;
}
