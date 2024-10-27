package com.idle.weather.mission.api.port;

import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.Mission;

import static com.idle.weather.mission.api.response.MissionResponseDto.*;

public interface MissionService {
    SingleMission createMission(Long userId , int nx , int ny);
}
