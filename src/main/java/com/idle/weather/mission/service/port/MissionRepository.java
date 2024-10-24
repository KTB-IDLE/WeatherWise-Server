package com.idle.weather.mission.service.port;

import com.idle.weather.mission.domain.Mission;

public interface MissionRepository {
    Mission findById(Long id);
}
