package com.idle.weather.mission.api;

import com.idle.weather.mission.api.port.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MissionController {
    private final MissionService missionService;
}
