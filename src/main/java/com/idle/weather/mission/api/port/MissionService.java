package com.idle.weather.mission.api.port;

import com.idle.weather.mission.domain.Mission;

public interface MissionService {
    Mission createMission(Long userId , int nx , int ny);
}
