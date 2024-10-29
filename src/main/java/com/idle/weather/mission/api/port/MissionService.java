package com.idle.weather.mission.api.port;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.Mission;

import static com.idle.weather.mission.api.response.MissionResponseDto.*;

public interface MissionService {
    SingleMission createMission(Long userId , MissionRequestDto.CreateMission createMission);
}
